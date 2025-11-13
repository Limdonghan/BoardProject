package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.PostDTO;
import com.example.BoardProject_back.dto.PostInfoDTO;
import com.example.BoardProject_back.dto.PostUpdateDTO;
import com.example.BoardProject_back.entity.CategoryEntity;
import com.example.BoardProject_back.entity.PostEntity;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.jwt.JwtProvider;
import com.example.BoardProject_back.repository.CategoryRepository;
import com.example.BoardProject_back.repository.PostRepository;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.service.PostService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final JwtProvider jwtProvider;


    /**
     * 게시글 작성
     */
    @Override
    @Transactional
    public void postCreation(PostDTO postDTO, String accessToken) {
        UserEntity userEntity = tokenCheck(accessToken);
        CategoryEntity categoryEntity = categoryRepository.findById(postDTO.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없음"));

        PostEntity build = PostEntity.builder()
                .title(postDTO.getTitle())
                .user(userEntity)
                .category(categoryEntity)
                .context(postDTO.getContext())
                .build();
        postRepository.save(build);
    }

    /**
     * 게시글 상세조회 (로그인 / 비로그인 다 가능)
     */
    @Override
    public PostInfoDTO getPostInfo(int id, String accessToken) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 개시글이 존재하지 않음!!"));


        String nickName = postEntity.getUser().getNickName();
        String category = postEntity.getCategory().getCategory();

        LocalDateTime date = postEntity.getCreatedAt();
        if (!postEntity.getCreatedAt().equals(postEntity.getUpdatedAt())) {
            date = postEntity.getUpdatedAt();
        }
        return PostInfoDTO.builder()
                .title(postEntity.getTitle())
                .user(nickName)
                .category(category)
                .context(postEntity.getContext())
                .postView(postEntity.getPostView())
                .likeCount(postEntity.getLikeCount())
                .disLikeCount(postEntity.getDisLikeCount())
                .date(date)
                .build();
    }

    /**
     * 게시글 업데이트
     */
    @Override
    @Transactional
    public void postUpdate(int id, PostUpdateDTO postUpdateDTO, String accessToken) {
        PostEntity post = postCheck(id, tokenCheck(accessToken));

        CategoryEntity categoryEntity = categoryRepository.findById(postUpdateDTO.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 카테고리가 없음!"));

        post.postUpdate(
                postUpdateDTO.getTitle(),
                postUpdateDTO.getContext(),
                categoryEntity
        );


    }

    /**
     * 게시글 삭제 ( Soft Delete )
     */
    @Override
    @Transactional
    public void postDelete(int id, String accessToken) {
        PostEntity post = postCheck(id, tokenCheck(accessToken));
        post.postDelete();

    }

    /**
     * 작성자 검증
     */
    private UserEntity tokenCheck(String accessToken) {
        ///  토큰 유효성체크
        Jws<Claims> claims = jwtProvider.getClaims(jwtProvider.extractToken(accessToken));
        String loginId = claims.getBody().get("loginID").toString();
        return userRepository.findByEmail(loginId)
                .orElseThrow(() -> new IllegalArgumentException("DB에 등록된 회원이 아님!"));
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


}
