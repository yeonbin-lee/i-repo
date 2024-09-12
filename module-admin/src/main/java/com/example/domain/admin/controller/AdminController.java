package com.example.domain.admin.controller;

import com.example.domain.admin.controller.dto.RestoreRequest;
import com.example.domain.admin.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @PostMapping("/member/restore")
    private ResponseEntity<?> restoreMember(@RequestBody RestoreRequest request){
        adminService.restoreMember(request);
        return ResponseEntity.status(HttpStatus.OK).body("User Restored Successfully!");
    }




}
