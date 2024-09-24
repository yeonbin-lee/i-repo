package com.example.domain.deleted.service;

import com.example.domain.admin.controller.dto.request.RestoreRequest;
import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.member.entity.Member;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DeletedMemberService {

    public void restoreMember(RestoreRequest request);

    public DeletedMember findDeletedMemberById(Long id);

    public List<DeletedMember> findAll(Specification<DeletedMember> spec);
}
