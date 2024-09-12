package com.example.domain.member.service;

import com.example.domain.member.entity.Profile;
import com.example.domain.member.repository.ProfileRepository;

public interface ProfileService {

    public void saveProfile(Profile profile);
}
