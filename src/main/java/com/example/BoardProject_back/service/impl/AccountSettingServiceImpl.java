package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.UserUpdateProfileDTO;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.security.CustomUserPrincipal;
import com.example.BoardProject_back.service.AccountSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountSettingServiceImpl implements AccountSettingService {
    private final UserRepository userRepository;
    private final CustomUserPrincipal customUserPrincipal;

    @Override
    @Transactional
    public void updateProfile(UserUpdateProfileDTO userUpdateProfileDTO) {
        UserEntity userEntity = customUserPrincipal.customUserPrincipal();
        UserEntity byNickName = userRepository.findByNickName(userEntity.getNickName());
        if (byNickName.getNickName() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userEntity.userUpdate(userUpdateProfileDTO.getUsername());
        userRepository.save(userEntity);
    }
}
