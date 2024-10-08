package com.example.domain.admin.service;

import com.example.domain.admin.controller.dto.response.DeletedFilterResponse;
import com.example.domain.admin.controller.dto.response.FilterResponse;
import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.repository.DeletedMemberSpecification;
import com.example.domain.deleted.service.DeletedMemberService;
import com.example.domain.member.entity.Member;
import com.example.domain.member.mapper.DeletedMemberMapper;
import com.example.domain.member.mapper.MemberMapper;
import com.example.domain.member.repository.MemberSpecification;
import com.example.domain.member.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final DeletedMemberService deletedMemberService;
    private final MemberService memberService;


    public List<FilterResponse> searchMembers(String field, Object value, String profileField, Object profileValue, LocalDate startDate, LocalDate endDate, String dateField) {
        Specification<Member> spec = Specification.where(null);

        if (value != null) {
            spec = spec.and(MemberSpecification.filterByField(field, value));
        }

        if (profileValue != null) {
            spec = spec.and(MemberSpecification.filterByProfileField(profileField, profileValue));
        }

        if (startDate != null && endDate != null && dateField != null) {
            spec = spec.and(MemberSpecification.filterByDateRange(dateField, startDate, endDate));
        }
        List<Member> members = memberService.findAll(spec);
        return members.stream()
                .map(MemberMapper::toFilterResponse)
                .collect(Collectors.toList());
    }



    public List<DeletedFilterResponse> searchDeletedMembers(String field, Object value, String profileField, Object profileValue, LocalDate startDate, LocalDate endDate, String dateField) {
        Specification<DeletedMember> spec = Specification.where(null);
        if (value != null) {
            spec = spec.and(DeletedMemberSpecification.filterByField(field, value));
        }

        if (profileValue != null) {
            spec = spec.and(DeletedMemberSpecification.filterByProfileField(profileField, profileValue));
        }

        if (startDate != null && endDate != null && dateField != null) {
            spec = spec.and(DeletedMemberSpecification.filterByDateRange(dateField, startDate, endDate));
        }

        List<DeletedMember> deletedMembers = deletedMemberService.findAll(spec);
        return deletedMembers.stream()
                .map(DeletedMemberMapper::toFilterResponse)
                .collect(Collectors.toList());
    }


}
