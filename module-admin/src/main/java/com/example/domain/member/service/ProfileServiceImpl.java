package com.example.domain.member.service;

import com.example.domain.member.entity.Profile;
import com.example.domain.member.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public void saveProfile(Profile profile){
        profileRepository.save(profile);
    }

//    @Override
//    public Profile findMainProfile(Long memberId){
//        Optional<Profile> profile = profileRepository.findByMemberIdAndOwner(memberId);
//        if (profile == null) {
//            return null;
//        } return profile.get();
//    }
}
