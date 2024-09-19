package com.example.domain.admin.controller;

import com.example.domain.admin.controller.dto.request.RestoreRequest;
import com.example.domain.admin.controller.dto.response.FilterResponse;
import com.example.domain.admin.service.AdminService;
import com.example.domain.member.entity.Member;
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

    @GetMapping("/search")
    public ResponseEntity<List<FilterResponse>> searchMembers(
            @RequestParam(required = false) String field,
            @RequestParam(required = false) Object value,
            @RequestParam(required = false) String profileField,
            @RequestParam(required = false) Object profileValue,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String dateField
    ) {
        System.out.println("field=" + field);
        System.out.println("value=" + value);

        List<FilterResponse> responses = adminService.searchMembers(field, value, profileField, profileValue, startDate, endDate, dateField);
        return ResponseEntity.ok(responses);
    }






}
