package com.aeropelican.userservice.repository;
import com.aeropelican.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.UUID;
public interface UserRepository extends JpaRepository<User, UUID>,
        JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUserIdNot(String email, UUID userId);
    boolean existsByUserId(UUID userId);

    Optional<User> findByUserId(UUID userId);
}
