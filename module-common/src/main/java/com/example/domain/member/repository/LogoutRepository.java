package com.example.domain.member.repository;

import com.example.domain.member.entity.Logout;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutRepository extends CrudRepository<Logout, String> {
}
