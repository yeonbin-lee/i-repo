package com.example.gateway.filter;

import com.example.gateway.exception.InvalidAuthorizationHeaderFormatException;
import com.example.gateway.exception.MissingAuthorizationHeaderException;
import com.example.gateway.exception.UnauthorizedAccessException;
import com.example.gateway.util.JwtUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

// Global Filter 적용
@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // pre
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (validator.isSecured.test(request)) {
                // JWT 검증 로직
                List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

                if (authHeaders == null || authHeaders.isEmpty()) {
                    throw new MissingAuthorizationHeaderException();
                }

                String authHeader = authHeaders.get(0);

                if (authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);

                    try {
                        jwtUtil.validateToken(token);

                        // 로그아웃 체크
                        if (jwtUtil.isTokenLogout(token)) {
                            throw new UnauthorizedAccessException();
                        }
                    } catch (Exception e) {
                        throw new UnauthorizedAccessException();
                    }

                } else {
                    throw new InvalidAuthorizationHeaderFormatException();
                }
            }

            // post
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();
                log.info("Custom Post filter: response code: " + response.getStatusCode());
            }));
        };
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
