package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    List<Address> findByUser_UserId(UUID userId);

    Optional<Address> findByAddressIdAndUser_UserId(
            UUID addressId,
            UUID userId
    );

    boolean existsByAddressIdAndUser_UserId(
            UUID addressId,
            UUID userId
    );

    List<Address> findByUser_UserIdAndIsDefaultTrue(
            UUID userId
    );
}