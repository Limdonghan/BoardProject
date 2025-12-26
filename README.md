# BoardProject-back

Spring Boot를 기반으로 구축된 게시판 서비스의 백엔드 프로젝트입니다. 사용자 인증, 게시글 및 댓글 관리, 신고 기능, 그리고 Typesense를 이용한 검색 엔진 연동 등을 포함하고 있습니다.

## 🛠 Tech Stack

### Environment

- **Java**: 17
- **Spring Boot**: 3.5.7
- **Build Tool**: Gradle

### Database & Storage

- **Main DB**: MySQL (JPA/Hibernate)
- **Cache**: Redis
- **Search Engine**: Typesense
- **File Storage**: AWS S3

### Security & Auth

- **Security**: Spring Security
- **Auth**: JWT (JSON Web Token)

## 📂 Project Structure (Major Modules)

- **Account**: 회원 가입, 정보 수정, 등급(Grade) 및 포인트 관리
- **Authentication**: 로그인, 로그아웃, 토큰 재발급 (Refresh Token)
- **Post & Comment**: 게시글/댓글 CRUD, 좋아요(Reaction) 기능
- **Search**: Typesense를 이용한 게시글 검색
- **Report**: 게시글/댓글 신고, 신고 사유 및 처리 상태 관리
- **Admin**: 관리자 기능
- **File**: AWS S3를 이용한 파일 업로드/삭제

## 🔗 Frontend Integration

이 백엔드는 React + Vite 기반 프론트엔드와 연동됩니다.

### 프론트엔드 기술 스택

- **React**: 19.2.0
- **Vite**: 7.2.4
- **React Router DOM**: 7.10.1
- **Axios**: 1.13.2

### API 연동 방식

#### 개발 환경

- 프론트엔드는 Vite 개발 서버(`http://localhost:5173`)에서 실행됩니다
- Vite 프록시를 통해 `/api` 경로의 요청이 백엔드(`http://localhost:8080`)로 전달됩니다
- CORS 문제를 피하기 위해 프록시를 사용합니다

#### 운영 환경

- 프론트엔드는 `VITE_API_URL` 환경변수로 백엔드 서버 주소를 직접 지정합니다
- 브라우저에서 백엔드 API로 직접 요청을 보냅니다
- 백엔드에서 CORS 설정이 필요합니다

### 인증 방식

- **Access Token**: `localStorage.accessToken`에 저장
- **Refresh Token**: `localStorage.refreshToken`에 저장
- 모든 API 요청 헤더에 `Authorization: Bearer <accessToken>` 자동 포함
- 401 에러 발생 시 `/api/user/refresh` 엔드포인트로 토큰 자동 갱신 후 원래 요청 재시도
- 403 에러는 권한 문제로 처리되어 토큰 갱신을 시도하지 않습니다

### 주요 API 엔드포인트

프론트엔드에서 사용하는 주요 API 엔드포인트:

**인증 (Authentication)**

- `POST /api/auth/login` - 로그인
- `POST /api/auth/logOut` - 로그아웃
- `POST /api/user/createAccount` - 회원가입
- `GET /api/user/me` - 현재 사용자 정보 조회
- `POST /api/user/refresh` - 토큰 재발급
- `PATCH /api/user/profile` - 프로필 수정 (닉네임)
- `PATCH /api/user/password` - 비밀번호 변경
- `GET /api/user/check-nickname` - 닉네임 중복 확인

**게시글 (Post)**

- `GET /api/post/lists` - 게시글 목록 조회 (페이징)
- `GET /api/post/{categoryId}/lists` - 카테고리별 게시글 목록
- `GET /api/post/{id}` - 게시글 상세 조회
- `POST /api/post` - 게시글 작성
- `PATCH /api/post/{id}` - 게시글 수정
- `DELETE /api/post/{id}` - 게시글 삭제
- `POST /api/post/{id}/reactions` - 게시글 반응 (좋아요/싫어요)
- `GET /api/post/my` - 내 게시글 목록
- `GET /api/post/search` - 게시글 검색

**댓글 (Comment)**

- `GET /api/post/{postId}/comment` - 댓글 목록 조회
- `POST /api/post/{postId}/comment` - 댓글 작성
- `PATCH /api/post/{postId}/comment/{commentId}` - 댓글 수정
- `DELETE /api/post/{postId}/comment/{commentId}` - 댓글 삭제

**신고 (Report)**

- `POST /api/report/posts/{postId}` - 게시글 신고
- `POST /api/report/comments/{commentId}` - 댓글 신고
- `POST /api/report/{reportId}` - 신고 상태 변경

