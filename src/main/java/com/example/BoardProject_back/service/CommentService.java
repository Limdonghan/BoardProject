package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.CommentDTO;
import com.example.BoardProject_back.dto.CommentRespDTO;
import com.example.BoardProject_back.entity.UserEntity;

import java.util.List;

public interface CommentService {
    void CreateComment(int postId, CommentDTO commentDTO, UserEntity userEntity);
    List<CommentRespDTO> getComments(int postId, UserEntity userEntity);
    void updateComment(int commendId,CommentDTO commentDTO, UserEntity userEntity);
    void deleteComment(int commendId,UserEntity userEntity);

}
