package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndUserIdNot(String email, UUID userId);

    Optional<User> findByUserId(UUID userId);

    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
