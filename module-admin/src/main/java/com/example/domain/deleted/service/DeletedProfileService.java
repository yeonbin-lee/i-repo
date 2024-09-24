package com.example.domain.deleted.service;

import com.example.domain.deleted.entity.DeletedProfile;

public interface DeletedProfileService {
    public void deletedDeletedProfile(DeletedProfile deletedProfile);
    public DeletedProfile findDeletedProfileById(Long id);
}
