package com.kcc.groo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Swagger(SpringDoc OpenAPI) 설정 클래스
 *
 * 주요 기능:
 * 1. API 문서 기본 정보 설정 (제목, 설명, 버전 등)
 * 2. JWT 토큰 기반 인증 스키마 설정
 * 3. 환경별 서버 정보 자동 매칭 (별도 서버 URL 지정 X → 현재 도메인 사용)
 * 4. 보안 요구사항 설정
 *
 * 환경별 접속 URL:
 * - 로컬: http://localhost:8080/swagger-ui.html
 * - 운영: https://groo.site/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 메인 설정 Bean
     * SpringDoc에서 사용하는 핵심 설정 객체를 생성합니다.
     *
     * 주요 설정:
     * - API 문서 기본 정보 (제목, 설명, 버전)
     * - JWT 인증 스키마 컴포넌트
     * - 서버 URL은 swagger-ui를 연 현재 Origin 자동 사용
     *
     * @return 완전히 설정된 OpenAPI 객체
     */
    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "JWT";

        // 보안 스키마 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, createJWTSecurityScheme());

        return new OpenAPI()
                .info(createApiInfo())     // API 기본 정보
                // .servers(...) 제거 → swagger-ui가 현재 도메인 자동 사용
                .addSecurityItem(new SecurityRequirement().addList(jwtSchemeName))
                .components(components);   // JWT 인증 컴포넌트
    }

    /**
     * JWT 보안 스키마 생성
     */
    private SecurityScheme createJWTSecurityScheme() {
        return new SecurityScheme()
                .name("JWT")                              // 스키마 이름
                .type(SecurityScheme.Type.HTTP)           // HTTP 인증 타입
                .scheme("bearer")                         // Bearer 토큰 방식
                .bearerFormat("JWT")                      // JWT 포맷 명시
                .description("JWT 토큰을 입력해주세요. 'Bearer ' 접두사는 자동으로 추가됩니다.");
    }

    /**
     * API 문서 기본 정보 생성
     */
    private Info createApiInfo() {
        return new Info()
                .title("Groo API Documentation")  // API 문서 제목
                .description("""
                        📚 독서 SNS 플랫폼 Groo의 REST API 문서입니다.
                        
                        ## 주요 기능
                        - 📖 도서 검색 및 상세 정보 조회
                        - 👥 사용자 인증 및 프로필 관리  
                        - 💬 독서 커뮤니티 기능
                        
                        ## 인증 방법
                        1. `/api/auth/login` 엔드포인트로 로그인
                        2. 응답으로 받은 JWT 토큰을 복사
                        3. 우측 상단 🔒 Authorize 버튼 클릭
                        4. 토큰 입력 후 Authorize 클릭
                        
                        ## 주의사항
                        - 인증이 필요한 API는 🔒 아이콘으로 표시됩니다
                        - 현재 접속한 Origin(로컬/운영)에 맞춰 자동 호출됩니다
                        """)
                .version("v1.0.0");  // API 버전
    }
}
