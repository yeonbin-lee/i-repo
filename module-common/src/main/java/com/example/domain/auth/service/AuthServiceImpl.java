package com.example.domain.auth.service;

import com.example.domain.auth.controller.dto.request.*;
import com.example.domain.auth.controller.dto.response.LoginResponse;
import com.example.domain.coolsms.entity.Sms;
import com.example.domain.coolsms.repository.SmsRepository;
import com.example.domain.coolsms.service.SmsService;
import com.example.domain.member.entity.Logout;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.enums.Provider;
import com.example.domain.member.entity.enums.Role;
import com.example.domain.member.repository.LogoutRepository;
import com.example.domain.member.repository.MemberRepository;
import com.example.domain.member.service.MemberService;
import com.example.global.config.jwt.CustomUserDetails;
import com.example.global.config.jwt.JwtTokenProvider;
import com.example.global.config.jwt.RefreshToken;
import com.example.global.config.jwt.RefreshTokenRepository;
import com.example.global.exception.custom.NotEqualsCodeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


//    @Value("${spring.jwt.accessTokenExpirationTime}")
//    private final Integer accessTokenExpirationTime;

    private final SmsService smsService;
    private final MemberService memberService;
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
        if(memberService.existByEmail(request.getEmail())){
            throw new DataIntegrityViolationException("중복되는 이메일입니다.");
        }

        if(memberService.existByPhone(request.getPhone())){
            throw new DataIntegrityViolationException("중복되는 전화번호입니다.");
        }

        if(memberService.existByNickname(request.getNickname())){
            throw new DataIntegrityViolationException("중복되는 닉네임입니다.");
        }

        Sms sms = smsService.findSmsByPhone(request.getPhone());

        if (request.getCode().equals(sms.getCode())) {
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

            // SAVE MEMBER ENTITY
            memberService.saveMember(member);
        } else {
            throw new NotEqualsCodeException("잘못된 요청입니다.");
        }
    }

    /** 이메일 중복체크 */
    public boolean checkDuplicateEmail(String email){
        return memberService.existByEmail(email);
    }

    /**
     * 전화번호로 이메일 찾기
     * 1) CoolSms를 통해 User에게 인증코드 전송
     * 2) 받은 인증코드를 Redis 내에 (key: 전화번호, value:인증코드) 형태로 저장
     * 3) Redis 내에 인증코드가 존재한다면 email 반환
     * @return
     */
    public String findEmailByPhone(FindEmailByPhoneRequest request) {

        String phone = request.getPhone();

        // key(phone)으로 Redis에서 Sms값 가져오기
        Sms sms = smsService.findSmsByPhone(phone);

        if (request.getCode().equals(sms.getCode())) {
            Member member = memberService.findByPhone(phone);
            smsService.deletePhone(phone);
            return member.getEmail();
        }
        // 인증코드가 일치하지않는 경우
        throw new NotEqualsCodeException();
    }

    private Sms verifyRedisByPhone(String phone){
        // Redis 내에 Key(전화번호)가 존재하는지 확인
        Sms sms = smsService.findSmsByPhone(phone);
        smsService.deletePhone(phone);
        return sms;
    }


    /** 1. 비밀번호 재설정
     *  - 비밀번호를 잊어버렸을 경우
     *  1) coolSms를 통해 전화번호로 인증코드 전송
     *  2) 인증코드를 Redis에 저장한 후, (key: 전화번호, value:인증코드) 형태로 저장
     *  3) Redis 내에 인증코드가 존재한다면 password 재설정
     * */
    public void findPassword(PwFindRequest pwFindRequest) {

        String phone = pwFindRequest.getPhone();
        String newPassword = pwFindRequest.getPassword();
        String code = pwFindRequest.getCode();

        Sms sms = verifyRedisByPhone(phone);

        if (code.equals(sms.getCode())) {
            Member member = memberService.findByPhone(phone);

            String encryptPassword = passwordEncoder.encode(newPassword);
            member.updatePassword(encryptPassword);
            memberService.saveMember(member);
        } else {
            throw new NotEqualsCodeException("잘못된 요청입니다.");
        }
    }


    /** [일반] 로그인 API */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // CHECK EMAIL AND PASSWORD

        Member member = memberService.findByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // GENERATE ACCESS_TOKEN AND REFRESH_TOKEN
        String accessToken = jwtTokenProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(member), member.getPassword()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(
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

    /** Token 갱신 */
    @Transactional
    public String refreshAccessToken(String refreshToken, RefreshRequest request) {

        String email = request.getEmail();
        // CHECK IF REFRESH_TOKEN EXPIRATION AVAILABLE, UPDATE ACCESS_TOKEN AND RETURN
        Member member = memberService.findByEmail(email);
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String newAccessToken = jwtTokenProvider.generateAccessToken(
                    new UsernamePasswordAuthenticationToken(new CustomUserDetails(member), member.getPassword()));
            return newAccessToken;
        }
        return null;
    }

    public void logout(String accessToken, LogoutRequest request) {
        String email = request.getEmail();
        // Redis 내의 기존 refreshToken 삭제
        if (!refreshTokenRepository.existsById(email)){
            // 리프레시 토큰 없다고 예외처리 날려야됨
        }
        refreshTokenRepository.deleteById(email);
        // access_token의 남은 유효시간 가져오기 (Seconds 단위)
        Date expirationFromToken = jwtTokenProvider.getExpirationFromToken(accessToken);
        Date today = new Date();
        Integer sec = (int) ((expirationFromToken.getTime() - today.getTime()) / 1000);

        // access_token을 Redis의 key 값으로 등록
        logoutRepository.save(
                Logout.builder()
                        .id(accessToken)
                        .data("logout")
                        .expiration(sec)
                        .build()
        );
    }
}
