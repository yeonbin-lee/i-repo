package com.example.domain.member.repository;

import com.example.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    //    @Query("select * from ")
//    boolean existByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByNickname(String nickname);
    Optional<Member> findByPhone(String phone);
    Optional<Member> findByEmailAndPhone(String email, String phone);
}
