package com.example.domain.deleted.repository;

import com.example.domain.deleted.entity.Restore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestoreRepository extends JpaRepository<Restore, Long> {

}
