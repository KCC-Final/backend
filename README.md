# backend
# 그루(Groo) - 일상을 심다, 독서 경험의 모든 것

> 책을 통해 사람을 연결하는 소셜 북 커뮤니티 플랫폼

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen?style=flat-square&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.0.5-red?style=flat-square)](https://mybatis.org/mybatis-3/)

## 📖 프로젝트 소개

그루(Groo)는 독서를 통한 소통과 습관 형성을 지원하는 종합 독서 플랫폼입니다. 
사용자가 책을 찾고, 읽고, 후기를 공유하며 건전한 독서 습관을 형성할 수 있도록 돕습니다.

### 🎯 핵심 가치

- **독서 동기 부여**: 독서 챌린지와 오늘의 문장으로 지속적인 독서 동기 제공
- **정보 접근성**: 가까운 도서관의 소장/대출 정보 확인 기능
- **경험 공유**: 독후감을 통한 독서 경험 나눔과 공감대 형성

## 🎨 주요 기능

### 📚 도서 관리
- **도서 탐색**: 추천/베스트셀러/독후감/필독도서 조회
- **검색 기능**: 키워드/저자/ISBN 기반 도서 검색
- **개인화 추천**: 콘텐츠 기반 추천과 개인화 재랭킹 시스템

### 🏛️ 도서관 연동
- **소장 정보**: 가까운 도서관의 도서 소장 현황 확인
- **실시간 대출**: 일부 기관의 실시간 대출 정보 제공

### ✍️ 독서 기록 & 커뮤니티
- **독후감 작성**: 마크다운 및 사진 지원
- **소셜 기능**: 팔로우, 피드, 댓글, 좋아요 시스템
- **정렬 옵션**: 최신순/공감순/팔로우 우선 정렬

### 🎯 독서 챌린지
- **목표 설정**: 개인별 독서 목표 설정 및 관리
- **진행 추적**: 체크인 기능과 달성률 대시보드
- **성취 시스템**: 배지 및 통계 제공

## 💡 제안 배경

### 문제 인식
한국의 독서 인구는 2013년 62.4%에서 2023년 48.5%로 지속적으로 감소하고 있습니다. 동시에 성인 문해력도 10년간 20점 하락하여 OECD 평균에 미달하는 상황입니다.

### 해결 방안
- 독서에 대한 접근성 향상 (도서관 연동)
- 독서 경험의 통합 관리 (한 플랫폼에서 모든 독서 활동)
- 지속적인 동기 부여 (챌린지, 커뮤니티, 추천 시스템)

## 🛠️ 기술 스택

### Backend
- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Database**: MySQL 8.0+
- **ORM**: MyBatis 3.0.5
- **Migration**: Flyway
- **Documentation**: Springdoc OpenAPI

### Frontend (연동 예정)
- **Framework**: Next.js 15.5.2 (App Router)
- **Language**: TypeScript
- **Styling**: CSS Modules / Vanilla CSS
- **Icons**: Lucide React
- **State Management**: React Hooks + Context API

### Infrastructure (예정)
- **Deployment**: AWS EC2
- **Database**: AWS RDS MySQL
- **Cache**: Redis
- **External APIs**: 국립중앙도서관, 알라딘 도서

## 🚀 시작하기

### 전제 조건
- Java 21 이상
- gradle/groovy 9.0 이상
- MySQL 8.0 이상

## 📁 프로젝트 구조

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/kcc/groo/
│   │   │   ├── controller/          # REST 컨트롤러
│   │   │   ├── service/             # 비즈니스 로직
│   │   │   ├── dto/                 # 데이터 전송 객체
│   │   │   ├── mapper/              # MyBatis 매퍼 인터페이스
│   │   │   └── DemoApplication.java # 메인 클래스
│   │   └── resources/
│   │       ├── mappers/             # MyBatis XML 매퍼
│   │       ├── db/migration/        # Flyway 마이그레이션
│   │       └── application.properties      # 애플리케이션 설정
│   └── test/                        # 테스트 코드
├── .github/
│   └── workflows/
│       └── deploy.yml               # GitHub Actions CI/CD
├── Dockerfile                       # Docker 설정
├── README.md                        # 프로젝트 문서
├── CONTRIBUTING.md                  # 기여 가이드
└── build.gradle                     # gradle 설정
```

## 📊 기대 효과

### 개인적 측면
- **독서율 향상**: 지속 가능한 독서 습관 형성
- **동기 부여**: 맞춤형 추천과 챌린지를 통한 독서 의욕 증진
- **접근성 향상**: 도서관 정보를 통한 독서 비용 절감

### 사회적 측면
- **독서 문화 조성**: 건전한 독서 문화 확산
- **지식 공유**: 독서 경험과 지식의 활발한 공유
- **생태계 선순환**: 도서 유통의 선순환 구조 형성

## 👥 팀 정보

**팀명**: 트리버스(Traverse)

**팀원**:
- 최윤성 (팀장)
- 엄윤호
- 김연수

**작성일**: 2025년 9월 1일

**"일상을 심다, 독서 경험의 모든 것"** - 그루(Groo)와 함께 더 나은 독서 습관을 만들어보세요! 📚✨
