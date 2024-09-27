package com.example.domain.auth.service;

import com.example.domain.auth.controller.dto.request.*;
import com.example.domain.auth.controller.dto.response.FindEmailResponse;
import com.example.domain.auth.controller.dto.response.LoginResponse;

public interface AuthService {

    public void signup(SignupRequest requestDto);

    public Boolean checkDuplicateEmail(String email);

    public Boolean checkDuplicatePhone(String phone);

    public Boolean checkDuplicateNickname(String nickname);


    public FindEmailResponse findEmailByPhone(FindEmailByPhoneRequest request);

    public void findPassword(PwFindRequest request);

    /** 인가코드로 액세스 토큰 발급 */
    public String kakaoAccess(OauthMemberLoginRequest request);

    public LoginResponse login(LoginRequest request);

    public LoginResponse adminLogin(LoginRequest request);

    public String refreshAccessToken(String refreshToken, RefreshRequest request);

    public LoginResponse loginByKakao(String token);
}
