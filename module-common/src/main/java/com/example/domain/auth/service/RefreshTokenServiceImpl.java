package com.example.domain.auth.service;

import com.example.global.config.jwt.RefreshToken;
import com.example.global.config.jwt.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void saveRefreshToken(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public boolean existsById(String email){
        return refreshTokenRepository.existsById(email);
    }

    @Override
    public void deleteById(String email) {
        refreshTokenRepository.deleteById(email);
    }
}
