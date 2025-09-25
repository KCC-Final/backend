package com.kcc.groo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

/**
 * Swagger(SpringDoc OpenAPI) 설정 클래스
 *
 * 주요 기능:
 * 1. API 문서 기본 정보 설정 (제목, 설명, 버전 등)
 * 2. JWT 토큰 기반 인증 스키마 설정 (Swagger UI에서 토큰 인증 테스트 가능)
 * 3. 환경별 서버 주소 고정 → Swagger UI에서 드롭다운으로 선택 가능
 * 4. 보안 요구사항 설정 (🔒 표시된 API는 JWT 토큰 필요)
 *
 * 환경별 접속 URL:
 * - 로컬:  http://localhost:8080/swagger-ui.html
 * - Docker: http://localhost/swagger-ui.html
 * - 운영:  https://groo.site/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 메인 설정 Bean
     *
     * 주요 설정:
     * - API 문서 기본 정보
     * - JWT 인증 스키마 등록
     * - JWT 보안 요구사항 추가
     * - 환경별 서버 주소 목록 (드롭다운으로 선택 가능)
     */
    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "JWT";

        // JWT 보안 스키마 등록 (Authorize 버튼 활성화)
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, createJWTSecurityScheme());

        // 환경별 서버 주소 목록 (드롭다운)
        List<Server> servers = List.of(
                new Server().url("http://localhost:8080").description("로컬 개발 서버"),
                new Server().url("http://localhost").description("Docker 환경"),
                new Server().url("https://groo.site").description("운영 서버")
        );

        return new OpenAPI()
                .info(createApiInfo())                             // API 기본 정보
                .servers(servers)                                  // 환경별 서버 드롭다운
                .addSecurityItem(new SecurityRequirement().addList(jwtSchemeName)) // JWT 인증 요구사항
                .components(components);                           // JWT 인증 스키마
    }

    /**
     * JWT 보안 스키마 생성
     * Swagger UI의 Authorize 버튼을 눌렀을 때 입력하는 토큰 스키마를 정의합니다.
     */
    private SecurityScheme createJWTSecurityScheme() {
        return new SecurityScheme()
                .name("JWT")
                .type(SecurityScheme.Type.HTTP)   // HTTP 인증 방식
                .scheme("bearer")                 // Bearer 토큰
                .bearerFormat("JWT")              // JWT 포맷
                .description("JWT 토큰을 입력해주세요. 'Bearer ' 접두사는 자동으로 추가됩니다.");
    }

    /**
     * API 문서 기본 정보 생성
     * Swagger UI 상단에 노출되는 프로젝트 정보/주의사항을 설정합니다.
     */
    private Info createApiInfo() {
        return new Info()
                .title("Groo API Documentation")  // 문서 제목
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
                        - 환경별 서버 주소를 드롭다운에서 선택 후 테스트하세요
                        """)
                .version("v1.0.0");
    }
}
