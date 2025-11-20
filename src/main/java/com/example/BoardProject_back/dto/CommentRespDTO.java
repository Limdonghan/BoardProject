package com.example.BoardProject_back.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRespDTO {

    private int commentId;              /// 댓글 ID
    private String content;             /// 댓글 내용
    private String authorName;          /// 작성자 닉네임
    private LocalDateTime createdAt;    /// 댓글 작성 시각
    private boolean isOwner;            ///// 이 댓글의 주인인지 여부 (프론트에서 삭제 버튼 노출용)
}
