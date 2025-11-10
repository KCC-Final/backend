# 그루 (Groo) - Backend

> 일상을 심다, 독서 경험의 모든 것

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-7.0-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

책을 통해 사람을 연결하는 소셜 북 커뮤니티 플랫폼의 백엔드 API 서버입니다.

## 프로젝트 소개

그루(Groo)는 독서를 통한 소통과 습관 형성을 지원하는 종합 독서 플랫폼입니다.
이 레포지토리는 RESTful API를 제공하는 백엔드 서버로, 사용자 인증, 도서 관리, 독후감, 소셜 기능 등을 담당합니다.

### 핵심 기능

- **사용자 인증/인가**: JWT 기반 토큰 인증, 이메일 인증
- **도서 관리**: 도서 검색, 상세 정보, 책장 관리
- **독후감 시스템**: CRUD, 좋아요, 댓글 기능
- **소셜 기능**: 팔로우/팔로워, 피드, 실시간 알림
- **독서 챌린지**: 목표 설정, 진행 추적, 배지 시스템

## 기술 스택

### Core
- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Build Tool**: Gradle 9.0+

### Database & Cache
- **Database**: MySQL 8.0+
- **ORM**: MyBatis 3.0.5
- **Cache**: Redis 7.0
- **Connection Pool**: Lettuce

### Security & Authentication
- **Security**: Spring Security
- **Authentication**: JWT (httpOnly Cookie)
- **Password**: BCrypt

### Documentation & Monitoring
- **API Docs**: Springdoc OpenAPI (Swagger UI)
- **Monitoring**: Spring Boot Actuator

### External APIs
- **Aladin API**: 도서 검색 및 상세 정보
- **정보나루**: 도서관 통계 및 인기도서
- **국립중앙도서관**: 사서 추천도서

## 프로젝트 구조

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/kcc/groo/
│   │   │   ├── challenge/          # 독서 챌린지
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── dao/
│   │   │   │   └── data/
│   │   │   ├── comment/            # 댓글 기능
│   │   │   ├── follow/             # 팔로우 기능
│   │   │   ├── notification/       # 실시간 알림 (SSE)
│   │   │   ├── review/             # 독후감
│   │   │   ├── search/             # 통합 검색
│   │   │   ├── user/               # 사용자 관리
│   │   │   ├── config/             # 설정 클래스
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── RedisConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       ├── mapper/             # MyBatis XML 매퍼
│   │       │   ├── UserMapper.xml
│   │       │   ├── ReviewMapper.xml
│   │       │   └── ...
│   │       ├── application.properties
│   │       ├── application-local.properties
│   │       ├── application-docker.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/com/kcc/groo/      # 테스트 코드
├── database/
│   ├── init_codes.sql              # 초기 데이터
│   └── migration_*.sql             # 스키마 변경 이력
├── Dockerfile
├── docker-compose.yml
├── build.gradle
└── README.md
```

## 시작하기

### 사전 요구사항

- Java 21 이상
- Gradle 9.0 이상
- MySQL 8.0 이상
- Redis 7.0 이상 (선택)

### 환경 변수 설정

프로젝트 루트에 `.env` 파일을 생성하고 필요한 환경 변수를 설정하세요.

`.env.example` 파일을 참고하여 다음 항목들을 설정합니다:
- 데이터베이스 연결 정보
- Redis 연결 정보
- JWT 시크릿
- 이메일 SMTP 설정
- 외부 API 키

```bash
# .env.example 파일 복사
cp .env.example .env

# .env 파일을 열어 실제 값으로 수정
nano .env
```

### 로컬 환경 실행

#### 1. 데이터베이스 준비

```bash
# MySQL 실행 및 데이터베이스 생성
mysql -u root -p
CREATE DATABASE kcc CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'kcc'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON kcc.* TO 'kcc'@'localhost';
FLUSH PRIVILEGES;

# 초기 스키마 및 데이터 적용
mysql -u kcc -p kcc < database/init_codes.sql
```

#### 2. 애플리케이션 실행

```bash
# 저장소 클론
git clone https://github.com/KCC-Final/backend.git
cd backend

# 환경 변수 설정
cp .env.example .env
# .env 파일 수정

# Gradle 빌드
./gradlew build

# 로컬 프로파일로 실행
./gradlew bootRun --args='--spring.profiles.active=local'
```

애플리케이션이 실행되면:
- API 서버: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health Check: http://localhost:8080/actuator/health

### Docker 환경 실행

```bash
# Docker Compose로 전체 스택 실행 (MySQL, Redis, Backend)
docker-compose up -d

# 로그 확인
docker-compose logs -f groo-backend-dev

# 중지
docker-compose down
```

## 환경별 프로파일

| 프로파일 | 설명 | 사용 시점 |
|---------|------|----------|
| local | 로컬 개발 환경 | IDE에서 직접 실행 |
| docker | Docker 개발 환경 | docker-compose 사용 |
| prod | 운영 환경 | 배포 서버 |

```bash
# 프로파일 지정 실행
./gradlew bootRun --args='--spring.profiles.active=local'
```

## API 문서

### Swagger UI

API 문서는 Swagger UI를 통해 확인할 수 있습니다:
- 로컬: http://localhost:8080/swagger-ui.html
- 운영: https://api.groo.site/swagger-ui.html

### 주요 엔드포인트

```
# 인증
POST   /api/users/sign-up              # 회원가입
POST   /api/users/sign-in              # 로그인
POST   /api/users/sign-out             # 로그아웃
POST   /api/users/verify-email         # 이메일 인증

# 도서
GET    /api/books                      # 도서 목록 조회
GET    /api/books/{id}                 # 도서 상세 조회
GET    /api/books/search               # 도서 검색

