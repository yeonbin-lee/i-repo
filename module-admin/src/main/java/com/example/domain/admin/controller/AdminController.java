package com.example.domain.admin.controller;

import com.example.domain.admin.controller.dto.request.RestoreRequest;
import com.example.domain.admin.controller.dto.response.DeletedFilterResponse;
import com.example.domain.admin.controller.dto.response.FilterResponse;
import com.example.domain.admin.service.AdminService;
import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.repository.DeletedMemberRepository;
import com.example.domain.deleted.service.DeletedMemberService;
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
    private final DeletedMemberService deletedMemberService;
    private final DeletedMemberRepository deletedMemberRepository;

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
    @DeleteMapping("/member/restore")
    private ResponseEntity<?> restoreMember(@RequestBody RestoreRequest request){
        deletedMemberService.restoreMember(request);
//        adminService.restoreMember(request);
        return ResponseEntity.status(HttpStatus.OK).body("User Restored Successfully!");
    }

    /**
     * admin - 회원 조회
     * */
    @GetMapping("/search/member")
    public ResponseEntity<List<FilterResponse>> searchMembers(
            @RequestParam(required = false) String field,
            @RequestParam(required = false) Object value,
            @RequestParam(required = false) String profileField,
            @RequestParam(required = false) Object profileValue,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String dateField
    ) {

        List<FilterResponse> responses = adminService.searchMembers(field, value, profileField, profileValue, startDate, endDate, dateField);
        return ResponseEntity.ok(responses);
    }

    /**
     * admin - 탈퇴 회원 조회
     * */
    @GetMapping("/search/deletedMember")
    public ResponseEntity<List<DeletedFilterResponse>> searchDeletedMembers(
            @RequestParam(required = false) String field,
            @RequestParam(required = false) Object value,
            @RequestParam(required = false) String profileField,
            @RequestParam(required = false) Object profileValue,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String dateField
    ) {

        List<DeletedFilterResponse> responses = adminService.searchDeletedMembers(field, value, profileField, profileValue, startDate, endDate, dateField);
        return ResponseEntity.ok(responses);
    }



}
