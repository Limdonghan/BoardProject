# BoardProject-back

Spring Boot를 기반으로 구축된 게시판 서비스의 백엔드 프로젝트입니다. 사용자 인증, 게시글 및 댓글 관리, 신고 기능, 그리고 Typesense를 이용한 검색 엔진 연동 등을 포함하고 있습니다.

## 🛠 Tech Stack

### Environment

* **Java**: 17
* **Spring Boot**: 3.5.7
* **Build Tool**: Gradle

### Database & Storage

* **Main DB**: MySQL (JPA/Hibernate)
* **Cache**: Redis
* **Search Engine**: Typesense
* **File Storage**: AWS S3

### Security & Auth

* **Security**: Spring Security
* **Auth**: JWT (JSON Web Token)

## 📂 Project Structure (Major Modules)

* **Account**: 회원 가입, 정보 수정, 등급(Grade) 및 포인트 관리
* **Authentication**: 로그인, 로그아웃, 토큰 재발급 (Refresh Token)
* **Post & Comment**: 게시글/댓글 CRUD, 좋아요(Reaction) 기능
* **Search**: Typesense를 이용한 게시글 검색
* **Report**: 게시글/댓글 신고, 신고 사유 및 처리 상태 관리
* **Admin**: 관리자 기능

## 🚀 Getting Started

### 1. Prerequisites

이 프로젝트를 실행하기 위해 다음 소프트웨어가 설치되어 있어야 합니다.

* JDK 17
* Docker (Typesense 실행용)
* MySQL
* Redis

### 2. Configuration (환경 변수 설정)

`src/main/resources/application-local.properties.example` 파일을 복사하여 `application-local.properties` 파일을 생성하고, 로컬 환경에 맞는 값을 입력해야 합니다.

> ⚠️ `application-local.properties` 파일은 보안 정보가 포함되므로 **Git에 커밋되지 않도록 주의**하세요 (`.gitignore`에 포함됨).

```bash
cd src/main/resources
cp application-local.properties.example application-local.properties

```

**`application-local.properties` 설정 항목:**

| Category | Key | Description |
| --- | --- | --- |
| **DB** | `DB_URL` | MySQL 접속 URL (예: jdbc:mysql://localhost:3306/mydb) |
|  | `DB_USERNAME` | DB 사용자명 |
|  | `DB_PASSWORD` | DB 비밀번호 |
| **Redis** | `REDIS_HOST` | Redis 호스트 (기본: localhost) |
|  | `REDIS_PORT` | Redis 포트 (기본: 6379) |
| **JWT** | `JWT_SECRET_KEY` | JWT 서명에 사용할 32바이트 이상 비밀키 |
| **AWS** | `AWS_ACCESS_KEY_ID` | AWS S3 접근을 위한 Access Key |
|  | `AWS_SECRET_ACCESS_KEY` | AWS S3 접근을 위한 Secret Key |
|  | `AWS_S3_BUCKET` | 업로드할 S3 버킷 이름 |
| **Typesense** | `TYPESENSE_API_KEY` | Typesense API Key (Docker 설정과 일치시켜야 함) |

### 3. Running Typesense (Search Engine)

프로젝트 루트 경로의 `src/main/resources/docker-compose.yml`을 사용하여 Typesense 컨테이너를 실행합니다.

```bash
# Docker Compose 실행
cd src/main/resources
docker-compose up -d

```

* Typesense는 `localhost:8108` 포트에서 실행됩니다.
* 기본 API Key 설정은 `xyz`로 되어 있으며, `docker-compose.yml` 또는 설정 파일에서 변경할 수 있습니다.

### 4. Build & Run

프로젝트 루트 디렉토리에서 다음 명령어로 빌드 및 실행합니다.

```bash
# 빌드 및 테스트 (Windows)
./gradlew.bat build

# 애플리케이션 실행 (Windows)
./gradlew.bat bootRun

# Mac/Linux
./gradlew build
./gradlew bootRun

```

## 📝 License

This project is licensed under the MIT License.
