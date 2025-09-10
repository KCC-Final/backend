# =================================================================
# Stage 1: Gradle을 사용한 빌드 스테이지
# =================================================================
# ⚠️ 변경: 베이스 이미지를 gradle과 jdk21이 포함된 이미지로 변경
FROM gradle:8.7.0-jdk21 AS build
WORKDIR /app

# ⚠️ 변경: Gradle 설정 파일을 먼저 복사하여 의존성 레이어를 캐싱합니다.
# gradlew 파일들에 실행 권한을 부여합니다.
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN chmod +x ./gradlew

# ⚠️ 변경: Gradle을 사용하여 의존성을 다운로드합니다.
RUN ./gradlew dependencies

# 소스 코드를 복사합니다.
COPY src ./src

# ⚠️ 변경: Gradle Wrapper를 사용하여 테스트를 제외하고 프로젝트를 빌드합니다.
RUN ./gradlew build -x test

# =================================================================
# Stage 2: 실행 스테이지
# =================================================================
FROM openjdk:21-slim
WORKDIR /app

# ⚠️ 변경: 빌드된 JAR 파일의 경로를 Gradle의 기본 출력 경로(build/libs)로 수정
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]