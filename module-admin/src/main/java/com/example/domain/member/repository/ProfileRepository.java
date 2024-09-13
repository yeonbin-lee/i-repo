package com.example.domain.member.repository;

import com.example.domain.member.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT COUNT(p) FROM Profile p WHERE p.owner = true")
    Long countProfilesWithOwnerTrue();

    @Query("SELECT COUNT(p) FROM Profile p")
    Long countAllProfiles();

    @Query("SELECT p FROM Profile p where p.member.id=:memberId AND p.owner =: true")
    Optional<Profile> findByMemberIdAndOwner(Long memberId);
}

