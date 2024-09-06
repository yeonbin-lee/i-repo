package com.example.domain.auth.service;

import com.example.domain.auth.controller.dto.request.*;
import com.example.domain.auth.controller.dto.response.LoginResponse;
import com.example.domain.auth.service.helper.KakaoClient;
import com.example.domain.coolsms.entity.Sms;
import com.example.domain.coolsms.service.SmsService;
import com.example.domain.member.controller.vo.KakaoInfo;
import com.example.domain.member.controller.vo.KakaoToken;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.enums.Gender;
import com.example.domain.member.entity.enums.Provider;
import com.example.domain.member.entity.enums.Role;
import com.example.domain.member.service.LogoutService;
import com.example.domain.member.service.MemberService;
import com.example.global.config.jwt.CustomUserDetails;
import com.example.global.config.jwt.JwtTokenProvider;
import com.example.global.config.jwt.RefreshToken;
import com.example.global.config.jwt.RefreshTokenRepository;
import com.example.global.exception.custom.NotEqualsCodeException;
import com.example.global.exception.custom.SocialLoginException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    private final SmsService smsService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final KakaoClient kakaoClient;
    private final LogoutService logoutService;


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

    // Redis 내에 Key(전화번호)가 존재하는지 확인
    private Sms verifyRedisByPhone(String phone){
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
        refreshTokenService.saveRefreshToken(
                RefreshToken.builder()
                        .id(request.getEmail())
                        .refresh_token(refreshToken)
                        .expiration(14) // 2주
                        .build()
        );

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
        if (!refreshTokenService.existsById(email)){
            // 리프레시 토큰 없다고 예외처리 날려야됨
        }
        refreshTokenService.deleteById(email);
        // access_token의 남은 유효시간 가져오기 (Seconds 단위)
        Date expirationFromToken = jwtTokenProvider.getExpirationFromToken(accessToken);
        Date today = new Date();
        Integer sec = (int) ((expirationFromToken.getTime() - today.getTime()) / 1000);

        // accessToken을 Redis의 key 값으로 등록
        logoutService.logoutUser(accessToken.substring(7), sec); // "Bearer "삭제
    }


    /**
     * [카카오] 발급받은 인가코드로 카카오 액세스 토큰 발급
     * */
    public String kakaoAccess(OauthMemberLoginRequest request) {
        String access_Token = "";
        String refresh_Token = "";
        String code = request.getToken();
        String strUrl = "https://kauth.kakao.com/oauth/token"; // 토큰 요청 보낼 주소
        KakaoToken kakaoToken = new KakaoToken(); // 요청받을 객체

        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // url Http 연결 생성

            // POST 요청
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);// outputStreamm으로 post 데이터를 넘김

            // 파라미터 세팅
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            // 0번 파라미터 grant_type 입니다 authorization_code로 고정
            sb.append("grant_type=authorization_code");

            // 1번 파라미터 client_id입니다.
            sb.append("&client_id=" + KAKAO_CLIENT_ID);

            // 2번 파라미터 redirect_uri입니다.
            sb.append("&redirect_uri=" + KAKAO_REDIRECT_URI);

            // 3번 파라미터 code
            sb.append("&code=" + code);

            sb.append("&client_secret=" + KAKAO_CLIENT_SECRET);

            bw.write(sb.toString());
            bw.flush();// 실제 요청을 보내는 부분

            // 실제 요청을 보내는 부분, 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responsecode(200이면성공): {}", responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            log.info("response body: {}", result);

            // Jackson으로 json 파싱할 것임
            ObjectMapper mapper = new ObjectMapper();

            // kakaoToken에 result를 KakaoToken.class 형식으로 변환하여 저장
            kakaoToken = mapper.readValue(result, KakaoToken.class);
            System.out.println(kakaoToken);

            // api호출용 access token
            access_Token = kakaoToken.getAccess_token();

            // access 토큰 만료되면 refresh token사용(유효기간 더 김)
            refresh_Token = kakaoToken.getRefresh_token();

            log.info(access_Token);
            log.info(refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            throw new SocialLoginException("[카카오] 액세스 토큰을 발급받는 데 실패했습니다.");
        }
        return access_Token;
    }

    /**
     * [카카오] 로그인
     */
    public LoginResponse loginByKakao(String token) {
        System.out.println("[카카오] 받은 액세스 토큰="+ token);
        Member member = signInByProvider(token);

        Optional<Member> findMember = memberService.findOpMemberByEmail(member.getEmail());
        if (findMember.isEmpty()) { // 최초 로그인 시 회원가입
            memberService.saveMember(member);
        }
        return createAndSaveToken(member);
    }


    private Member signInByProvider(String token) {

        // 카카오 로그인
        KakaoInfo memberInfo = kakaoClient.getUserInfo("Bearer " + token);
        String email = memberInfo.getKakaoAccount().getEmail();
        String nickname = memberInfo.getKakaoAccount().getProfile().getNickname();
        String gender = memberInfo.getKakaoAccount().getGender();
        // 생년
        String birthyear = memberInfo.getKakaoAccount().getBirthyear();
        // 월일
        String birthday = memberInfo.getKakaoAccount().getBirthday();

        // LocalDate 타입 변환 형식 지정 (yyyy-mm-dd)
        String birth_str = birthyear + "-" + birthday.substring(0,2) + "-" + birthday.substring(2,4);
        LocalDate birth = LocalDate.parse(birth_str, DateTimeFormatter.ISO_DATE);

        String password = socialRandomPassword(email);
        Member member = new Member(nickname, email, password, "", Gender.valueOf(gender.toUpperCase()), birth, Role.ROLE_USER, Provider.KAKAO);
        return member;
    }

    private LoginResponse createAndSaveToken(Member member) {
        String accessToken = jwtTokenProvider.generateAccessToken(new UsernamePasswordAuthenticationToken(new CustomUserDetails(member), member.getPassword()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(new UsernamePasswordAuthenticationToken(new CustomUserDetails(member), member.getPassword()));
        refreshTokenService.saveRefreshToken(
                RefreshToken.builder()
                        .id(member.getEmail())
                        .refresh_token(refreshToken)
                        .expiration(14)
                        .build());

        LoginResponse loginResponse = LoginResponse.builder()
                .member(member)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return loginResponse;
    }

    private String socialRandomPassword(String email) {
        String systemMil = String.valueOf(System.currentTimeMillis());
        return passwordEncoder.encode(email + systemMil);
    }
}
