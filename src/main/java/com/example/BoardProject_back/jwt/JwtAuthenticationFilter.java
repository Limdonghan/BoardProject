package com.example.BoardProject_back.jwt;

import com.example.BoardProject_back.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtProvider.extractTokenFromRequest(request);
        log.info("token : {}", token);
        if (token != null) {
            if (redisService.keyBlackListCheck(token)){
                throw new RuntimeException("블랙리스트에 등록된 토큰입니다.");
            }
            SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthentication(token));
        }
        filterChain.doFilter(request, response);
    }

}
