package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.PostDTO;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.repository.CategoryRepository;
import com.example.BoardProject_back.repository.GradeRepository;
import com.example.BoardProject_back.repository.PostRepository;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;



    /* 로직이 이게 맞나?
    * 게시글을 작성을 할려한다
    * 로그인이 되어있어야한다.
    * 로그인 토큰으로 유저를 확인하고 게시글을 작성할 수 있게한다
    * 그러면 토큰 구성을 먼저해야한다?
    *
    * */
    @Override
    public void postCreation(PostDTO postDTO) {

    }
}
