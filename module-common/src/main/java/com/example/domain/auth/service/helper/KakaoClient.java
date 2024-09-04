package com.example.domain.auth.service.helper;

import com.example.domain.member.controller.vo.KakaoInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "kakaoClient", url = "https://kapi.kakao.com")
public interface KakaoClient {

    @GetMapping("/v2/user/me")
    KakaoInfo getUserInfo(@RequestHeader("Authorization") String accessToken);  // 카카오의 idToken = accessToken
}
