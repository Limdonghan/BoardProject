package com.example.BoardProject_back.security;

import com.example.BoardProject_back.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CustomUserPrincipal {

    public  UserEntity customUserPrincipal() {
        log.info("CustomUserPrincipal() 권한조회 시작 ");
        Object userPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("userPrincipal : {}", userPrincipal);
        CustomUserDetails customUserDetails = (CustomUserDetails) userPrincipal;
        log.info("customUserDetails : {}", customUserDetails);
        return customUserDetails.getUserEntity();

    }

}
