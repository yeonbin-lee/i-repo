package com.example.global.config.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String jwtSecretKey;
    @Value("${spring.jwt.accessTokenExpirationTime}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${spring.jwt.refreshTokenExpirationTime}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;


    public String generateAccessToken(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        // getAuthorities()에서 Role을 추출하여 클레임에 설정
        List<String> roles = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // authority 값을 추출
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(customUserDetails.getUsername()) // email
                .claim("user-id", customUserDetails.getId())
                .claim("role", roles.get(0))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    /** access_token, refresh_token 모두 갱신된다*/
    public String generateRefreshToken(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        // getAuthorities()에서 Role을 추출하여 클레임에 설정
        List<String> roles = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // authority 값을 추출
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(customUserDetails.getUsername()) // email
                .claim("user-id", customUserDetails.getId())
                .claim("role", roles.get(0))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Long getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("user-id", Long.class);
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

//    public String getUserEmailFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(jwtSecretKey)
//                .parseClaimsJws(token)
//                .getBody()
//                .get("user-email", String.class);
//    }

    public Date getExpirationFromToken(String token) {
        System.out.println("token= " + token);

        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

}

