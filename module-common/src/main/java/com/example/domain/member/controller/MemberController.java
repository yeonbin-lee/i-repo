package com.example.domain.member.controller;

import com.example.domain.member.controller.dto.request.member.CheckEmailDuplicateRequest;
import com.example.domain.member.controller.dto.request.member.NicknameChangeRequest;
import com.example.domain.member.controller.dto.request.member.PwChangeRequest;
import com.example.domain.member.controller.dto.request.member.PwFindRequest;
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


//    /** 이메일 중복체크 */
//    // Q. 어떤 provider를 사용하는지 알려아하는가? ex) normal, kakao
//    @PostMapping("/api/v1/user/email/duplicate")
//    public ResponseEntity<?> emailDuplicate(@RequestBody @Valid CheckEmailDuplicateRequest requestDto){
//        Boolean checkEmailDuplicate = memberService.checkDuplicateEmail(requestDto.getEmail());
//        return ResponseEntity.status(HttpStatus.OK).body(checkEmailDuplicate);
//    }


//    /** 전화번호로 이메일 찾기 */
//    @PostMapping("/find/email")
//    public ResponseEntity<?> findEmailByPhone(@RequestBody @Valid FindEmailDTO findEmailDTO){
//        try{
//            String email = memberService.findEmailByPhone(findEmailDTO);
//            return ResponseEntity.status(HttpStatus.OK).body(email);
//        } catch (IllegalStateException e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    /** 1. 비밀번호 재설정
//     *  - 비밀번호를 잊어버렸을 경우
//     * */
//    @PutMapping("/change/password/v1")
//    public ResponseEntity<?> changePasswordV1(@RequestBody @Valid PwFindRequest pwFindRequest){
//        memberService.changePasswordV1(pwFindRequest);
//        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully!");
//    }
//
//    /** 2. 비밀번호 재설정
//     *  - 비밀번호를 변경하고싶을 경우
//     * */
//    @PutMapping("/change/password/v2")
//    public ResponseEntity<?> changePasswordV2(@RequestHeader("Authorization") String accessToken, @RequestBody @Valid PwChangeRequest pwChangeRequest){
//        System.out.println("access_token=" + accessToken);
//        memberService.changePasswordV2(accessToken, pwChangeRequest);
//        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully!");
//
//    }
//
//    /** 회원 닉네임 수정 */
//    @PutMapping("/change/nickname")
//    public ResponseEntity<?> changeNickname(@RequestHeader("Authorization") String accessToken, @RequestBody @Valid NicknameChangeRequest nicknameChangeRequest) {
//        memberService.changeNickname(accessToken, nicknameChangeRequest);
//        return ResponseEntity.status(HttpStatus.OK).body("Nickname changed successfully!");
//    }

    /** 회원정보 조회 API */
    @GetMapping("/info")
    public ResponseEntity<?> findUser(@RequestHeader("Authorization") String accessToken) {
        System.out.println("access_token=" + accessToken);
        MemberResponse userResponseDto = memberService.findById(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

//    /** 회원정보 삭제 API */
//    @DeleteMapping("/api/v1/user")
//    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String accessToken) {
//        memberService.delete(accessToken);
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//    }
}
