package com.kcc.groo.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cys
 * @modified 2025-10-11
 * JWT 인증 필터 (화이트리스트 방식 적용)
 * - SecurityFilterChain보다 먼저 실행되어, 요청 헤더의 JWT 토큰을 검사
 * - 화이트리스트에 등록된 경로는 토큰 검사 없이 통과
 * - 그 외의 모든 경로는 토큰이 유효해야만 통과
 */
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * JWT 검증을 건너뛸 화이트리스트 경로
     */
    private static final String[] PERMIT_URL_ARRAY = {
            /* 정적 리소스 및 테스트 */
            "/api/test/ping",
            "/actuator",

            /* Swagger UI */
            "/swagger-ui",
            "/v3/api-docs",
            "/api-docs",

            /* 로그인, 회원가입, 토큰 재발급 */
            "/api/v1/auth/login",
            "/api/v1/users/signup",
            "/api/v1/token-refresh",

            /* ID/PW 찾기, 이메일 인증 */
            "/api/v1/users/id",
            "/api/v1/users/password",
            "/api/v1/email"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // 화이트리스트 경로 확인
        for (String permitUrl : PERMIT_URL_ARRAY) {
            if (path.startsWith(permitUrl)) {
                filterChain.doFilter(request, response);
                return; // 화이트리스트 경로인 경우 필터의 나머지 로직을 실행하지 않고 통과
            }
        }

        // 화이트리스트가 아닌 경로에 대하여 토큰 검증 수행
        try {
            String token = jwtTokenProvider.resolveAccessToken(request);

            // 토큰이 없는 경우 401 반환
            if (token == null) {
                log.warn("Access token not found in request");
                sendUnauthorizedResponse(response, "엑세스 토큰이 필요합니다");
                return;
            }

            // 토큰이 유효하지 않은 경우 401 반환 (만료, 위조 등)
            if (!jwtTokenProvider.validateToken(token)) {
                log.warn("Invalid or expired access token");
                sendUnauthorizedResponse(response, "엑세스 토큰이 유효하지 않습니다");
                return;
            }

            // 엑세스 토큰이 유효한 경우 인증 정보 설정
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.error("JWT authentication failed: {}", e.getMessage());
            sendUnauthorizedResponse(response, "잘못된 엑세스 토큰입니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * @author uyh
     * @created 2025-10-11
     * 401 Unauthorized 응답 전송
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(String.format("{\"error\":\"Unauthorized\",\"message\":\"%s\"}", message));
    }
}