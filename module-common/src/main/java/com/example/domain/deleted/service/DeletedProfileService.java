package com.example.domain.deleted.service;

import com.example.domain.deleted.entity.DeletedMember;
import com.example.domain.deleted.entity.DeletedProfile;
import com.example.domain.member.entity.Profile;

public interface DeletedProfileService {

    public void convertDefaultProfileToDeletedDefaultProfile(DeletedMember deletedMember, Profile profile);
    public void convertProfileToDeletedProfile(DeletedMember deletedMember, Profile profile);
    public DeletedProfile saveDeletedProfile(DeletedProfile deletedProfile);
//    public void registerProfile(DeletedMember deletedMember);
//    public void registerMainProfile(DeletedMember deletedMember);
}