package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByUser(User user);

    Optional<Address> findByAddressIdAndUser(UUID addressId, User user);

    boolean existsByUserAndIsDefaultTrue(User user);
}
