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
 * 2. JWT 토큰 기반 인증 스키마 설정 (Swagger UI에서 토큰 인증 테스트 가능)
 * 3. 서버 URL 고정 제거 → Swagger UI 접속 도메인을 자동 사용
 * 4. 보안 요구사항 설정 (🔒 표시된 API는 JWT 토큰 필요)
 *
 * 환경별 접속 URL:
 * - 로컬:  http://localhost:8080/swagger-ui.html
 * - Docker: http://localhost/swagger-ui.html
 * - 운영:  https://groo.site/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "JWT";

        // JWT 보안 스키마 등록 (Authorize 버튼 활성화)
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, createJWTSecurityScheme());

        return new OpenAPI()
                .info(createApiInfo()) // API 기본 정보
                // .servers(...) 제거 → 현재 접속한 Origin 자동 사용
                .addSecurityItem(new SecurityRequirement().addList(jwtSchemeName)) // JWT 인증 요구사항
                .components(components); // JWT 인증 스키마
    }

    /**
     * JWT 보안 스키마 생성
     */
    private SecurityScheme createJWTSecurityScheme() {
        return new SecurityScheme()
                .name("Authorization") // 헤더 이름 고정
                .type(SecurityScheme.Type.APIKEY) // HTTP bearer → APIKEY 로 변경
                .in(SecurityScheme.In.HEADER)     // 헤더에 직접 삽입
                .description("JWT 토큰을 입력하세요. (Bearer 접두사 없이 순수 토큰만 입력)");
    }



    /**
     * API 문서 기본 정보 생성
     */
    private Info createApiInfo() {
        return new Info()
                .title("Groo API Documentation")
                .description("""
                        📚 독서 SNS 플랫폼 Groo의 REST API 문서입니다.

                        ## 주요 기능
                        - 📖 도서 검색 및 상세 정보 조회
                        - 👥 사용자 인증 및 프로필 관리
                        - 💬 독서 커뮤니티 기능

                        ## 인증 방법
                        1. `/api/auth/login` 엔드포인트로 로그인
                        2. 응답에서 JWT 토큰을 복사
                        3. Swagger UI 우측 상단 Authorize(🔒) 버튼 클릭
                        4. 토큰 입력 후 Authorize 클릭 → 이후 🔒 API 호출 가능

                        ## 주의사항
                        - 인증이 필요한 API는 🔒 아이콘으로 표시됩니다
                        - Swagger UI는 접속한 도메인(로컬/운영)에 맞춰 자동으로 API를 호출합니다
                        """)
                .version("v1.0.0");
    }
}
