package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.AddressResponse;
import com.aeropelican.userservice.dto.CreateAddressRequest;
import com.aeropelican.userservice.dto.UpdateAddressRequest;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exception.ResourceInUseException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.AddressMapper;
import com.aeropelican.userservice.repository.AddressRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public AddressResponse createAddress(UUID userId, CreateAddressRequest request) {
        logger.info("Creating new address for user ID: {}", userId);

        User user = userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)
                .orElseThrow(() -> {
                    logger.warn("Address creation failed: Active user not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        if (Boolean.TRUE.equals(request.isDefault())) {
            logger.debug("Clearing existing default addresses for user ID: {}", userId);
            addressRepository.clearDefaultAddressesForUser(userId);
        }

        Address addressEntity = addressMapper.toEntity(request, user);
        Address savedAddress = addressRepository.save(addressEntity);

        logger.info("Successfully created address with ID: {} for user ID: {}", savedAddress.getAddressId(), userId);
        return addressMapper.toResponse(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(UUID addressId) {
        logger.debug("Fetching address details for addressId: {}", addressId);
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.warn("Address search failed: Address not found with ID: {}", addressId);
                    return new ResourceNotFoundException("Address not found with id: " + addressId);
                });
        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getUserAddresses(UUID userId) {
        logger.debug("Fetching addresses for user ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            logger.warn("Fetch addresses failed: User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return addressRepository.findByUser_UserId(userId).stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Override
    public AddressResponse updateAddress(UUID addressId, UpdateAddressRequest request) {
        logger.info("Updating address ID: {}", addressId);

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.warn("Address update failed: Address not found with ID: {}", addressId);
                    return new ResourceNotFoundException("Address not found with id: " + addressId);
                });

        UUID userId = existingAddress.getUser().getUserId();

        if (Boolean.TRUE.equals(request.isDefault())) {
            logger.debug("Clearing existing default addresses for user ID: {}", userId);
            addressRepository.clearDefaultAddressesForUser(userId);
        }

        addressMapper.updateEntityFromDto(request, existingAddress);
        Address updatedAddress = addressRepository.save(existingAddress);

        logger.info("Successfully updated address ID: {}", addressId);
        return addressMapper.toResponse(updatedAddress);
    }

    @Override
    public AddressResponse setDefaultAddress(UUID addressId) {
        logger.info("Setting address ID: {} as default address", addressId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.warn("Set default address failed: Address not found with ID: {}", addressId);
                    return new ResourceNotFoundException("Address not found with id: " + addressId);
                });

        UUID userId = address.getUser().getUserId();
        addressRepository.clearDefaultAddressesForUser(userId);

        address.setIsDefault(true);
        Address updatedAddress = addressRepository.save(address);

        logger.info("Successfully set address ID: {} as default for user ID: {}", addressId, userId);
        return addressMapper.toResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(UUID addressId) {
        logger.info("Request received to delete address ID: {}", addressId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.warn("Address delete failed: Address not found with ID: {}", addressId);
                    return new ResourceNotFoundException("Address not found with id: " + addressId);
                });

        UUID userId = address.getUser().getUserId();

        if (Boolean.TRUE.equals(address.getIsDefault()) && addressRepository.countByUser_UserId(userId) > 1) {
            logger.warn("Address delete conflict: Default address cannot be deleted when other addresses exist for user ID: {}", userId);
            throw new ResourceInUseException("Cannot delete default address when other addresses exist. Please set another address as default first.");
        }

        addressRepository.delete(address);
        logger.info("Successfully deleted address ID: {}", addressId);
    }
}
