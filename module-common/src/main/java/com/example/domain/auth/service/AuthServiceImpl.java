package com.example.domain.auth.service;

import com.example.domain.auth.controller.dto.request.*;
import com.example.domain.auth.controller.dto.response.FindEmailResponse;
import com.example.domain.auth.controller.dto.response.LoginResponse;
import com.example.domain.auth.controller.vo.TokenResponse;
import com.example.domain.auth.service.helper.KakaoClient;
import com.example.domain.auth.util.UUIDUtil;
import com.example.domain.coolsms.entity.Sms;
import com.example.domain.coolsms.service.SmsService;
import com.example.domain.member.controller.vo.KakaoInfo;
import com.example.domain.member.controller.vo.KakaoToken;
import com.example.domain.member.entity.Member;
import com.example.domain.member.entity.MemberTermsAgreement;
import com.example.domain.member.entity.TermsCondition;
import com.example.domain.member.entity.enums.Gender;
import com.example.domain.member.entity.enums.Provider;
import com.example.domain.member.entity.enums.Role;
import com.example.domain.member.service.consentService.ConsentService;
import com.example.domain.member.service.fcm.FcmService;
import com.example.domain.member.service.logoutService.LogoutService;
import com.example.domain.member.service.memberService.MemberService;
import com.example.domain.member.service.profileService.ProfileService;
import com.example.domain.member.service.terms.MemberTermsAgreementService;
import com.example.domain.member.service.terms.TermsConditionService;
import com.example.global.config.jwt.CustomUserDetails;
import com.example.global.config.jwt.JwtTokenProvider;
import com.example.global.config.jwt.RefreshToken;
import com.example.global.exception.custom.SocialLoginException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final KakaoClient kakaoClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final FcmService fcmService;

    private final TermsConditionService termsConditionService;
    private final MemberTermsAgreementService memberTermsAgreementService;
    private final ConsentService consentService;

    /** 이메일 중복체크 */
    public Boolean checkDuplicateEmail(String email){
        return memberService.existByEmail(email);
    }

    /** 전화번호 중복체크 */
    public Boolean checkDuplicatePhone(String phone) {
        return memberService.existByPhone(phone);
    }

    /** 닉네임 중복체크 */
    public Boolean checkDuplicateNickname(String nickname) {
        return memberService.existByNickname(nickname);
    }



    /** [일반] 이메일 회원가입 API
     * 이메일, 전화번호, 닉네임 중복 체크
     * 중복 없을 시 member 저장
     * 프로필 저장 Version
     * */
    @Transactional
    public void signup(SignupRequest request) {

        // 인증되지않은 전화번호
        if(!isTokenPhone(request.getPhone())){
            throw new DataIntegrityViolationException("잘못된 요청입니다.");
        }

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
        // 필수 약관 ID 목록
        List<Long> requiredTermsIds = List.of(1L, 2L, 3L);

        // 사용자가 동의한 약관 ID 리스트
        List<Long> agreedTermsIds = request.getAgreedTermsIds();

        // 필수 약관에 동의했는지 확인
        if (!agreedTermsIds.containsAll(requiredTermsIds)) {
            throw new IllegalArgumentException("모든 필수 약관에 동의해야 합니다.");
        }

        // 회원가입 후 Redis에 저장된 전화번호 인증 정보를 삭제
        deletePhoneNumberVerification(request.getPhone());

        Member member = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .birthday(request.getBirthday())
                .createdAt(LocalDate.now())
                .role(Role.ROLE_USER)
                .provider(Provider.NORMAL)
                .build();

        // 기본 프로필 등록
        profileService.registerMainProfile(member);


        // SAVE MEMBER ENTITY
        memberService.saveMember(member);


        // 동의한 필수 약관 정보 저장
        for (Long termsId : agreedTermsIds) {
            TermsCondition termsCondition = termsConditionService.findTermsConditionById(termsId);

            memberTermsAgreementService.saveMemberTermsAgreement(
                    MemberTermsAgreement.builder()
                            .member(member)
                            .termsCondition(termsCondition)
                            .isAgreed(true)
                            .agreedAt(LocalDate.now())
                            .build());
        }


        // 선택 약관 정보 저장 - 마케팅
        consentService.saveMarketingConsent(member, request.getMarketingConsent());

        System.out.println("555");


        // 선택 약관 정보 저장 - 시스템
        consentService.saveSystemConsent(member, request.getSystemConsent());
    }



    /**
     * 전화번호로 이메일 찾기
     * 1) CoolSms를 통해 User에게 인증코드 전송
     * 2) 받은 인증코드를 Redis 내에 (key: 전화번호, value:인증코드) 형태로 저장
     * 3) Redis 내에 인증코드가 존재한다면 email 반환
     * @return
     */
    public FindEmailResponse findEmailByPhone(FindEmailByPhoneRequest request) {

        // 인증되지않은 전화번호
        if(!isTokenPhone(request.getPhone())){
            throw new DataIntegrityViolationException("잘못된 요청입니다.");
        }

        // Redis에 저장된 전화번호 인증 정보를 삭제
        deletePhoneNumberVerification(request.getPhone());

        Member member = memberService.findByPhone(request.getPhone());
        smsService.deletePhone(request.getPhone());
        FindEmailResponse findEmailResponse = FindEmailResponse.builder()
                .provider(member.getProvider())
                .email(member.getEmail())
                .build();

        return findEmailResponse;
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
    public void findPassword(PwFindRequest request) {

        // 인증되지않은 전화번호
        if(!isTokenPhone(request.getPhone())){
            throw new DataIntegrityViolationException("잘못된 요청입니다.");
        }
        // Redis에 저장된 전화번호 인증 정보를 삭제
        deletePhoneNumberVerification(request.getPhone());

        Member member = memberService.findByEmailAndPhone(request.getEmail(), request.getPhone());
        member.updatePassword(passwordEncoder.encode(request.getPassword()));
        memberService.saveMember(member);
    }


    /** [일반] 로그인 API */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // CHECK EMAIL AND PASSWORD

        Member member = memberService.findByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // FCM Token 갱신
        fcmService.updateFCMToken(member, request.getFcmToken());

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
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(member.getNickname())
                .email(member.getEmail())
                .build();

        return loginResponse;
    }



    /** [일반] 어드민 로그인 API */
    @Transactional
    public LoginResponse adminLogin(LoginRequest request) {
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
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(member.getNickname())
                .email(member.getEmail())
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
        String email = signInByKakao(token);
        Member member = new Member();
        Optional<Member> findMember = memberService.findOpMemberByEmail(email);
        if (findMember.isEmpty()) { // 최초 로그인 시 회원가입
            member = singUpByKakao(token);
            memberService.saveMember(member);
        } else {
            member = findMember.get();
        }
        TokenResponse tokenResponse = createAndSaveToken(member);

        return LoginResponse.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .build();
    }

    private String signInByKakao(String token) {
        // 카카오 로그인
        KakaoInfo memberInfo = kakaoClient.getUserInfo("Bearer " + token);
        return memberInfo.getKakaoAccount().getEmail();
    }

    private Member singUpByKakao(String token) {

        // 카카오 회원가입
        KakaoInfo memberInfo = kakaoClient.getUserInfo("Bearer " + token);
        String email = memberInfo.getKakaoAccount().getEmail();
        String nickname = memberInfo.getKakaoAccount().getProfile().getNickname();

        // 닉네임 중복 방지
        String newNickname = generateNicknameWithUUID(nickname);
        while (memberService.existByNickname(newNickname)){
            newNickname = generateNicknameWithUUID(nickname);
        }

        String gender = memberInfo.getKakaoAccount().getGender();
        // 생년
        String birthyear = memberInfo.getKakaoAccount().getBirthyear();
        // 월일
        String birthday = memberInfo.getKakaoAccount().getBirthday();
        String phone = memberInfo.getKakaoAccount().getPhone_number();

        // LocalDate 타입 변환 형식 지정 (yyyy-mm-dd)
        String birth_str = birthyear + "-" + birthday.substring(0,2) + "-" + birthday.substring(2,4);
        LocalDate birth = LocalDate.parse(birth_str, DateTimeFormatter.ISO_DATE);

        String password = socialRandomPassword(email);
        Member member = new Member(newNickname, email, password, phone, Gender.valueOf(gender.toUpperCase()), birth, LocalDate.now(), Role.ROLE_USER, Provider.KAKAO);
        return member;
    }


    private TokenResponse createAndSaveToken(Member member) {
        String accessToken = jwtTokenProvider.generateAccessToken(new UsernamePasswordAuthenticationToken(new CustomUserDetails(member), member.getPassword()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(new UsernamePasswordAuthenticationToken(new CustomUserDetails(member), member.getPassword()));
        refreshTokenService.saveRefreshToken(
                RefreshToken.builder()
                        .id(member.getEmail())
                        .refresh_token(refreshToken)
                        .expiration(14)
                        .build());

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return tokenResponse;
    }

    private String socialRandomPassword(String email) {
        String systemMil = String.valueOf(System.currentTimeMillis());
        return passwordEncoder.encode(email + systemMil);
    }

    /**
     * redis에 인증된 전화번호인지 체크
     * */
    public boolean isTokenPhone(String phone) {
        // Redis에 해당 번호가 존재하는지 확인
        return Boolean.TRUE.equals(redisTemplate.hasKey(getRedisKeyForToken(phone)));
    }
    private String getRedisKeyForToken(String token) {
        return "verified_phone:" + token;
    }

    // 회원가입 후 Redis에 저장된 전화번호 인증 정보를 삭제
    public void deletePhoneNumberVerification(String phone) {
        // Redis에서 해당 전화번호와 연결된 인증 정보를 삭제
        String key = "verified_phone:" + phone;
        redisTemplate.delete(key);  // 해당 키를 삭제
    }

    // 4자리 UUID를 닉네임 뒤에 붙여서 생성하는 메서드
    private String generateNicknameWithUUID(String baseNickname) {
        String uuid4 = UUIDUtil.generate4CharUUID();
        return baseNickname + uuid4;
    }
}
