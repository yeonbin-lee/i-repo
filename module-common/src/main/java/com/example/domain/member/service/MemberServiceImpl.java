package com.example.domain.member.service;

import com.example.domain.member.controller.dto.request.member.FindEmailByPhoneRequest;
import com.example.domain.member.controller.dto.request.member.NicknameChangeRequest;
import com.example.domain.member.controller.dto.request.member.PwChangeRequest;
import com.example.domain.member.controller.dto.request.member.PwFindRequest;
import com.example.domain.member.controller.dto.response.MemberResponse;
import com.example.domain.member.entity.Member;
import com.example.domain.member.repository.MemberRepository;
import com.example.global.config.jwt.JwtTokenProvider;
import com.example.global.exception.custom.NotEqualsCodeException;
import com.example.global.exception.custom.NotEqualsPasswordException;
import com.example.global.exception.custom.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    /** Member 조회 */
    @Override
    @Transactional
    public MemberResponse findById(String accessToken) {
        Long id = findMemberIdByAccessToken(accessToken);
        Member member = this.memberRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));
        return new MemberResponse(member);
    }
//
//
//    /** Member 삭제 */
//    @Override
//    @Transactional
//    public void delete(String accessToken) {
//        Long id = findMemberIdByAccessToken(accessToken);
//        Member member = memberRepository.findById(id).orElseThrow(
//                () -> new UserNotFoundException("해당 유저를 찾을 수 없습니다. member_id = " + id));
//        memberRepository.delete(member);
//    }
//
//    /** 이메일 중복체크 */
//    public boolean checkDuplicateEmail(String email){
//        return memberRepository.existsByEmail(email);
//    }
//
//
//    /** 1. 비밀번호 재설정
//     *  - 비밀번호를 잊어버렸을 경우
//     *  1) coolSms를 통해 전화번호로 인증코드 전송
//     *  2) 인증코드를 Redis에 저장한 후, (key: 전화번호, value:인증코드) 형태로 저장
//     *  3) Redis 내에 인증코드가 존재한다면 password 재설정
//     * */
//    public void changePasswordV1(PwFindRequest pwFindRequest) {
//
//        String phone = pwFindRequest.getPhone();
//        String new_password = pwFindRequest.getPassword();
//        String code = pwFindRequest.getCode();
//
//        Sms sms = verifyRedisByPhone(phone);
//
//        if (code.equals(sms.getCode())) {
//            Member member = memberRepository.findByPhone(phone).orElseThrow(
//                    () -> new UserNotFoundException("등록되지 않은 전화번호입니다.")
//            );
//            String encryptPassword = passwordEncoder.encode(new_password);
//            member.updatePassword(encryptPassword);
//            memberRepository.save(member);
//        } else {
//            throw new NotEqualsCodeException("인증번호가 일치하지 않습니다.");
//        }
//    }
//
//    /** 2. 비밀번호 재설정
//     *  - 비밀번호를 변경하고싶을 경우
//     *  1) User로부터 받은 비밀번호와 Access Token으로 찾은 password 비교
//     *  2) 같을 경우 password 재설정
//     * */
//    public void changePasswordV2(String accessToken, PwChangeRequest pwChangeRequest) {
//
//        String originPassword = pwChangeRequest.getOriginPassword();
//        String newPassword = pwChangeRequest.getNewPassword();
//
//        Long id = findMemberIdByAccessToken(accessToken);
//        Member member = memberRepository.findById(id).orElseThrow(
//                () -> new UserNotFoundException("회원 정보가 존재하지 않습니다.")
//        );
//
//        if(passwordEncoder.matches(originPassword, member.getPassword())) {
//            String encryptPassword = passwordEncoder.encode(newPassword);
//            member.updatePassword(encryptPassword);
//            memberRepository.save(member);
//        } else {
//            throw new NotEqualsPasswordException("비밀번호가 일치하지 않습니다.");
//        }
//    }
//
//    /** 회원 닉네임 재설정 */
//    public void changeNickname(String accessToken, NicknameChangeRequest nicknameChangeRequest){
//        Long id = findMemberIdByAccessToken(accessToken);
//        String newNickname = nicknameChangeRequest.getNewNickname();
//        Member member = this.memberRepository.findById(id).orElseThrow(
//                () -> new UserNotFoundException("해당 유저를 찾을 수 없습니다. member_id = " + id));
//
//        if(!memberRepository.existsByNickname(newNickname)){
//            member.updateNickname(newNickname);
//            memberRepository.save(member);
//        } else {
//            throw new DataIntegrityViolationException("중복되는 닉네임입니다.");
//        }
//    }
//
//
//    /**
//     * 전화번호로 이메일 찾기
//     * 1) CoolSms를 통해 User에게 인증코드 전송
//     * 2) 받은 인증코드를 Redis내에 (key: 전화번호, value:인증코드) 형태로 저장
//     * 3) Redis 내에 인증코드가 존재한다면 email 반환
//     * @return
//     */
//    public String findEmailByPhone(FindEmailByPhoneRequest findEmailByPhoneRequest) {
//
//        String phone = findEmailByPhoneRequest.getPhoneNum();
//
//        // Redis 내에 Key(전화번호)가 존재하는지 확인
//        Sms sms = verifyRedisByPhone(phone);
//
//        if (findEmailByPhoneRequest.getCode().equals(sms.getCode())) {
//            Member member = memberRepository.findByPhone(findEmailByPhoneRequest.getPhoneNum()).orElseThrow(
//                    () -> new UserNotFoundException("등록되지 않은 전화번호입니다.")
//            );
//            return member.getEmail();
//        }
//        // 인증코드가 일치하지않는 경우
//        throw new NotEqualsCodeException();
//    }
//
//    private Sms verifyRedisByPhone(String phone){
//
//        // Redis 내에 Key(전화번호)가 존재하는지 확인
//        Sms sms = smsRepository.findById(phone).orElseThrow(
//                () -> new UserNotFoundException("등록되지 않은 전화번호입니다.")
//        );
//        // 타임아웃이 짧기 때문에 굳이 삭제할 필요는 없을 수도 있다ㅏ..
//        smsRepository.deleteById(phone);
//        return sms;
//    }
//
    private Long findMemberIdByAccessToken(String accessToken){
        return jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
    }
}
