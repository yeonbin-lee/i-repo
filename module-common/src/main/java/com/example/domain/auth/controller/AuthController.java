package com.example.domain.auth.controller;

import com.example.domain.auth.controller.dto.request.*;
import com.example.domain.auth.controller.dto.response.FindEmailResponse;
import com.example.domain.auth.controller.dto.response.LoginResponse;
import com.example.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    /**
     * [일반] 이메일 회원가입 API
     * */
    @PostMapping("/signup")
    public ResponseEntity<?> singUp(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully!");
    }

    /**
     * 이메일 중복체크 API
     * */
    // Q. 어떤 provider를 사용하는지 알려아하는가? ex) normal, kakao
    @PostMapping("/email/duplicate")
    public ResponseEntity<?> emailDuplicate(@RequestBody @Valid CheckEmailDuplicateRequest requestDto){
        Boolean checkEmailDuplicate = authService.checkDuplicateEmail(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(checkEmailDuplicate);
    }

    /**
     * 이메일 찾기 By 전화번호 API
     * */
    @PostMapping("/find/email")
    public ResponseEntity<?> findEmailByPhone(@RequestBody @Valid FindEmailByPhoneRequest request){
        FindEmailResponse response = authService.findEmailByPhone(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 1. 비밀번호 재설정 API
     *  - 비밀번호를 잊어버렸을 경우
     * */
    @PutMapping("/find/password")
    public ResponseEntity<?> findPassword(@RequestBody @Valid PwFindRequest pwFindRequest){
        authService.findPassword(pwFindRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully!");
    }

    /**
     * [일반] 로그인 API
     * */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    /**
     * Access 토큰갱신 API
     * */
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken, @RequestBody RefreshRequest request) {
        String newAccessToken = authService.refreshAccessToken(refreshToken, request);
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }

    /**
     * 로그아웃
     * 1. Redis내의 refresh_token 삭제
     * 2. Redis에 현재 access_token을 logout 상태로 등록
     * - 2.1. 해당 access_token의 남은 유효시간을 Redis의 TTL로 등록
     * 3. JwtTokenFilter 파일의 doFIlterInternal 메소드에서 redis에 logout 상태인지 검증하는 로직 추가
     * */
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken, @RequestBody LogoutRequest request) {
        authService.logout(accessToken, request);
        return ResponseEntity.status(HttpStatus.OK).body("User logout!");
    }

    /** 실제 서비스 구현할 때는 인가코드를 받는 컨트롤러는 삭제 */
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    /** [카카오] 인가코드 요청 API */
    @GetMapping("/kakao/authorize")
    public String kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+KAKAO_CLIENT_ID);
        url.append("&redirect_uri="+KAKAO_REDIRECT_URI);
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    /**
     * [카카오] 발급받은 인가코드로 카카오 액세스 토큰 발급
     * */
    @PostMapping("/kakao/access")
    public ResponseEntity<?> kakaoAccess(@RequestBody OauthMemberLoginRequest request){
        String access_token = authService.kakaoAccess(request);
        return ResponseEntity.status(HttpStatus.OK).body(access_token);
    }


    /**
     * [카카오] 로그인
     */
    @PostMapping("/kakao/login")
    public ResponseEntity<?> loginByKakao(@RequestBody OauthMemberLoginRequest request) {
        LoginResponse response = authService.loginByKakao(request.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
