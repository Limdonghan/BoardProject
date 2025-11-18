package com.example.BoardProject_back.service;

import com.example.BoardProject_back.dto.UserUpdatePassword;
import com.example.BoardProject_back.dto.UserUpdateProfileDTO;

public interface AccountSettingService {

    void updateProfile(UserUpdateProfileDTO userUpdateProfileDTO);

    void updatePassword(UserUpdatePassword userUpdatePassword);

}
