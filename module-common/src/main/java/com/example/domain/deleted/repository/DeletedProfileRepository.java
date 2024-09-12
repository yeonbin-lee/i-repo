package com.example.domain.deleted.repository;

import com.example.domain.deleted.entity.DeletedProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletedProfileRepository extends JpaRepository<DeletedProfile, Long> {
}
