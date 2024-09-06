package com.example.domain.member.controller;

import com.example.domain.member.controller.dto.request.member.NicknameChangeRequest;
import com.example.domain.member.controller.dto.request.member.PwChangeRequest;
import com.example.domain.member.controller.dto.response.MemberResponse;
import com.example.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
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
     * 회원정보 조회 API
     * @return id, role, email, nickname, phone, gender, birthday
     * */
    @GetMapping("/info")
    public ResponseEntity<?> findUser(@RequestHeader("Authorization") String accessToken) {
        MemberResponse userResponseDto = memberService.findById(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    /**
     * 회원정보 삭제 API
     * */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String accessToken) {
        memberService.delete(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }
}
