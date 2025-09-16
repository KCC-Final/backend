package com.kcc.groo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger(SpringDoc OpenAPI) 설정 클래스
 * 
 * 주요 기능:
 * 1. API 문서 정보 설정 (제목, 설명, 버전 등)
 * 2. JWT 토큰 기반 인증 스키마 설정
 * 3. 서버 정보 설정 (개발/운영 환경)
 * 4. 보안 요구사항 설정
 * 
 * 접속 URL: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 설정 Bean
     * SpringDoc에서 사용하는 메인 설정 객체를 생성합니다.
     * 
     * @return OpenAPI 설정이 완료된 객체
     */
    @Bean
    public OpenAPI openAPI() {
        // JWT 보안 스키마 이름 정의
        String jwtSchemeName = "JWT";
        
        // 보안 요구사항 생성 - 이 설정으로 모든 API에 JWT 인증이 적용됩니다
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);
        
        // 보안 스키마 컴포넌트 설정
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, createJWTSecurityScheme());

        return new OpenAPI()
                .info(createApiInfo())                    // API 기본 정보 설정
                .servers(createServers())                 // 서버 정보 설정
                // .addSecurityItem(securityRequirement) // 전역 보안 비활성화 - 개별 API에서 설정
                .components(components);                  // 보안 컴포넌트 설정
    }

    /**
     * JWT 보안 스키마 생성
     * Swagger UI에서 "Authorize" 버튼을 클릭했을 때 나타나는 인증 방식을 정의합니다.
     * 
     * @return JWT 토큰을 위한 SecurityScheme 객체
     */
    private SecurityScheme createJWTSecurityScheme() {
        return new SecurityScheme()
                .name("JWT")                              // 스키마 이름
                .type(SecurityScheme.Type.HTTP)           // HTTP 타입 (Bearer 토큰 사용)
                .scheme("bearer")                         // Bearer 방식
                .bearerFormat("JWT")                      // JWT 포맷임을 명시
                .description("JWT 토큰을 입력해주세요. 'Bearer ' 접두사는 자동으로 추가됩니다.");
    }

    /**
     * API 기본 정보 생성
     * Swagger UI 상단에 표시되는 API 문서의 기본 정보를 설정합니다.
     * 
     * @return API 정보가 설정된 Info 객체
     */
    private Info createApiInfo() {
        return new Info()
                .title("Groo API Documentation")                    // API 제목
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
                        """)
                .version("v1.0.0")                                   // API 버전
                .contact(createContact())                            // 연락처 정보
                .license(createLicense());                           // 라이센스 정보
    }

    /**
     * 연락처 정보 생성
     * API 문서에 표시될 개발팀 연락처 정보를 설정합니다.
     * 
     * @return 연락처 정보가 설정된 Contact 객체
     */
    private Contact createContact() {
        return new Contact()
                .name("Groo Development Team")                       // 팀명
                .email("dev@groo.com")                              // 이메일
                .url("https://github.com/your-organization/groo");  // GitHub 저장소
    }

    /**
     * 라이센스 정보 생성
     * API 문서에 표시될 라이센스 정보를 설정합니다.
     * 
     * @return 라이센스 정보가 설정된 License 객체
     */
    private License createLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    /**
     * 서버 정보 생성
     * Swagger UI에서 API 요청을 보낼 서버 목록을 설정합니다.
     * 환경별로 다른 서버를 선택할 수 있습니다.
     * 
     * @return 서버 정보 리스트
     */
    private List<Server> createServers() {
        // 로컬 개발 서버
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("로컬 개발 서버");

        // EC2 운영 서버 (탄력적 IP)
        Server ec2Server = new Server()
                .url("http://13.209.109.116:8080")  // 새로운 탄력적 IP
                .description("EC2 운영 서버");

        return List.of(localServer, ec2Server);
    }
}