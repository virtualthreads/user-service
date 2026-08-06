package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateAddressRequest;
import com.aeropelican.userservice.dto.response.AddressResponse;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.exception.ResourceInUseException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.AddressMapper;
import com.aeropelican.userservice.repository.AddressRepository;
import com.aeropelican.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    @Transactional
    public AddressResponse createAddress(String userId, CreateAddressRequest req) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        List<Address> userAddresses = addressRepository.findByUserId(userId);
        boolean setAsDefault = Boolean.TRUE.equals(req.isDefault()) || userAddresses.isEmpty();

        if (setAsDefault) {
            userAddresses.forEach(a -> {
                a.setIsDefault(false);
                addressRepository.save(a);
            });
        }

        Address address = addressMapper.toEntity(req, userId, setAsDefault);
        Address saved = addressRepository.save(address);
        return addressMapper.toResponse(saved);
    }

    public List<AddressResponse> getUserAddresses(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return addressRepository.findByUserId(userId).stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteAddress(String addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            throw new ResourceInUseException("Cannot delete default address directly. Mark another as default first.");
        }
        addressRepository.delete(address);
    }
}