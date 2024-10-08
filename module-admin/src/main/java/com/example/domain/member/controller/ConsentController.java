package com.example.domain.member.controller;

import com.example.domain.member.service.consent.ConsentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/module-admin/marketing")
public class ConsentController {

    private final ConsentService consentService;

    @GetMapping("/search/all")
    public ResponseEntity<?> sendNotificationToAll(){
        return ResponseEntity.status(HttpStatus.OK).body(consentService.findAllMarketingAgreedMember());
    }


}
