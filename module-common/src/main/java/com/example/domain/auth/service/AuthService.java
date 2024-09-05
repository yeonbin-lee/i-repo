package com.example.domain.auth.service;

import com.example.domain.auth.controller.dto.request.*;
import com.example.domain.auth.controller.dto.response.LoginResponse;

public interface AuthService {

    public void signup(SignupRequest requestDto);

    public boolean checkDuplicateEmail(String email);

    public String findEmailByPhone(FindEmailByPhoneRequest request);

    public void findPassword(PwFindRequest pwFindRequest);

    public LoginResponse login(LoginRequest requestDto);

    public String refreshAccessToken(String refreshToken, RefreshRequest request);

    public void logout(String accessToken, LogoutRequest request);
}
