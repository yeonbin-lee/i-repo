package com.example.domain.member.controller;

import com.example.domain.auth.controller.dto.request.LogoutRequest;
import com.example.domain.member.controller.dto.request.member.NicknameChangeRequest;
import com.example.domain.member.controller.dto.request.member.PwChangeRequest;
import com.example.domain.member.controller.dto.response.MemberResponse;
import com.example.domain.member.service.memberService.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/module-common/user")
public class MemberController {

    private final MemberService memberService;

    /**
     * [일반] 비밀번호 재설정 API - 로그인을 한 상태에서 비밀번호를 변경하고싶을 경우
     * @param request - originPassword, newPassword 사용자 입력
     * */
    @PutMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String accessToken, @RequestBody @Valid PwChangeRequest request){
        memberService.changePassword(accessToken, request);
        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully!");
    }

    /**
     * 회원 닉네임 수정 API
     * @param request - newNickname 사용자 입력
     *  */
    @PutMapping("/change/nickname")
    public ResponseEntity<?> changeNickname(@RequestHeader("Authorization") String accessToken, @RequestBody @Valid NicknameChangeRequest request) {
        memberService.changeNickname(accessToken, request);
        return ResponseEntity.status(HttpStatus.OK).body("Nickname changed successfully!");
    }

    /**
     * 회원정보 삭제 API
     * */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String accessToken) {
        memberService.delete(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    /**
     * 로그아웃
     * 1. Redis내의 refresh_token 삭제
     * 2. Redis에 현재 access_token을 logout 상태로 등록
     * - 2.1. 해당 access_token의 남은 유효시간을 Redis의 TTL로 등록
     * 3. JwtTokenFilter 파일의 doFIlterInternal 메소드에서 redis에 logout 상태인지 검증하는 로직 추가
     * @param request - email
     * */
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken, @RequestBody LogoutRequest request) {
        memberService.logout(accessToken, request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("User logout!");
    }

    /**
     * 회원정보 조회 API
     * @return id, role, email, nickname, phone, gender, birthday
     * */
    @GetMapping("/info")
    public ResponseEntity<?> findUser(@RequestHeader("Authorization") String accessToken) {
        MemberResponse userResponseDto = memberService.findById(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

}
