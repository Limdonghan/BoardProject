package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.CommentDTO;
import com.example.BoardProject_back.dto.CommentRespDTO;
import com.example.BoardProject_back.entity.CommentEntity;
import com.example.BoardProject_back.entity.PointRole;
import com.example.BoardProject_back.entity.PostEntity;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.repository.CommentRepository;
import com.example.BoardProject_back.repository.PostRepository;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.service.CommentService;
import com.example.BoardProject_back.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GradeService gradeService;

    /**
     * 댓글 작성
     */
    @Override
    @Transactional
    public void CreateComment(int postId, CommentDTO commentDTO, UserEntity userEntity) {

        ///  게시글 검증
        PostEntity postEntity = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new RuntimeException("해당 개시글이 존재하지 않거나 삭제된 게시글 입니다!!"));

        CommentEntity commentEntity = CommentEntity.builder()
                .user(userEntity)
                .post(postEntity)
                .comment(commentDTO.getComment())
                .build();

        /// [추가] 부모 댓글 여부 확인
        if (commentDTO.getParentId()!=null){
            /// 대댓글인 경우
            CommentEntity parentComment = commentRepository.findById(commentDTO.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 없슴"));

            commentEntity.setParentComment(parentComment);          /// 부모 설정
            commentEntity.setDepth(parentComment.getDepth()+1);     /// 깊이 + 1
            commentEntity.setGroupId(parentComment.getGroupId());   /// 그룹 ID 물려받기
        } else {
            /// 원댓글인 경우
             commentEntity.setDepth(0);
            /// group_id는 자기 자신의 id여야 함. 하지만 아직 저장 전이라 id가 없음(0).
            /// 따라서 일단 저장을 먼저 하고 update를 쳐야 함 (Auto Increment 전략의 한계)
        }

        CommentEntity savedComment = commentRepository.save(commentEntity);

        /// [원댓글 후처리] 저장 후 생성된 ID를 GroupID로 업데이트
        if (commentDTO.getParentId() == null) {
            savedComment.setGroupId(savedComment.getId());
            /// @Transactional 때문에 메서드 종료 시 변경 감지(Dirty Checking)로 자동 update 쿼리 나감
        }
        UserEntity user = userRepository.findById(userEntity.getId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 작성한 유저를 찾을 수 없음??"));

        /// 포인트 지급
        user.userAddPoint(PointRole.WRITING_COMMENTS);

        /// 등급심사
        gradeService.gradeAssessment(user);

    }

    /**
     * 댓글 목록 조회 ( 비로그인자도 볼 수 있도록)
     * isOwner로 본인이 작성한 댓글인지 확인
     * 본인이 작성한 댓글이면 수정/삭제 버튼이 프론트에서 보임
     */
    @Override
    public List<CommentRespDTO> getComments(int postId, UserEntity userEntity) {

        List<CommentEntity> commentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);

        List<CommentRespDTO> commentRespDTOS = commentList.stream().map(comment -> {
            boolean isOwner = false;  /// 댓글 본인 확인 true = 본인
            if (userEntity != null && ((userEntity.getId()) == (comment.getUser().getId()))) {
                isOwner = true;
            }
            return CommentRespDTO.builder()
                    .commentId(comment.getId())
                    .content(comment.isDeleted() ? "삭제된 댓글 입니다." : comment.getComment())
                    .authorName(comment.getUser().getNickName())
                    .createdAt(comment.getCreatedAt())
                    .isOwner(isOwner)
                    .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : 0)
                    .depth(comment.getDepth())
                    .groupId(comment.getGroupId())
                    .children(new ArrayList<>())
                    .build();
        }).toList();

        /// 3. 계층 구조 조립 (Map 활용)
        Map<Integer, CommentRespDTO> commentRespDTOMap = commentRespDTOS.stream()
                .collect(Collectors.toMap(CommentRespDTO::getCommentId, commentRespDTO -> commentRespDTO));

        List<CommentRespDTO> result = new ArrayList<>();

        for (CommentRespDTO commentRespDTO : commentRespDTOS) {
            /// 부모가 있으면 (대댓글이면) 부모의 children 리스트에 추가
            if (commentRespDTO.getParentCommentId() != 0 && commentRespDTOMap.containsKey(commentRespDTO.getCommentId())) { /// DTO에 parentId 필드 추가 필요 혹은 엔티티 참조 로직 변경
                /// *참고: 위 스트림 map에서 parentId를 DTO에 넣어뒀다고 가정.
                /// 만약 DTO에 parentId가 없다면 로직을 조금 바꿔야 함.
                commentRespDTOMap.get(commentRespDTO.getParentCommentId()).getChildren().add(commentRespDTO);
            } else {
                /// 부모가 없으면 (최상위 댓글이면) 결과 리스트에 추가
                result.add(commentRespDTO);
            }
        }



        return result;
    }

    /**
     * 댓글 수정
     */
    @Override
    @Transactional
    public void updateComment(int commendId, CommentDTO commentDTO, UserEntity userEntity) {
        CommentEntity comment = commentRepository.findByIdAndIsDeletedFalse(commendId)
                .orElseThrow(() -> new IllegalArgumentException("삭제된 댓글입니다."));
        comment.commentUpdate(commentDTO.getComment());
    }

    /**
     * 댓글 삭제
     */
    @Override
    @Transactional
    public void deleteComment(int commendId, UserEntity userEntity) {
        CommentEntity comment = commentRepository.findByIdAndIsDeletedFalse(commendId)
                .orElseThrow(() -> new IllegalArgumentException("이미 삭제된 게시글입니다"));
        comment.commentDelete();

    }

}
