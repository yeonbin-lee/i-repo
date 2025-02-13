# I-repo 서비스
>사용자의 눈과 관련된 간단한 챗봇, 사용자의 이미지 데이터를 이용한 데이터 분석 결과, 비대면 진료 서비스를 제공하는 헬스케어 프로젝트입니다.
>
>**진행 기간**은 [2024.08. ~ (진행중)] 입니다.

### :blush: 목차
1. 프로그램 주요 기능
    *  **[유저]**
    * 회원가입
    * 로그인
    * 프로필 관리 (생성 / 업데이트 / 삭제 / 조회)
    * 시스템 / 마케팅 수신 동의
     

    *  **[어드민]**
    * 일반 / 탈퇴 회원 조회 / 복구
    * 푸시 메시지 전송
    * 약관 생성 / 변경
    * 공지사항
  
    
2. 사용한 기술 스택 및 라이브러리


## [프로젝트 주요 기능]
### 1. 회원가입 
- [x] 이메일 회원가입
- [x] 카카오톡 회원가입 → 카카오씽크 사업자 등록 문제로 로그인 이후 단계는 진행하지 않는다.
- [x] 아메일, 닉네임, 휴대전화는 중복 불가하다.
- [x] CoolSms를 이용하여 회원가입 시 인증코드를 전송한다. (redis에 5분간 저장)

### 2. 로그인
- [x] 회원가입 시 사용한 이메일(일반/ 카카오)과 비밀번호로 로그인 할 수 있다.
- [x] 회원가입과 로그인을 제외한 다른 기능은 로그인 후 사용 가능하다.
- [x] 전화번호로 이메일을 찾을 수 있다.
- [x] 전화번호로 비밀번호를 변경할 수 있다. 

### 3. 프로필 관리
- [x] 첫 회원가입시 메인 프로필은 자동 등록된다.
- [x] 프로필은 최대 10개까지 등록할 수 있다.
- [x] 프로필의 닉네임은 중복 불가하다.  

### 4. 시스템 / 마케팅 수신 동의
- [x] 마케팅 수신 동의 여부는 DB에 남아야한다.
- [x] 수신 동의 여부 변경 시 캐시를 무효화한다.

### 5. 일반 / 탈퇴 회원 조회 / 복구
- [x] 어드민은 회원 / 탈퇴 회원의 특정 조건으로 조회할 수 있다.
- [x] 어드민은 탈퇴 회원을 회원으로 북구시킬 수 있다.

### 6. 푸시 메시지 전송
- [x] 어드민은 마케팅 수신 동의 유저에게 푸시 알림을 전송할 수 있다.

### 7. 약관 변경 / 생성
- [x] 어드민이 약관을 변경하거나 생성할 시 유저의 이메일으로 변경 / 생성 사항을 전송한다.
- [x] 어드민은 공지사항을 작성할 수 있다.

## [사용한 기술 스택 및 라이브러리]
<div align=center>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> 
  <img src="https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <br>
  <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> 
  <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"/>
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black"/>
  <img src="https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black"/>
  <br>
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>
  

</div>
