package com.example.BoardProject_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPostListDTO {
    private int totalPostCount;             /// 작성자가 쓴 전체 게시글 수
    private List<MyPostDTO> myPostList;    /// 작성자 게시글 리스트
}
