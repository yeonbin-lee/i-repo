package com.example.domain.member.repository;


import com.example.domain.member.entity.Member;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MemberSpecification {

    public static Specification<Member> betweenBirthDay(LocalDate startDate, LocalDate endDate) {
        return (root, query, builder) -> builder.between(root.get("birthday"), startDate, endDate);
    }

    public static Specification<Member> betweenCreatedAt(LocalDate startDate, LocalDate endDate) {
        return (root, query, builder) -> builder.between(root.get("createdAt"), startDate, endDate);
    }

    public static Specification<Member> hasMemberId(Long memberId) {
        return (root, query, builder) -> builder.equal(root.get("id"), memberId);
    }

    public static Specification<Member> hasEmail(String email) {
        return (root, query, builder) -> builder.equal(root.get("email"), "%" + email + "%");
    }

    public static Specification<Member> hasNickname(String nickname) {
        return (root, query, builder) -> builder.like(root.get("nickname"), "%" + nickname + "%");
    }

    public static Specification<Member> hasGender(String gender) {
        return (root, query, builder) -> builder.equal(root.get("gender"), gender);
    }
}