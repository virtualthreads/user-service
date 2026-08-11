package com.aeropelican.userservice.service;
import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exceptions.AddressNotFound;
import com.aeropelican.userservice.exceptions.AddressOwnershipException;
import com.aeropelican.userservice.exceptions.DefaultAddressDeletionException;
import com.aeropelican.userservice.exceptions.UserNotFound;
import com.aeropelican.userservice.mapper.AddressMapper;
import com.aeropelican.userservice.repository.AddressRepository;
import com.aeropelican.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    public AddressResponseDTO createAddress(UUID userId,
                                            AddressCreateRequestDTO requestDTO) {

        log.info("Create Address API called for User ID : {}", userId);

        // Check whether user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID : {}", userId);
                    return new UserNotFound(
                            "User not found with ID : " + userId);
                });

        log.info("User found successfully : {}", userId);
        if (Boolean.TRUE.equals(requestDTO.getIsDefault())) {

            addressRepository.findByUserIdAndIsDefaultTrue(userId)
                    .ifPresent(existingDefault -> {

                        existingDefault.setIsDefault(false);

                        addressRepository.save(existingDefault);

                        log.info("Previous default address updated to false. Address ID : {}",
                                existingDefault.getAddressId());
                    });
        }

        Address address = AddressMapper.toEntity(requestDTO, user.getUserId());

        Address savedAddress = addressRepository.save(address);

        log.info("Address created successfully with Address ID : {}",
                savedAddress.getAddressId());

        return AddressMapper.toResponseDTO(savedAddress);
    }
     //To Get Address By Address ID
    @Transactional(readOnly = true)
    @Cacheable(value="Get Address",key = "#addressId")
    public AddressResponseDTO getAddressById(UUID addressId) {
        log.info("Get Address API called for Address ID : {}", addressId);
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.error("Address not found with ID : {}", addressId);
                    return new AddressNotFound(
                            "Address not found with ID : " + addressId);
                });

        log.info("Address fetched successfully with ID : {}", addressId);

        return AddressMapper.toResponseDTO(address);
    }
     //To Get All Addresses of a User
    @Transactional(readOnly = true)
    @Cacheable(value="Get list of Addresses",key = "#userId")
    public List<AddressResponseDTO> getUserAddresses(UUID userId) {

        log.info("Get User Addresses API called for User ID : {}", userId);

        // Check if User Exists
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID : {}", userId);
                    return new UserNotFound(
                            "User not found with ID : " + userId);
                });

        List<Address> addresses = addressRepository.findByUserId(userId);

        log.info("Total {} address(es) found for User ID : {}",
                addresses.size(), userId);

        return addresses.stream()
                .map(AddressMapper::toResponseDTO)
                .toList();
    }
     //To Update Address
    public AddressResponseDTO updateAddress(UUID addressId,
                                            AddressUpdateRequestDTO requestDTO) {
        log.info("Update Address API called for Address ID : {}", addressId);

        // Check whether address exists
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.error("Address not found with ID : {}", addressId);
                    return new AddressNotFound(
                            "Address not found with ID : " + addressId);
                });
        if (Boolean.TRUE.equals(requestDTO.getIsDefault())) {

            addressRepository.findByUserIdAndIsDefaultTrue(address.getUserId())
                    .ifPresent(existingDefault -> {

                        if (!existingDefault.getAddressId()
                                .equals(address.getAddressId())) {

                            existingDefault.setIsDefault(false);

                            addressRepository.save(existingDefault);

                            log.info("Previous default address updated to false : {}",
                                    existingDefault.getAddressId());
                        }
                    });
        }

        // Update entity
        address.setAddressType(requestDTO.getAddressType());
        address.setRecipientName(requestDTO.getRecipientName());
        address.setPhoneNumber(requestDTO.getPhoneNumber());
        address.setAddressLine1(requestDTO.getAddressLine1());
        address.setAddressLine2(requestDTO.getAddressLine2());
        address.setLandmark(requestDTO.getLandmark());
        address.setCity(requestDTO.getCity());
        address.setState(requestDTO.getState());
        address.setCountry(requestDTO.getCountry());
        address.setPostalCode(requestDTO.getPostalCode());
        address.setLatitude(requestDTO.getLatitude());
        address.setLongitude(requestDTO.getLongitude());

        if (requestDTO.getIsDefault() != null) {
            address.setIsDefault(requestDTO.getIsDefault());
        }

        Address updatedAddress = addressRepository.save(address);

        log.info("Address updated successfully : {}",
                updatedAddress.getAddressId());

        return AddressMapper.toResponseDTO(updatedAddress);
    }

//To Set Default Address
    public AddressResponseDTO setDefaultAddress(UUID addressId) {

        log.info("Set Default Address API called for Address ID : {}", addressId);

        // Check whether address exists
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.error("Address not found with ID : {}", addressId);
                    return new AddressNotFound(
                            "Address not found with ID : " + addressId);
                });

        UUID userId = address.getUserId();

        // Remove default from existing default address
        addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(defaultAddress -> {

                    if (!defaultAddress.getAddressId().equals(addressId)) {

                        defaultAddress.setIsDefault(false);

                        addressRepository.save(defaultAddress);

                        log.info("Previous default address removed : {}",
                                defaultAddress.getAddressId());
                    }
                });

        // Set selected address as default
        address.setIsDefault(true);

        Address updatedAddress = addressRepository.save(address);

        log.info("Default address set successfully : {}",
                updatedAddress.getAddressId());

        return AddressMapper.toResponseDTO(updatedAddress);
    }
    //To Delete Address
    public void deleteAddress(UUID addressId) {

        log.info("Delete Address API called for Address ID : {}", addressId);

        // Check whether address exists
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    log.error("Address not found with ID : {}", addressId);
                    return new AddressNotFound(
                            "Address not found with ID : " + addressId);
                });

        UUID userId = address.getUserId();
        long addressCount = addressRepository.countByUserId(userId);
        // Do not allow deleting the only default address
        if (Boolean.TRUE.equals(address.getIsDefault()) && addressCount == 1) {

            log.error("Cannot delete the only default address for User ID : {}", userId);

            throw new DefaultAddressDeletionException(
                    "Cannot delete the only default address.");
        }

        // If default address and multiple addresses exist
        if (Boolean.TRUE.equals(address.getIsDefault()) && addressCount > 1) {

            log.error("Cannot delete default address. Set another address as default first.");

            throw new DefaultAddressDeletionException(
                    "Please set another address as default before deleting this address.");
        }

        addressRepository.delete(address);

        log.info("Address deleted successfully. Address ID : {}", addressId);
    }

}