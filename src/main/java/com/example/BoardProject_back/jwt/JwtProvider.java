package com.example.BoardProject_back.jwt;

import com.example.BoardProject_back.entity.UserEntity;
import com.example.BoardProject_back.repository.UserRepository;
import com.example.BoardProject_back.security.CustomUserDetails;
import com.example.BoardProject_back.service.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private Key key;

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisService redisService;


    @PostConstruct
    public void init() {
        /// 문자열 secretKey를 Key 객체로 변환
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /// jwt 토큰을 구문 분석하고 Claims을 반환, 토큰 유효성 확인
    public Jws<Claims> getClaims(final String token) {
        try {
            /// 1. Jwts.parser() 초기화
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    /// 2. setSigningKey(key) - 서명 검증을 위한 키 설정 / 이미 생성된 Key객체 사용
                    .setSigningKey(key)
                    .build()
                    /// 3. parseClaimsJws(token) - 실제 토큰 파싱 및 검증 시도
                    .parseClaimsJws(token);

            ///  블랙리스트 예외처리
            if (redisService.keyBlackListCheck(token)) {
                throw new RuntimeException("블랙리스트된 토큰입니다~!~!~!");
            }
            return claimsJws;
        } catch (ExpiredJwtException e) {
            /// 4. 예외 처리: 만료된 토큰
            throw new IllegalArgumentException("만료된 토큰");
        } catch (UnsupportedJwtException e) {
            /// 4. 예외 처리: 지원되지 않는 토큰 형식
            throw new IllegalArgumentException("지원되지 않는 토큰");
        } catch (IllegalArgumentException e) {
            /// 4. 예외 처리: 기타 잘못된 토큰 (서명 불일치 등)
            throw new IllegalArgumentException("잘못된 토큰");
        }
    }

    /// accessToken 생성
    public String createAccessToken(final String loginID) {
        String accessToken = Jwts.builder()
                .claim("loginID", loginID)
                .setSubject(loginID)
                .claim("token_type", "Access")  /// claim에 명시적으로 넣어주어야 나중에 token검사때 꺼낼 수 있음
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        log.info("accessToken : {}", accessToken);
        return accessToken;
    }

    ///  refreshToken 생성
    public String createRefreshToken(final String loginID) {
        String refreshToken = Jwts.builder()
                .setSubject(loginID)
                .claim("token_type", "Refresh")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        log.info("refreshToken : {}", refreshToken);
        try {
            redisTemplate.opsForValue().set(
                    loginID,
                    refreshToken,
                    jwtProperties.getRefreshExpiration(),
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return refreshToken;
    }


    /// JWT 토큰의 남은 유효시간을 얻어오는 메서드
    public Long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        long time = new Date().getTime();
        return expiration.getTime() - time;
    }

    public Authentication getAuthentication(final String token) {
        /// 1. 토큰에서 Claims 추출 및 유효성 검증 (서명, 만료 등)
        final Jws<Claims> claims = getClaims(token); // 이전 질문에서 구현한 메서드 호출

        /// 2. 토큰 타입 확인 (Access Token인지 확인)
        if (isWrongType(claims, "Access")) {
            /// 잘못된 타입이면 예외 발생 (예: Refresh Token 사용 시)
            throw new RuntimeException("AccessToken 아님.  잘못된 타입!@");
        }

        /// 3. 사용자 정보 조회 및 Principal 생성
        final String login = claims.getBody().getSubject(); /// 토큰의 subject (여기서는 loginID) 추출
        final UserEntity userEntity = userRepository.findByEmail(login) /// DB에서 사용자 정보 조회
        /// 사용자가 없으면 예외 발생
                .orElseThrow(() -> new RuntimeException("잘못된 회원정보!@!@ (사용자가 없음)"));

        /// 4. Authentication 객체 생성 및 반환
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        return new UsernamePasswordAuthenticationToken(
                customUserDetails,                   /// principal (사용자 정보, UserDetails 객체)
                null,                                /// credentials (비밀번호, JWT 방식에서는 null)
                customUserDetails.getAuthorities()   /// authorities (권한 목록)
        );
    }


    ///  토큰 타입 검사
    public boolean isWrongType(final Jws<Claims> claims, final String tokenType) {
        Object t = claims.getBody().get("token_type");
        boolean type = (t == null) || !t.toString().equalsIgnoreCase(tokenType);
        log.info("토큰타입검사 결과(true면 에러): {}", type);
        return type;
    }

    ///  Http 요청의 authorization를 헤더로 가져와서 헤더에서 extractToken메서드로 리턴
    public String extractTokenFromRequest(HttpServletRequest request) {
        /// 1. Authorization 헤더 값 가져오기 (예시: "Bearer eyJhbGciOiJIUzI1Ni...")
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        /// 2. 실제 토큰 문자열 추출 후 리턴 (extractToken 메서드 호출)
        return extractToken(authorizationHeader);
    }

    /// 토큰이 비어있는지 확인 && Bearer로 시작하면 Bearer를 제거 후 리턴
    public String extractToken(final String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
