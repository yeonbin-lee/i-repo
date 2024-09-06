package com.example.domain.auth.service;

import com.example.global.config.jwt.RefreshToken;

public interface RefreshTokenService {

    public void saveRefreshToken(RefreshToken refreshToken);
    public boolean existsById(String email);
    public void deleteById(String email);

}
