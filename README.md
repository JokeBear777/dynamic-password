# Secure Password Manager

본 프로젝트는 OAuth2 인증, TOTP 기반 이중 인증(2FA), 클라이언트 중심의 비대칭키 구조, 안전한 대칭키 암호화, 감사 로그(Audit Log), 공유 Store 권한 모델 등을 통합하여 높은 수준의 보안을 제공하는 비밀번호 관리 시스템이다.  
서버는 비밀번호 평문을 절대 알 수 없으며(Zero-Knowledge), 사용자는 클라이언트 단에서만 복호화된 비밀번호를 열람할 수 있다.

---

## 1. 주요 특징

- 클라이언트에서 생성한 키 쌍 기반의 **완전한 Zero-Knowledge Password Management**
- OAuth2 인증(현재: Naver) + TOTP 기반 2FA
- 비밀번호 생성, 공개키 암호화, AES 재암호화 저장 구조
- Android Keystore / Secure Enclave 기반의 개인키 보호
- OTP·로그인 보안 강화(Rate Limit, 실패 횟수 제한, 잠금 정책)
- 비밀번호 공유 Store(권한 모델 포함)
- 비밀번호 유출 여부 조회 API(k-Anonymity 방식)
- 복구 코드 기반 계정 복구
- Key Rotation, KMS 기반 서버 비밀키 관리

---

## 2. 전체 아키텍처 개요

### 2.1 클라이언트 중심 키 생성

1. 클라이언트가 최초 등록 시 **RSA/ECC 키 쌍을 직접 생성**
2. Private Key  
   - Android Keystore 또는 iOS Secure Enclave에 영구 저장  
   - 생체 인증 또는 PIN을 통과해야만 접근 가능  
   - 서버는 절대 Private Key를 알 수 없음
3. Public Key  
   - 서버에 업로드하여 사용자 계정과 연결  
   - 서버는 항상 Public Key만 사용하여 데이터를 암호화

### 2.2 비밀번호 암호화 및 저장 흐름

1. 사용자가 특정 사이트를 등록하면 서버가 강력한 비밀번호를 생성한다.
2. 비밀번호는 서버에서 평문으로 보지 않고 즉시 클라이언트로 전달된다.
3. 클라이언트는 Public Key로 비밀번호를 암호화한 후 다시 서버로 전송한다.
4. 서버는 전달받은 암호문을 AES로 재암호화하여 DB에 저장한다.
5. AES 키는 KMS에 저장되어 있으며 정기적으로 Key Rotation이 수행된다.

### 2.3 비밀번호 복호화 및 열람 흐름

1. 사용자가 OAuth2 로그인 진행
2. 로그인 후 반드시 TOTP 2FA를 통과해야 한다
3. 클라이언트는 서버에서 AES 암호문을 전달받는다
4. 클라이언트는 기기 보안 영역에서 Private Key 접근을 위해 생체인증/PIN 요구
5. Private Key로 복호화한 비밀번호를 N분 동안 열람 가능  
   (시간 초과 시 암호문 삭제 후 재인증 필요)

서버는 어떤 시점에서도 비밀번호 평문을 볼 수 없기 때문에 완전한 Zero-Knowledge가 보장된다.

---

## 3. 인증 및 보안

### 3.1 OAuth2 기반 로그인
- 현재 Naver OAuth2 Login 구현
- 향후 Google, GitHub 등 Provider 확장 가능

### 3.2 TOTP 기반 이중 인증

- 서버는 TOTP Secret을 저장하지 않는다
- 사용자 ID + 서버 비밀키(KMS에서 관리)를 이용해 Secret을 결정론적으로 생성
- HMAC 기반 계산만으로 검증 가능하여 DB 없이 Stateless 운영
- Secret 노출 가능성을 줄이기 위해 서버 비밀키는 주기적으로 Rotation

### 3.3 로그인 및 OTP 보안 강화

- 특정 시간당 OTP 입력 실패 횟수 제한
- 로그인 요청에 Rate Limiting 적용 (IP·사용자 단위)
- 연속 실패 시 일정 시간 잠금(cooldown)
- 모든 로그인 시도, 비번 열람 시도는 Audit Log로 남김

---

## 4. 비밀번호 공유 Store 기능

비밀번호 공유를 위해 별도의 Store 기능을 제공하며 다음과 같이 구성된다.

### 4.1 권한 모델
- Owner  
- Admin  
- Member  
- Read-only  

### 4.2 기능
- Store 초대 링크 생성  
- 링크 만료 기간 설정  
- 일회성 사용 토큰  
- 초대 승인 후 권한 부여  
- 비밀번호 열람 기록 저장(누가, 언제, 어떤 이유로 열람했는지)  
- 특정 사용자 차단 기능  
- 공유된 비밀번호는 사용자 클라이언트에서 Private Key로만 복호화 가능

---

## 5. 비밀번호 유출 여부 조회 API

HIBP(Have I Been Pwned) 등의 유출 데이터베이스를 활용한다.

- 비밀번호 전체를 외부로 전송하지 않음
- 비밀번호 해시의 prefix(예: SHA-1 앞 5자)만 외부 API로 전송하는  
  **k-Anonymity 방식**을 적용
- 서버는 응답 목록에서 전체 해시와 비교하여 유출 여부 판단
- 개인정보 보호 요구를 충족하는 방식으로 설계

---

## 6. 복구 및 초기화 전략

사용자가 기기 또는 개인키를 잃어버렸을 때를 대비한 기능 제공.

- Private Key 분실 시 비밀번호 복호화 불가 → Zero-Knowledge 특성 유지
- 복구 코드 제공(초기 가입 시 1회 생성)
- 복구 코드는 단 1회 사용 가능, 사용 후 즉시 무효 처리
- 복구 후 Public Key 재등록 가능

이 방식을 통해 보안성과 편의성 사이의 균형을 유지한다.

---

## 7. 인프라 및 운영 보안

- HTTPS 및 HSTS 적용
- CSP, X-Frame-Options, XSS 보호 등 보안 헤더 적용
- 서버 비밀키 및 AES 키는 AWS KMS에서 관리
- Key Rotation 자동화
- 데이터 재암호화(마이그레이션) 절차 마련
- 모든 보안 관련 이벤트는 Audit Log에 기록하고 별도 모니터링

---

## 8. 기술 스택

- Backend: Spring Boot, Spring Security, OAuth2 Client
- Auth: Naver OAuth2, TOTP
- Crypto: RSA/ECC, AES256-GCM, Android Keystore, Secure Enclave
- Infra: AWS KMS, AWS RDS, AWS EC2
- Client: Android / iOS / Web (확장 가능)

---
