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
@RequestMapping("/module-common/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * [일반] 이메일 회원가입 API
     * @param request - role "ROLE_USER"으로 고정
     * @param request - provider 사용자가 가입한 채널, enum(NORMAL, KAKAO)
     * @param request - email, nickname, password, phone 사용자가 입력
     * @param request - gender 사용자가 입력한 성별, enum(MALE, FEMALE, NO_INFO)
     * @param request - birthday 사용자가 입력한 생년월일, YYYY-mm-dd 형식 사용
     * @return response - provider, email
     * */
    @PostMapping("/signup")
    public ResponseEntity<?> singUp(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully!");
    }

    /**
     * [일반] 이메일 중복체크 API
     * @param email
     * @return response - boolean (true - 이메일이 이미 존재할 경우, false - 사용할 수 있는 이메일)
     * */
    @PostMapping("/email/duplicate")
    public ResponseEntity<?> checkEmailDuplicate(@RequestParam String email){
        Boolean checkEmailDuplicate = authService.checkDuplicateEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(checkEmailDuplicate);
    }

    /**
     * [일반] 전화번호 중복체크 API
     * @param phone
     * @return response - boolean
     * */
    @PostMapping("/phone/duplicate")
    public ResponseEntity<?> checkPhoneDuplicate(@RequestParam String phone){
        Boolean checkPhoneDuplicate = authService.checkDuplicatePhone(phone);
        return ResponseEntity.status(HttpStatus.OK).body(checkPhoneDuplicate);
    }

    /**
     * [일반] 닉네임 중복체크 API
     * @param nickname
     * @return response - boolean
     * */
    @PostMapping("/nickname/duplicate")
    public ResponseEntity<?> checkNicknameDuplicate(@RequestParam String nickname){
        Boolean checkNicknameDuplicate = authService.checkDuplicateEmail(nickname);
        return ResponseEntity.status(HttpStatus.OK).body(checkNicknameDuplicate);
    }



    /**
     * [일반] 비밀번호 재설정 API by phone - 비밀번호를 잊어버렸을 경우
     * @param request - phone 사용자의 전화번호
     * @param request - password 비밀번호로 설정할 새로운 비밀번호
     * */
    @PutMapping("/phone/change/password")
    public ResponseEntity<?> findPassword(@RequestBody @Valid PwFindRequest request){
        authService.findPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully!");
    }

    /**
     * [일반] 로그인 API
     * @param request - 사용자가 입력한 email, password
     * @return response - accessToken, refreshToken, Member 객체
     * */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authService.adminLogin(request);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
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


    /**
     * 이메일 찾기 By 전화번호 API
     * @param request phone - 사용자 전화번호
     * @return response - provider, email
     */
    @GetMapping("/find/email")
    public ResponseEntity<?> findEmailByPhone(@RequestBody @Valid FindEmailByPhoneRequest request){
        FindEmailResponse response = authService.findEmailByPhone(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * Access 토큰갱신 API
     * */
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken, @RequestBody RefreshRequest request) {
        String newAccessToken = authService.refreshAccessToken(refreshToken, request);
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }


}
