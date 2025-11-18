package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.UserUpdatePassword;
import com.example.BoardProject_back.dto.UserUpdateProfileDTO;
import com.example.BoardProject_back.entity.UserEntity;

public interface AccountSettingService {

    void updateProfile(UserUpdateProfileDTO userUpdateProfileDTO, UserEntity userEntity);

    void updatePassword(UserUpdatePassword userUpdatePassword, UserEntity userEntity);

}