**관리자 (Admin)**

- `GET /api/admin` - 신고 목록 조회 (페이징)
- `GET /api/admin/{statusId}` - 상태별 신고 목록
- `GET /api/admin/post/{statusId}` - 게시글 신고 목록 (상태별)
- `GET /api/admin/comment/{statusId}` - 댓글 신고 목록 (상태별)
- `GET /api/admin/detail/{reportId}` - 신고 상세 조회
- `DELETE /api/admin/{id}` - 관리자 삭제 (게시글/댓글)
- `GET /api/admin/sync-typesense` - Typesense 데이터 동기화

**파일 (File)**

- `POST /api/file/upload` - 이미지 업로드 (multipart/form-data)
- `DELETE /api/file/delete` - 이미지 삭제

## 🚀 Getting Started

### 1. Prerequisites

이 프로젝트를 실행하기 위해 다음 소프트웨어가 설치되어 있어야 합니다.

- JDK 17
- Docker (Typesense 실행용)
- MySQL
- Redis

### 2. Configuration (환경 변수 설정)

`src/main/resources/application-local.properties.example` 파일을 복사하여 `application-local.properties` 파일을 생성하고, 로컬 환경에 맞는 값을 입력해야 합니다.

> ⚠️ `application-local.properties` 파일은 보안 정보가 포함되므로 **Git에 커밋되지 않도록 주의**하세요 (`.gitignore`에 포함됨).

```bash
cd src/main/resources
cp application-local.properties.example application-local.properties

```

**`application-local.properties` 설정 항목:**

| Category      | Key                     | Description                                           |
| ------------- | ----------------------- | ----------------------------------------------------- |
| **DB**        | `DB_URL`                | MySQL 접속 URL (예: jdbc:mysql://localhost:3306/mydb) |
|               | `DB_USERNAME`           | DB 사용자명                                           |
|               | `DB_PASSWORD`           | DB 비밀번호                                           |
| **Redis**     | `REDIS_HOST`            | Redis 호스트 (기본: localhost)                        |
|               | `REDIS_PORT`            | Redis 포트 (기본: 6379)                               |
| **JWT**       | `JWT_SECRET_KEY`        | JWT 서명에 사용할 32바이트 이상 비밀키                |
| **AWS**       | `AWS_ACCESS_KEY_ID`     | AWS S3 접근을 위한 Access Key                         |
|               | `AWS_SECRET_ACCESS_KEY` | AWS S3 접근을 위한 Secret Key                         |
|               | `AWS_S3_BUCKET`         | 업로드할 S3 버킷 이름                                 |
| **Typesense** | `TYPESENSE_API_KEY`     | Typesense API Key (Docker 설정과 일치시켜야 함)       |

### 3. Running Typesense (Search Engine)

프로젝트 루트 경로의 `src/main/resources/docker-compose.yml`을 사용하여 Typesense 컨테이너를 실행합니다.

```bash
# Docker Compose 실행
cd src/main/resources
docker-compose up -d

```

- Typesense는 `localhost:8108` 포트에서 실행됩니다.
- 기본 API Key 설정은 `xyz`로 되어 있으며, `docker-compose.yml` 또는 설정 파일에서 변경할 수 있습니다.

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

## 📚 API References

애플리케이션 실행 후 다음 URL에서 Swagger UI를 통해 API 문서를 확인할 수 있습니다:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html

## 🔍 개발 팁

### 프론트엔드와 함께 개발 시

1. 백엔드를 먼저 실행합니다 (`./gradlew.bat bootRun` 또는 `./gradlew bootRun`)
2. 백엔드가 `http://localhost:8080`에서 정상 실행되는지 확인합니다
3. 프론트엔드 프로젝트에서 `npm run dev`로 개발 서버를 실행합니다
4. 프론트엔드의 Vite 프록시 설정이 백엔드 주소와 일치하는지 확인합니다 (기본값: `http://localhost:8080`)

### CORS 설정

개발 환경에서는 프론트엔드가 Vite 프록시를 사용하므로 CORS 설정이 필요 없습니다.  
운영 환경에서는 프론트엔드 도메인을 허용하도록 CORS 설정이 필요합니다.

### 로그 확인

- 애플리케이션 로그는 콘솔에 출력됩니다
- API 요청/응답 로그를 통해 프론트엔드 연동 문제를 디버깅할 수 있습니다
- Swagger UI를 통해 API를 직접 테스트할 수 있습니다
