package com.example.domain.member.controller;

import com.example.domain.member.service.consent.ConsentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/module-admin/marketing")
public class ConsentController {

    private final ConsentService consentService;

    @GetMapping("/search/all")
    public ResponseEntity<?> searchMarketingMember(){
        return ResponseEntity.status(HttpStatus.OK).body(consentService.getMarketingConsentFCMTokenList());
    }

}
