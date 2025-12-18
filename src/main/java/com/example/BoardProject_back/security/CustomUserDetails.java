package com.example.BoardProject_back.security;

import com.example.BoardProject_back.entity.UserEntity;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@ToString
@Slf4j
public class CustomUserDetails implements UserDetails {
    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String user_role = userEntity.getUserRole().toString();
        if (!user_role.startsWith("ROLE_")) {
            user_role = "ROLE_" + user_role;
        }
        log.info("user_role : {}", user_role);
        return List.of(new SimpleGrantedAuthority(user_role));
    }

    @Override
    public String getPassword() {  ///  계정의 패스워드 리턴
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {  ///  계정의 로그인ID 리턴
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
