package com.example.gateway.filter;

import com.example.gateway.exception.UnauthorizedAccessException;
import com.example.gateway.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Order(1)
public class AuthenticationFilter implements GlobalFilter {


    private final List<String> excludedPaths = List.of(
            "/eureka",
            "/module-common/auth/login",
            "/module-common/auth/signup",
            "/module-common/auth/find/email",
            "/module-common/auth/email/duplicate",
            "/module-common/auth/phone/find/password",
            "/module-common/auth/kakao/authorize", // <- 이후 컨트롤러와 삭제 필요
            "/module-common/auth/kakao/access",
            "/module-common/auth/kakao/login",
            "/module-common/auth/refresh",
            "/module-common/sms/fake/send",
            "/module-common/sms/verify",
            "/module-admin/admin/login"
            );  // 필터를 적용하지 않을 경로


    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        System.out.println("Request Path: " + path);  // 경로를 로그로 확인

        // 로그인 및 회원가입 경로는 JWT 검증하지 않음
        if (excludedPaths.contains(path)) {
            return chain.filter(exchange);
        }

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authorizationHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

//         로그아웃 체크
        if (jwtUtil.isTokenLogout(token)) {
            throw new UnauthorizedAccessException();
        }

        return chain.filter(exchange);
    }

}
