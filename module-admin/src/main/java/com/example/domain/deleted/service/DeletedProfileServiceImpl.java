package com.example.domain.deleted.service;

import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.DeletedProfile;
import com.example.domain.deleted.repository.DeletedProfileRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeletedProfileServiceImpl implements DeletedProfileService{

    private final DeletedProfileRepository deletedProfileRepository;

    @Transactional
    public void deletedDeletedProfile(DeletedProfile deletedProfile) {
        deletedProfileRepository.delete(deletedProfile);
    }


    public DeletedProfile findDeletedProfileById(Long id) {
        return deletedProfileRepository.findById(id).orElseThrow(
                () -> new NotFoundException("없음")
        );
    }


    private DeletedProfile saveDeletedProfile(DeletedProfile deletedProfile) {
        return deletedProfileRepository.save(deletedProfile);
    }
}
