package com.example.domain.member.service;

import com.example.domain.member.entity.Profile;
import com.example.domain.member.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public void saveProfile(Profile profile){
        profileRepository.save(profile);
    }
}
