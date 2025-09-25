package com.kcc.groo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Swagger(SpringDoc OpenAPI) 설정 클래스
 * 
 * 주요 기능:
 * 1. API 문서 정보 설정 (제목, 설명, 버전 등)
 * 2. JWT 토큰 기반 인증 스키마 설정
 * 3. 환경별 서버 정보 동적 설정 (local/docker/prod)
 * 4. 보안 요구사항 설정
 * 
 * 환경별 접속 URL:
 * - 로컬: http://localhost:8080/swagger-ui.html
 * - 운영: https://groo.site/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    // application.properties에서 환경별 서버 URL 주입
    // 기본값: http://localhost:8080 (로컬 환경)
    @Value("${swagger.server.url:http://localhost:8080}")
    private String serverUrl;

    // application.properties에서 환경별 서버 설명 주입  
    // 기본값: 로컬 서버
    @Value("${swagger.server.description:로컬 서버}")
    private String serverDescription;

    /**
     * OpenAPI 메인 설정 Bean
     * SpringDoc에서 사용하는 핵심 설정 객체를 생성합니다.
     * 
     * 주요 설정:
     * - API 문서 기본 정보 (제목, 설명, 버전)
     * - 환경별 서버 정보 (로컬/운영 자동 전환)
     * - JWT 인증 스키마 컴포넌트
     * 
     * @return 완전히 설정된 OpenAPI 객체
     */
    @Bean
    public OpenAPI openAPI() {
        // JWT 보안 스키마 식별자
        String jwtSchemeName = "JWT";
        
        // 보안 요구사항 정의 (현재는 전역 적용하지 않음)
        // 개별 컨트롤러에서 @SecurityRequirement 어노테이션으로 적용
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);
        
        // 보안 스키마를 컴포넌트로 등록
        // Swagger UI에서 "Authorize" 버튼 활성화
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, createJWTSecurityScheme());

        return new OpenAPI()
                .info(createApiInfo())                    // API 기본 정보
                .servers(List.of(new Server()             // 환경별 서버 정보
                    .url(serverUrl)                       // properties에서 주입된 URL
                    .description(serverDescription)))     // properties에서 주입된 설명
                .components(components);                  // JWT 인증 컴포넌트
    }

    /**
     * JWT 보안 스키마 생성
     * 
     * Swagger UI 우측 상단의 "Authorize" 버튼 클릭 시 나타나는 
     * JWT 토큰 입력 다이얼로그를 설정합니다.
     * 
     * 설정 내용:
     * - HTTP Bearer 토큰 방식
     * - JWT 포맷 명시
     * - 사용자 안내 메시지
     * 
     * @return JWT 인증용 SecurityScheme 객체
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
     * 
     * Swagger UI 상단에 표시되는 API 문서의 메타데이터를 설정합니다.
     * 프로젝트 정보, 사용법, 주의사항 등을 포함합니다.
     * 
     * @return API 문서 정보가 담긴 Info 객체
     */
    private Info createApiInfo() {
        return new Info()
                .title("Groo API Documentation")         // API 문서 제목
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
                        - 환경별로 올바른 서버가 자동 선택됩니다
                        """)
                .version("v1.0.0");                       // API 버전
    }
    /*
     * ============================================================================
     * 환경별 설정 파일 매핑 정보:
     * 
     * application-local.properties:
     * swagger.server.url=http://localhost:8080
     * swagger.server.description=로컬 개발 서버
     * 
     * application-docker.properties:  
     * swagger.server.url=http://localhost
     * swagger.server.description=Docker 개발 서버
     * 
     * application-prod.properties:
     * swagger.server.url=https://groo.site
     * swagger.server.description=운영 서버
     * ============================================================================
     */

}