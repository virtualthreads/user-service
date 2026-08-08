package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository
        extends JpaRepository<User, UUID>,
        JpaSpecificationExecutor<User> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}