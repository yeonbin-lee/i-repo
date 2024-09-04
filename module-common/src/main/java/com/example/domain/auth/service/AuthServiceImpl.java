package com.example.domain.auth.service;

import com.example.domain.auth.controller.dto.request.LoginRequest;
import com.example.domain.auth.controller.dto.request.SignupRequest;
import com.example.domain.auth.controller.dto.response.LoginResponse;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.enums.Provider;
import com.example.domain.member.entity.enums.Role;
import com.example.domain.member.repository.LogoutRepository;
import com.example.domain.member.repository.MemberRepository;
import com.example.global.config.jwt.CustomUserDetails;
import com.example.global.config.jwt.JwtTokenProvider;
import com.example.global.config.jwt.RefreshToken;
import com.example.global.config.jwt.RefreshTokenRepository;
import com.example.global.exception.custom.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LogoutRepository logoutRepository;

    /** [일반] 이메일 회원가입 API
     * 이메일, 전화번호, 닉네임 중복 체크
     * 중복 없을 시 member 저장
     * 프로필 저장 Version
     * */
    @Transactional
    public void signup(SignupRequest request) {

        // CHECK EMAIL, PHONE, NICKNAME DUPLICATE
        if(memberRepository.existsByEmail(request.getEmail())){
            throw new DataIntegrityViolationException("중복되는 이메일입니다.");
        }

        if(memberRepository.existsByPhone(request.getPhone())){
            throw new DataIntegrityViolationException("중복되는 전화번호입니다.");
        }

        if(memberRepository.existsByNickname(request.getNickname())){
            throw new DataIntegrityViolationException("중복되는 닉네임입니다.");
        }

        // SAVE MEMBER ENTITY
        Member member = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .birthday(request.getBirthday())
                .role(Role.ROLE_USER)
                .provider(Provider.NORMAL)
                .build();

        memberRepository.save(member);
    }


    /** [일반] 로그인 API */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // CHECK EMAIL AND PASSWORD
        Member member = this.memberRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UserNotFoundException(request.getEmail() + "은 존재하지 않는 이메일 정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // GENERATE ACCESS_TOKEN AND REFRESH_TOKEN
        String accessToken = this.jwtTokenProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(member), member.getPassword()));
        String refreshToken = this.jwtTokenProvider.generateRefreshToken(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(member), member.getPassword()));


        // REFRESH_TOKEN SAVE IN REDIS
        refreshTokenRepository.save(RefreshToken.builder()
                .id(request.getEmail())
                .refresh_token(refreshToken)
                .expiration(14) // 2주
                .build());

        LoginResponse loginResponse = LoginResponse.builder()
                .member(member)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return loginResponse;
    }

}
