package com.example.BoardProject_back.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private String comment;

    private Integer parentId;   /// [추가] 부모 댓글 ID (Null이면 원댓글)
}