# 독후감
GET    /api/reviews                    # 독후감 목록
POST   /api/reviews                    # 독후감 작성
PUT    /api/reviews/{id}               # 독후감 수정
DELETE /api/reviews/{id}               # 독후감 삭제
POST   /api/reviews/{id}/like          # 좋아요

# 댓글
GET    /api/comments/review/{reviewId} # 댓글 목록
POST   /api/comments                   # 댓글 작성
PUT    /api/comments/{id}              # 댓글 수정
DELETE /api/comments/{id}              # 댓글 삭제

# 팔로우
POST   /api/follow/{userId}            # 팔로우
DELETE /api/follow/{userId}            # 언팔로우
GET    /api/follow/followers           # 팔로워 목록
GET    /api/follow/followings          # 팔로잉 목록

# 알림
GET    /api/notifications/subscribe    # SSE 구독
GET    /api/notifications              # 알림 목록
PUT    /api/notifications/{id}/read    # 읽음 처리

# 챌린지
GET    /api/challenges                 # 챌린지 목록
POST   /api/challenges                 # 챌린지 생성
POST   /api/challenges/{id}/check-in   # 체크인
```

## 개발 가이드

### 코드 스타일

#### 주석 규칙

모든 컨트롤러, 서비스, DAO 메서드에는 다음 형식의 주석을 작성합니다:

```java
/**
 * 메서드 설명
 * @author uyh
 * @created YYYY-MM-DD
 * @param paramName 파라미터 설명
 * @return 반환값 설명
 */
public ResponseEntity<?> methodName(Parameter param) {
    // 구현
}
```

#### 패키지 구조

각 도메인별로 다음 구조를 따릅니다:

```
domain/
├── controller/     # REST 컨트롤러
├── service/        # 비즈니스 로직
├── dao/            # 데이터 접근 (MyBatis Repository)
└── data/
    ├── dto/        # 데이터 전송 객체
    └── model/      # 엔티티 모델
```

### MyBatis 매퍼 작성

```xml
<!-- src/main/resources/mapper/ExampleMapper.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.kcc.groo.example.dao.IExampleRepository">
    
    <select id="findById" resultType="Example">
        SELECT 
            id,
            user_id as userId,
            created_at as createdAt
        FROM example
        WHERE id = #{id}
    </select>
    
</mapper>
```

### 응답 형식

모든 API 응답은 `CommonResDTO`로 통일합니다:

```java
@RestController
@RequestMapping("/api/example")
public class ExampleController {
    
    @GetMapping("/{id}")
    public ResponseEntity<CommonResDTO<?>> getExample(@PathVariable Long id) {
        ExampleDTO data = exampleService.findById(id);
        return ResponseEntity.ok(
            new CommonResDTO<>("성공", data)
        );
    }
}
```

### 예외 처리

```java
// 커스텀 예외 사용
throw new CustomException("에러 메시지", HttpStatus.BAD_REQUEST);

// Global Exception Handler에서 처리
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResDTO<?>> handleCustomException(CustomException e) {
        return ResponseEntity
            .status(e.getStatus())
            .body(new CommonResDTO<>(e.getMessage(), null));
    }
}
```

## 데이터베이스 마이그레이션

### 마이그레이션 파일 생성

새로운 스키마 변경이 필요한 경우:

```bash
# 파일명 형식: migration_YYYYMMDD_description.sql
touch database/migration_20250115_add_bookshelf_table.sql
```

### 마이그레이션 작성 예시

```sql
-- database/migration_20250115_add_bookshelf_table.sql

-- 북셀프 테이블 생성
CREATE TABLE IF NOT EXISTS bookshelf (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_book (user_id, book_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

## 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "com.kcc.groo.user.service.UserServiceTest"

# 테스트 결과 확인
open build/reports/tests/test/index.html
```

## 빌드 및 배포

```bash
# JAR 파일 빌드
./gradlew build

# Docker 이미지 빌드
docker build -t groo-backend:latest .

# Docker 이미지 실행
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  --name groo-backend \
  groo-backend:latest
```

## 모니터링

### Health Check

```bash
# 애플리케이션 상태 확인
curl http://localhost:8080/actuator/health

# 환경 정보 확인
curl http://localhost:8080/actuator/env
```

### 로그 확인

```bash
# Docker 로그
docker logs groo-backend -f --tail 100

# 애플리케이션 로그 레벨 (application.properties)
logging.level.com.kcc.groo=DEBUG
logging.level.com.kcc.groo.mapper=DEBUG
```

## 트러블슈팅

### 자주 발생하는 문제

#### 1. 데이터베이스 연결 실패
```bash
# MySQL 서비스 확인
systemctl status mysql

# 연결 테스트
mysql -u kcc -p -h localhost kcc
```

#### 2. Redis 연결 실패
```bash
# Redis 서비스 확인
redis-cli ping

# 환경별 host 설정 확인
# local: localhost
# docker: redis (서비스명)
```

#### 3. 한글 인코딩 문제
```properties
# application.properties 확인
spring.datasource.url=jdbc:mysql://localhost:3306/kcc?characterEncoding=utf8mb4
spring.http.encoding.charset=UTF-8
spring.messages.encoding=UTF-8
```

## 팀

**팀명**: 트리버스 (Traverse)

**팀원**:
- 최윤성
- 엄윤호 (CI/CD 담당자, 형상 관리자)
- 김연수

## 관련 링크

- [Frontend Repository](https://github.com/KCC-Final/frontend)
- [API Documentation](https://api.groo.site/swagger-ui.html)

## 라이선스

이 프로젝트는 교육 목적으로 제작되었습니다.