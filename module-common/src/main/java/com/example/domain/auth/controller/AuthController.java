package com.example.domain.auth.controller;

import com.example.domain.auth.controller.dto.request.*;
import com.example.domain.auth.controller.dto.response.LoginResponse;
import com.example.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    /** [일반] 이메일 회원가입 API */
    @PostMapping("/signup")
    public ResponseEntity<?> singUp(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully!");
    }

    /** 이메일 중복체크 */
    // Q. 어떤 provider를 사용하는지 알려아하는가? ex) normal, kakao
    @PostMapping("/email/duplicate")
    public ResponseEntity<?> emailDuplicate(@RequestBody @Valid CheckEmailDuplicateRequest requestDto){
        Boolean checkEmailDuplicate = authService.checkDuplicateEmail(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(checkEmailDuplicate);
    }

    /** 전화번호로 이메일 찾기 */
    @PostMapping("/find/email")
    public ResponseEntity<?> findEmailByPhone(@RequestBody @Valid FindEmailByPhoneRequest request){
        String email = authService.findEmailByPhone(request);
        return ResponseEntity.status(HttpStatus.OK).body(email);
    }

    /** 1. 비밀번호 재설정
     *  - 비밀번호를 잊어버렸을 경우
     * */
    @PutMapping("/find/password")
    public ResponseEntity<?> findPassword(@RequestBody @Valid PwFindRequest pwFindRequest){
        authService.findPassword(pwFindRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully!");
    }

    /** [일반] 로그인 API */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    /** Access 토큰갱신 API */
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
    public ResponseEntity<?> logout(@RequestHeader("ACCESS_TOKEN") String accessToken, @RequestBody LogoutRequest request) {
        authService.logout(accessToken, request);
        return ResponseEntity.status(HttpStatus.OK).body("User logout!");
    }


}
