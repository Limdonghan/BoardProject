package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.UserUpdateProfileDTO;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.service.AccountSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountSettingServiceImpl implements AccountSettingService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void updateProfile(UserUpdateProfileDTO userUpdateProfileDTO) {

    }
}
