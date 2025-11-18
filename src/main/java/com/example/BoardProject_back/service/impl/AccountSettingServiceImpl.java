package com.example.BoardProject_back.service.impl;

import com.example.BoardProject_back.dto.UserUpdatePasswordDTO;
import com.example.BoardProject_back.dto.UserUpdateProfileDTO;
import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.service.AccountSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountSettingServiceImpl implements AccountSettingService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 유저 프로필 수정
     */
    @Override
    @Transactional
    public void updateProfile(UserUpdateProfileDTO userUpdateProfileDTO, UserEntity userEntity) {
        UserEntity byNickName = userRepository.findByNickName(userEntity.getNickName());
        if (byNickName.getNickName() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userEntity.userUpdate(userUpdateProfileDTO.getUsername());
        userRepository.save(userEntity);
    }

    /**
     * 유저 비밀번호 수정
     */
    @Override
    @Transactional
    public void updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO, UserEntity userEntity) {
        UserEntity user = userRepository.findByEmail(userEntity.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));
        /// 비밀번호 일치 체크
        if (!passwordEncoder.matches(userUpdatePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다!!!");
        }
        String newEncodePassword = passwordEncoder.encode(userUpdatePasswordDTO.getNewPassword());
        user.userUpdatePassword(newEncodePassword);
        userRepository.save(user);
    }
}
