package com.example.BoardProject_back.service;


import com.example.BoardProject_back.dto.PostDTO;
import com.example.BoardProject_back.dto.PostInfoDTO;
import com.example.BoardProject_back.dto.PostUpdateDTO;

public interface PostService {

void postCreation(PostDTO postDTO);

PostInfoDTO getPostInfo(int id);

void postUpdate(int id, PostUpdateDTO postUpdateDTO);

void postDelete(int id);




}
