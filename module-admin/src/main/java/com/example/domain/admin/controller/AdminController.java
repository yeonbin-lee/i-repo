package com.example.domain.admin.controller;

import com.example.domain.admin.controller.dto.RestoreRequest;
import com.example.domain.admin.service.AdminService;
import com.example.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/module-admin/admin")
public class AdminController {

    private final AdminService adminService;

    // 테스팅 용도
    @PostMapping("/aa")
    private ResponseEntity<?> aabb(){
        System.out.println("들어옴");
        return ResponseEntity.status(HttpStatus.OK).body("aa successfully!");
    }

//    @GetMapping("/manage/user")
//    public ResponseEntity<?> manageUser()
//

    /**
     * 계정, 프로필 복구 API
     */

    @PostMapping("/member/restore")
    private ResponseEntity<?> restoreMember(@RequestBody RestoreRequest request){
        adminService.restoreMember(request);
        return ResponseEntity.status(HttpStatus.OK).body("User Restored Successfully!");
    }

    @GetMapping("/members")
    public ResponseEntity<?> getMembers(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "false") boolean isBirthDayFilter, // 생년월일 또는 회원가입일 필터 선택
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String gender) {

        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(isBirthDayFilter);
        System.out.println(memberId);
        List<Member> members = adminService.searchMembers(startDate, endDate, isBirthDayFilter, memberId, email, nickname, gender);
//          return ResponseEntity.status(HttpStatus.OK).body("흠?");
//        return adminService.searchMembers(startDate, endDate, isBirthDayFilter, memberId, email, nickname, gender);
        return ResponseEntity.status(HttpStatus.OK).body(members);
    }






}
