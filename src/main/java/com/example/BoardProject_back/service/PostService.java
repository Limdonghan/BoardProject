package com.example.BoardProject_back.service;


import com.example.BoardProject_back.dto.PostDTO;
import com.example.BoardProject_back.dto.PostInfoDTO;
import com.example.BoardProject_back.dto.PostUpdateDTO;

public interface PostService {

void postCreation(PostDTO postDTO, String accessToken);

PostInfoDTO getPostInfo(int id, String accessToken);

void postUpdate(int id, PostUpdateDTO postUpdateDTO, String accessToken);

void postDelete(int id, String accessToken);




}
