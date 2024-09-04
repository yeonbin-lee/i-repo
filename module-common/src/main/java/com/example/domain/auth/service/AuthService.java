package com.example.domain.auth.service;

import com.example.domain.auth.controller.dto.request.LoginRequest;
import com.example.domain.auth.controller.dto.request.SignupRequest;
import com.example.domain.auth.controller.dto.response.LoginResponse;

public interface AuthService {

    public void signup(SignupRequest requestDto);

    public LoginResponse login(LoginRequest requestDto);
}
