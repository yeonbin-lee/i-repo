package com.example.domain.auth.controller;

import com.example.domain.auth.controller.dto.request.LoginRequest;
import com.example.domain.auth.controller.dto.request.SignupRequest;
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
        System.out.println("들어옴");
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully!");
    }

    /** [일반] 로그인 API */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

}
