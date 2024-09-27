package com.example.domain.member.controller;

import com.example.domain.admin.controller.dto.request.CreateTermsConditionRequest;
import com.example.domain.admin.controller.dto.request.DeleteTermsCondition;
import com.example.domain.admin.controller.dto.request.UpdateTermsConditionRequest;
import com.example.domain.admin.controller.dto.response.TermsConditionDTO;
import com.example.domain.member.entity.TermsCondition;
import com.example.domain.member.service.TermsConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/module-admin/terms")
public class TermsConditionController {

    private final TermsConditionService termsConditionService;

    /**
     * 필수 약관 생성 (모든 멤버에 대한 업데이트 발생)
     */
    @PostMapping("/create/mandatory/term")
    public ResponseEntity<?> createMandatoryTerm(@RequestBody CreateTermsConditionRequest request) {
        termsConditionService.createMandatoryTermsCondition(request);
        return ResponseEntity.status(HttpStatus.OK).body("Mandatory Terms Created Successfully!");
    }

    /**
     * 선택 약관 생성
     */
    @PostMapping("/create/optional/term")
    public ResponseEntity<?> createOptionalTerm(@RequestBody CreateTermsConditionRequest request) {
        termsConditionService.createOptionalTermsCondition(request);
        return ResponseEntity.status(HttpStatus.OK).body("Optional Terms Created Successfully!");


    }


    /**
     * 필수 약관 업데이트
     */
    @PutMapping("/update/term")
    public ResponseEntity<?> updateTerm(@RequestBody UpdateTermsConditionRequest request) {
        termsConditionService.updateTermsCondition(request);
        return ResponseEntity.status(HttpStatus.OK).body("Updated Terms Successfully!");
    }


    /**
     * 필수 약관 삭제
     */
    @DeleteMapping("/delete/term")
    public ResponseEntity<?> deleteTerm(@RequestBody DeleteTermsCondition request) {
        termsConditionService.deleteTermsCondition(request);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Terms Successfully!");
    }


    /**
     * 필수 약관 리스트 조회 - id, title
     */
    @GetMapping("/search/list")
    public ResponseEntity<?> searchList(){
        List<TermsConditionDTO> allTermsConditions = termsConditionService.findAllTermsCondition();
        return ResponseEntity.status(HttpStatus.OK).body(allTermsConditions);
    }

    /**
     * 필수 약관 상세 조회
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchTermsCondition(@RequestParam Long id){
        TermsCondition termsCondition = termsConditionService.findTermsConditionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(termsCondition);
    }



}


