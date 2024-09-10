package com.example.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/eureka",
            "/auth/login",
            "/auth/signup",
            "/auth/find/email",
            "/auth/email/duplicate",
            "/auth/phone/find/password",
            "/auth/kakao/authorize", // <- 이후 컨트롤러와 삭제 필요
            "/auth/kakao/access",
            "/auth/kakao/login",
            "/auth/refresh",
            "/sms/fake/send",
            "/sms/verify",
            "/admin/login"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
