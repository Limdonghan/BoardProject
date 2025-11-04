package com.example.BoardProject_back.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {  //권한 지정 테이블
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
    private String role;
}
