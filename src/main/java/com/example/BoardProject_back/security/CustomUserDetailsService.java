package com.example.BoardProject_back.security;

import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 필터 검증 -> 토큰 발급
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("-------------CustomUserDetailsService-------------------");
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() ->  new UsernameNotFoundException("가입된 회원을 찾을 수 없음!?!?!"));

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        log.info("username : {}",username);
        return customUserDetails;
    }
}
