package com.example.BoardProject_back.service;


import com.example.BoardProject_back.dto.*;
import com.example.BoardProject_back.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

void postCreation(PostDTO postDTO, UserEntity userEntity);

PostInfoDTO getPostInfo(int id);

void postUpdate(int id, PostUpdateDTO postUpdateDTO, UserEntity userEntity);

void postDelete(int id, UserEntity userEntity);

void handleReaction (int id, UserEntity userEntity, PostReactionDTO postReactionDTO);

MyPostListDTO getMyPostList(UserEntity userEntity);

Page<PostListPageDTO> getBoardList(Pageable pageable);

Page<PostListPageDTO> getCategoryBoardList(Pageable pageable, int categoryId);



}
