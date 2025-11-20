package com.example.BoardProject_back.config;

import com.example.BoardProject_back.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean  //
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.
                authorizeHttpRequests(authorizeRequest ->
                                authorizeRequest
                                        .requestMatchers(HttpMethod.POST,"/api/user/createAccount","/api/user/refresh").permitAll()
                                        .requestMatchers("/api/auth/**").permitAll()
                                        .requestMatchers(HttpMethod.GET,"/api/post/{postId}/comment").permitAll()
                                        .requestMatchers(HttpMethod.GET,"/api/post/{id}").permitAll()
                                        .anyRequest().authenticated()
                )  /// 그 외의 요청은 인증된 사용자만 접근
                .csrf(csrf -> csrf.disable())  /// JWT 사용 시 CSRF 보호 비활성화
                .formLogin(formLogin -> formLogin.disable())  /// 기본 로그인 폼 비활성화 (JWT 사용)
                .httpBasic(httpBasic->httpBasic.disable())  /// HTTP Basic 인증 비활성화

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
     * 회원가입 시 비밀번호를 안전하게 암호화하여 저장
     **/
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
