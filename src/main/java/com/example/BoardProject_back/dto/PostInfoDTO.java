package com.example.BoardProject_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostInfoDTO {

    @NotNull
    private String user; /// 게시글 작성자

    @NotNull
    private String title;

    @NotNull
    private String category;

    @NotNull
    private String context;

    @NotNull
    private int postView;

    @NotNull
    private int likeCount;

    @NotNull
    private int disLikeCount;

    @NotNull
    private LocalDateTime date;
}
