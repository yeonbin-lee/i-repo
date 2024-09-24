package com.example.domain.deleted.repository;

import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.DeletedProfile;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DeletedMemberSpecification {

    public static Specification<DeletedMember> filterByField(String field, Object value) {
        return (root, query, criteriaBuilder) -> {
            if (field != null && value != null) {
                switch (field) {
                    case "id":
                        return criteriaBuilder.equal(root.get("id"), value);
                    case "email":
                        // 대소문자를 무시하고 포함 여부 검사
                        String lowerEmail = ((String) value).toLowerCase();
                        return criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("email")),
                                "%" + lowerEmail + "%"
                        );
                    case "nickname":
                        // 대소문자를 무시하고 포함 여부 검사
                        String lowerNickname = ((String) value).toLowerCase();
                        return criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("nickname")),
                                "%" + lowerNickname + "%"
                        );
                    case "gender":
                        return criteriaBuilder.equal(root.get("gender"), value);
                    case "role":
                        return criteriaBuilder.equal(root.get("role"), value);
                    case "resignationReason":
                        // 대소문자를 무시하고 포함 여부 검사
                        String lowerResignationReason = ((String) value).toLowerCase();
                        return criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("resignationReason")),
                                "%" + lowerResignationReason + "%"
                        );
                }
            }
            return null; // 아무 필터도 없을 경우 null 반환
        };
    }

    public static Specification<DeletedMember> filterByProfileField(String profileField, Object profileValue) {
        return (root, query, criteriaBuilder) -> {
            if (profileField != null && profileValue != null) {
                Join<DeletedMember, DeletedProfile> deletedProfile = root.join("deletedDefaultProfile");
                switch (profileField) {
                    case "pregnancy":
                        return criteriaBuilder.equal(deletedProfile.get("pregnancy"), profileValue);
                    case "smoking":
                        return criteriaBuilder.equal(deletedProfile.get("smoking"), profileValue);
                    case "hypertension":
                        return criteriaBuilder.equal(deletedProfile.get("hypertension"), profileValue);
                    case "diabetes":
                        return criteriaBuilder.equal(deletedProfile.get("diabetes"), profileValue);
                }
            }
            return null;
        };
    }

    public static Specification<DeletedMember> filterByDateRange(String dateField, LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null && dateField != null) {
                switch (dateField) {
                    case "birthday":
                        return criteriaBuilder.between(root.get("birthday"), startDate, endDate);
                    case "createdAt":
                        return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
                    case "cancelledAt":
                        return criteriaBuilder.between(root.get("cancelledAt"), startDate, endDate);
                }
            }
            return null;
        };
    }
}