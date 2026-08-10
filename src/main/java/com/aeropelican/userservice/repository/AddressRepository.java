package com.aeropelican.userservice.repository;

import com.aeropelican.userservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    List<Address> findByUserId(String userId);

    List<Address> findByUserIdAndIsDefaultTrue(String userId);
}