package com.aeropelican.userservice.repository;
import com.aeropelican.userservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

    @Repository
    public interface AddressRepository extends JpaRepository<Address, UUID> {
        List<Address> findByUserId(UUID userId);
        Optional<Address> findByAddressIdAndUserId(UUID addressId, UUID userId);
        boolean existsByAddressIdAndUserId(UUID addressId, UUID userId);
        Optional<Address> findByUserIdAndIsDefaultTrue(UUID userId);
        long countByUserId(UUID userId);


}
