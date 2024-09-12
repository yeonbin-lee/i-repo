package com.example.domain.deleted.repository;

import com.example.domain.deleted.entity.DeletedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeletedMemberRepository extends JpaRepository<DeletedMember, Long> {

//    @Modifying
//    @Query("DELETE FROM DeletedMember dm WHERE dm.cancelledAt <= :cutoffDate")
//    void deleteOldMembers(@Param("cutoffDate") LocalDate cutoffDate);

    List<DeletedMember> findByCancelledAtBefore(LocalDate date);

    @Query("SELECT dm FROM DeletedMember dm JOIN FETCH dm.deletedProfiles WHERE dm.id = :id")
    Optional<DeletedMember> findByIdWithProfiles(@Param("id") Long id);
}

