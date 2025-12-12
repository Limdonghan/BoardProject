package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.*;
import com.example.BoardProject_back.entity.*;
import com.example.BoardProject_back.repository.*;
import com.example.BoardProject_back.service.AwsService;
import com.example.BoardProject_back.service.GradeService;
import com.example.BoardProject_back.service.PostService;
import com.example.BoardProject_back.service.TypesenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PostReactionRepository postReactionRepository;
    private final GradeService gradeService;
    private final CommentRepository commentRepository;
    private final TypesenseService typesenseService;

    private final AwsService awsService;

    /**
     * 게시글 작성
     */
    @Override
    @Transactional
    public void postCreation(PostDTO postDTO, UserEntity userEntity) {

        CategoryEntity categoryEntity = categoryRepository.findById(postDTO.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없음"));

        PostEntity build = PostEntity.builder()
                .title(postDTO.getTitle())
                .user(userEntity)
                .category(categoryEntity)
                .context(postDTO.getContext())
                .build();
        PostEntity savedPost = postRepository.save(build);

        /// [추가] Image Table 저장
        if (postDTO.getImageUrl() != null && !postDTO.getImageUrl().isEmpty()) {
            for (String url : postDTO.getImageUrl()) {

                /// URL에서 파일명만 추출
                String originalFilename = extractFileNameFromUrl(url);

                ImageEntity imageEntity = ImageEntity.builder()
                        .post(build)
                        .url(url)
                        .originalName(originalFilename)
                        .build();
                imageRepository.save(imageEntity);
            }
        }

        UserEntity user = userRepository.findById(userEntity.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 작성한 유저를 찾을 수 없음 ??"));

        /// 포인트 지급
        user.userAddPoint(PointRole.POST_CREATION);

        /// 등급심사
        gradeService.gradeAssessment(userEntity);

        /// typesense 저장
        typesenseService.indexPost(savedPost);
    }

    /**
     *  [추가] 파일명 추출 메서드
     */
    private String extractFileNameFromUrl(String url) {
        try {
            /// URL 인코딩
            String fileName = URLDecoder.decode(url, StandardCharsets.UTF_8);
            /// 마지막 '/'뒤의 문자열만 자르기
            return fileName.substring(fileName.lastIndexOf('_') + 1);
        }catch (Exception e) {
            return url;
        }
    }

    /**
     * 게시글 상세조회 (로그인 / 비로그인 다 가능)
     */
    @Override
    @Transactional
    public PostInfoDTO getPostInfo(int id) {

        /// 게시글 조회수 증가
        postRepository.increaseViewCount(id);

        PostEntity postEntity = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 개시글이 존재하지 않거나 삭제된 게시글 입니다!!"));

        /// 게시글에 해당되는 이미지 찾기
        List<ImageEntity> list = imageRepository.findAllByPostId(id);
        List<String> imageUrlList = list.stream()
                .map(imageEntity -> imageEntity.getUrl())
                .collect(Collectors.toList());


        String nickName = postEntity.getUser().getNickName();
        String category = postEntity.getCategory().getCategory();

        return PostInfoDTO.builder()
                .title(postEntity.getTitle())
                .user(nickName)
                .category(category)
                .context(postEntity.getContext())
                .postView(postEntity.getPostView())
                .likeCount(postEntity.getLikeCount()-postEntity.getDisLikeCount())
                .disLikeCount(postEntity.getDisLikeCount())
                .imageUrl(imageUrlList)
                .date(postEntity.getCreatedAt())
                .build();
    }

    /**
     * 게시글 업데이트
     */
    @Override
    @Transactional
    public void postUpdate(int id, PostUpdateDTO postUpdateDTO, UserEntity userEntity) {
        PostEntity post = postCheck(id, userEntity);

        CategoryEntity categoryEntity = categoryRepository.findById(postUpdateDTO.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 카테고리가 없음!"));

        post.postUpdate(
                postUpdateDTO.getTitle(),
                postUpdateDTO.getContext(),
                categoryEntity
        );

        /// typesense 저장
        typesenseService.indexPost(post);
    }

    /**
     * 게시글 삭제 ( Soft Delete )
     */
    @Override
    @Transactional
    public void postDelete(int id, UserEntity userEntity) {
        PostEntity post = postCheck(id, userEntity);
        post.postDelete();

        /// typesense 삭제
        typesenseService.deletePost(id);

    }

    /**
     * 게시글 체크
     */
    private PostEntity postCheck(int id, UserEntity userEntity) {

        /// 게시글 조회
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글!!"));

        /// 게시글에 등록된 유저 = 유저 일치 검증
        if (post.getUser().getId() != userEntity.getId()) {
            throw new RuntimeException("작성자만 삭제/수정 할 수 있습니다.");
        }
        return post;
    }

    /**
     * 게시글 좋아요 / 싫어요
     */
    @Override
    @Transactional
    public void handleReaction(int id, UserEntity userEntity, PostReactionDTO postReactionDTO) {
        /// 게시글 찾기
        PostEntity postEntity = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없음! 존재하지 않거나 삭제된것임"));

        /// 게시글 작성자 찾기
        UserEntity author = postEntity.getUser();

        /// 중복 반응 체크 (이미 좋아요 또는 싫어요가 눌렸는치 체크)
        if(postReactionRepository.existsByUserAndPost(userEntity, postEntity)){
            throw new IllegalStateException("이미 반응을 남겼습니다!");
        }

        /// 처음으로 반응(좋야요 or 싫어요)할 때 저장
        PostReactionEntity build = PostReactionEntity.builder()
                .post(postEntity)
                .user(userEntity)
                .reactionType(postReactionDTO.getRectionType())
                .build();
        postReactionRepository.save(build);


        /// 포인트 자추 금지
        boolean isSelfReaction = author.getEmail().equals(userEntity.getEmail());

        if (postReactionDTO.getRectionType().equals("LIKE")) {
            postEntity.likeHandle(postEntity.getLikeCount());       /// 좋아요 증가

            /// 본인이 작성한 게시글이 아는글에 좋아요를 누르면 포인트 지급
            if (!isSelfReaction) {
                author.userAddPoint(PointRole.LIKE);
            }

        } else if (postReactionDTO.getRectionType().equals("DISLIKE")) {
            postEntity.disLikeHandle(postEntity.getDisLikeCount());  /// 싫어요 증가

            /// 본인이 작성한 게시글이 아는글에 싫어요를 누르면 포인트 감소
            if (!isSelfReaction) {
                author.userAddPoint(PointRole.DISLIKE);
            }

            /// 포인트가 -1이 되면 0으로 초기화
            if (author.getPoint() < 0) {
                author.setPoint(0);
            }
        } else{
            throw new IllegalArgumentException("잘못된 처리 방식 입니다");
        }

        /// 게시글 작성자 등급 심사
        gradeService.gradeAssessment(author);

    }



    /**
     * 내가 작성한 게시글 목록 조회
     */
    @Override
    public MyPostListDTO getMyPostList(UserEntity userEntity) {

        /// 게시글 리스트 조회
        List<PostEntity> myPost = postRepository.findAllByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userEntity.getId());

        /// 게시글 전체 수 조회
        int totalPostCount = postRepository.countByUserIdAndIsDeletedFalse(userEntity.getId());

        /// DTO 매핑
        List<MyPostDTO> postDTOS = myPost.stream().map(
                        postEntity -> MyPostDTO.builder()
                                .id(postEntity.getId())
                                .authorName(postEntity.getUser().getNickName())
                                .title(postEntity.getTitle())
                                .category(postEntity.getCategory().getCategory())
                                .viewCount(postEntity.getPostView())
                                .likeCount(postEntity.getLikeCount()-postEntity.getDisLikeCount())
                                .createDate(postEntity.getCreatedAt())
                                .commentCount(commentRepository.countByPostIdAndIsDeletedFalse(postEntity.getId()))
                                .build())
                .collect(Collectors.toList());

        return MyPostListDTO.builder()
                .totalPostCount(totalPostCount)
                .myPostList(postDTOS)
                .build();
    }

    /**
     * Pageable 전체
     */
    @Override
    public Page<PostListPageDTO> getBoardList(Pageable pageable) {
        Page<PostEntity> postPage = postRepository.findAllByIsDeletedFalseOrderByCreatedAtDesc(pageable);
        return postPage.map(PostListPageDTO -> new PostListPageDTO(PostListPageDTO));
    }


    /**
     * Pageable 카테고리별
     */
    @Override
    public Page<PostListPageDTO> getCategoryBoardList(Pageable pageable, int categoryId) {
        Page<PostEntity> postPage = postRepository.findAllByCategoryIdAndIsDeletedFalseOrderByCreatedAtDesc(pageable, categoryId);
        return postPage.map(PostListPageDTO -> new PostListPageDTO(PostListPageDTO));
    }

}
