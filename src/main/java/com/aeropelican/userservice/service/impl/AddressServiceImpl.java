package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateAddressRequest;
import com.aeropelican.userservice.dto.request.UpdateAddressRequest;
import com.aeropelican.userservice.dto.response.AddressResponse;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exception.BusinessException;
import com.aeropelican.userservice.exception.InvalidRequestException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.AddressMapper;
import com.aeropelican.userservice.repository.AddressRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressResponse createAddress(UUID userId, CreateAddressRequest request) {
        log.info("Creating address for user {}", userId);
        if (userId == null) throw new InvalidRequestException("User id is required");
        if (request == null) throw new InvalidRequestException("Request body is required");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Address address = addressMapper.toEntity(request, user);
        if (Boolean.TRUE.equals(request.isDefault())) {
            addressRepository.findByUser(user).stream()
                    .filter(a -> Boolean.TRUE.equals(a.getIsDefault()))
                    .forEach(a -> a.setIsDefault(false));
            address.setIsDefault(true);
        }

        Address saved = addressRepository.save(address);
        return addressMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "Get Address", key = "#addressId")
    public AddressResponse getAddressById(UUID addressId) {
        if (addressId == null) throw new InvalidRequestException("Address id is required");
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "Get User Addresses", key = "#userId")
    public List<AddressResponse> getUserAddresses(UUID userId) {
        if (userId == null) throw new InvalidRequestException("User id is required");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return addressRepository.findByUser(user).stream().map(addressMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(UUID userId, UUID addressId, UpdateAddressRequest request) {
        log.info("Updating address {} for user {}", addressId, userId);
        if (userId == null) throw new InvalidRequestException("User id is required");
        if (addressId == null) throw new InvalidRequestException("Address id is required");
        if (request == null) throw new InvalidRequestException("Request body is required");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Address address = addressRepository.findByAddressIdAndUser(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        addressMapper.updateEntityFromDto(request, address);
        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public AddressResponse setDefaultAddress(UUID userId, UUID addressId) {
        if (userId == null) throw new InvalidRequestException("User id is required");
        if (addressId == null) throw new InvalidRequestException("Address id is required");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Address address = addressRepository.findByAddressIdAndUser(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        addressRepository.findByUser(user).stream()
                .filter(a -> !a.getAddressId().equals(addressId) && Boolean.TRUE.equals(a.getIsDefault()))
                .forEach(a -> a.setIsDefault(false));

        address.setIsDefault(true);
        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        if (userId == null) throw new InvalidRequestException("User id is required");
        if (addressId == null) throw new InvalidRequestException("Address id is required");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Address address = addressRepository.findByAddressIdAndUser(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (Boolean.TRUE.equals(address.getIsDefault()) && addressRepository.findByUser(user).size() == 1) {
            throw new BusinessException("Default address cannot be deleted when it's the only address");
        }

        addressRepository.delete(address);
    }
}
