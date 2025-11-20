package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.CommentDTO;
import com.example.BoardProject_back.dto.CommentRespDTO;
import com.example.BoardProject_back.entity.CommentEntity;
import com.example.BoardProject_back.entity.PostEntity;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.repository.CommentRepository;
import com.example.BoardProject_back.repository.PostRepository;
import com.example.BoardProject_back.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * 게시글 작성
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
        commentRepository.save(commentEntity);
    }

    /**
     * 댓글 목록 조회 ( 비로그인자도 볼 수 있도록)
     * isOwner로 본인이 작성한 댓글인지 확인
     * 본인이 작성한 댓글이면 수정/삭제 버튼이 프론트에서 보임
     */
    @Override
    public List<CommentRespDTO> getComments(int postId, UserEntity userEntity) {

        List<CommentEntity> commentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
        List<CommentRespDTO> collect = commentList.stream().map(comment -> {
            boolean isOwner = false;  /// 댓글 본인 확인 true = 본인
            if (userEntity!=null && ((userEntity.getId())==(comment.getUser().getId()))) {
                isOwner = true;
            }
            return CommentRespDTO.builder()
                    .commentId(comment.getId())
                    .content(comment.getComment())
                    .authorName(comment.getUser().getNickName())
                    .createdAt(comment.getCreatedAt())
                    .isOwner(isOwner)
                    .build();
        }).collect(Collectors.toList());

        return collect;
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
    public void deleteComment(int commendId,int postId, UserEntity userEntity) {
        log.info("userid: {}",userEntity.getId());
        CommentEntity comment = commentRepository.findByIdAndIsDeletedFalse(commendId)
                .orElseThrow(() -> new IllegalArgumentException("이미 삭제된 게시글입니다"));
        comment.commentDelete();

    }

}
