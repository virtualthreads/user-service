package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // =========================================================
    // CREATE USER
    // =========================================================

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    // =========================================================
    // GET USER
    // =========================================================

    Optional<User> findByUserId(UUID userId);

    // =========================================================
    // SEARCH USERS
    // =========================================================

    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email
    );

    List<User> findByStatus(UserStatus status);

    // =========================================================
    // UPDATE USER
    // =========================================================

    Optional<User> findByEmail(String email);

    // =========================================================
    // DELETE USER
    // =========================================================

    Optional<User> findById(UUID id);

}