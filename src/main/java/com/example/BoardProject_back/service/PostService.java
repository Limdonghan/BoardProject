package com.example.BoardProject_back.service;


import com.example.BoardProject_back.dto.PostDTO;
import com.example.BoardProject_back.dto.PostInfoDTO;
import com.example.BoardProject_back.dto.PostUpdateDTO;
import com.example.BoardProject_back.entity.UserEntity;

public interface PostService {

void postCreation(PostDTO postDTO, UserEntity userEntity);

PostInfoDTO getPostInfo(int id);

void postUpdate(int id, PostUpdateDTO postUpdateDTO, UserEntity userEntity);

void postDelete(int id, UserEntity userEntity);




}
