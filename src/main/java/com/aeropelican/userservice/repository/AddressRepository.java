package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    // Get all addresses of a user
    org.springframework.data.domain.Page<Address> findByUserUserId(
            String userId,
            org.springframework.data.domain.Pageable pageable);

    // Get default address of a user
    Optional<Address> findByUserUserIdAndIsDefaultTrue(String userId);

}