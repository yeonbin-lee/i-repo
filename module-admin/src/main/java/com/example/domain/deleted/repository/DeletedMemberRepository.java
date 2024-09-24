package com.example.domain.deleted.repository;

import com.example.domain.deleted.entity.DeletedMember;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeletedMemberRepository extends JpaRepository<DeletedMember, Long>, JpaSpecificationExecutor<DeletedMember> {

    List<DeletedMember> findByCancelledAtBefore(LocalDate date);

    @EntityGraph(attributePaths = {"deletedDefaultProfile", "deletedProfiles"})
    Optional<DeletedMember> findById(Long id);

    @EntityGraph(attributePaths = {"deletedDefaultProfile", "deletedProfiles"})
    List<DeletedMember> findAll(Specification<DeletedMember> spec);

    @Query("SELECT m FROM DeletedMember m " +
            "JOIN FETCH m.deletedDefaultProfile dp " +
            "LEFT JOIN FETCH m.deletedProfiles p " +
            "WHERE m.id = :id")
    Optional<DeletedMember> findMemberWithProfilesById(@Param("id") Long id);


}

