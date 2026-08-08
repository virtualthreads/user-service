package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateAddressRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateAddressRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exception.AddressNotFoundException;
import com.aeropelican.userservice.exception.UserNotFoundException;
import com.aeropelican.userservice.repository.AddressRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    // =========================================================
    // CREATE ADDRESS
    // =========================================================

    @Override
    public AddressResponseDTO createAddress(
            UUID userId,
            CreateAddressRequestDTO request) {

        log.info(
                "Creating address for user: {}",
                userId
        );

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {

                    log.warn(
                            "User not found while creating address: userId={}",
                            userId
                    );

                    return new UserNotFoundException(
                            "User not found with ID: " + userId
                    );
                });

        Address address = new Address();

        address.setUser(user);
        address.setAddressType(request.getAddressType());
        address.setRecipientName(request.getRecipientName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());

        boolean makeDefault =
                Boolean.TRUE.equals(request.getIsDefault());

        if (makeDefault) {

            removeExistingDefaultAddress(userId);

            address.setIsDefault(true);

        } else {

            address.setIsDefault(false);
        }

        Address savedAddress =
                addressRepository.save(address);

        log.info(
                "Address created successfully: addressId={}, userId={}",
                savedAddress.getAddressId(),
                userId
        );

        return toResponse(savedAddress);
    }

    // =========================================================
    // GET ADDRESS
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public AddressResponseDTO getAddress(UUID addressId) {

        log.info(
                "Fetching address: {}",
                addressId
        );

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Address not found: addressId={}",
                                    addressId
                            );

                            return new AddressNotFoundException(
                                    "Address not found with ID: "
                                            + addressId
                            );
                        });

        return toResponse(address);
    }

    // =========================================================
    // GET USER ADDRESSES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDTO> getUserAddresses(
            UUID userId) {

        log.info(
                "Fetching addresses for user: {}",
                userId
        );

        userRepository.findById(userId)
                .orElseThrow(() -> {

                    log.warn(
                            "User not found while fetching addresses: " +
                                    "userId={}",
                            userId
                    );

                    return new UserNotFoundException(
                            "User not found with ID: "
                                    + userId
                    );
                });

        return addressRepository
                .findByUser_UserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // UPDATE ADDRESS
    // =========================================================

    @Override
    public AddressResponseDTO updateAddress(
            UUID addressId,
            UpdateAddressRequestDTO request) {

        log.info(
                "Updating address: {}",
                addressId
        );

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Address not found while updating: " +
                                            "addressId={}",
                                    addressId
                            );

                            return new AddressNotFoundException(
                                    "Address not found with ID: "
                                            + addressId
                            );
                        });

        address.setAddressType(request.getAddressType());
        address.setRecipientName(request.getRecipientName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());

        boolean makeDefault =
                Boolean.TRUE.equals(request.getIsDefault());

        UUID userId =
                address.getUser().getUserId();

        if (makeDefault) {

            removeExistingDefaultAddress(userId);

            address.setIsDefault(true);

        } else {

            address.setIsDefault(false);
        }

        Address updatedAddress =
                addressRepository.save(address);

        log.info(
                "Address updated successfully: addressId={}",
                addressId
        );

        return toResponse(updatedAddress);
    }

    // =========================================================
    // SET DEFAULT ADDRESS
    // =========================================================

    @Override
    public AddressResponseDTO setDefaultAddress(
            UUID addressId) {

        log.info(
                "Setting default address: {}",
                addressId
        );

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Address not found while setting default: " +
                                            "addressId={}",
                                    addressId
                            );

                            return new AddressNotFoundException(
                                    "Address not found with ID: "
                                            + addressId
                            );
                        });

        UUID userId =
                address.getUser().getUserId();

        removeExistingDefaultAddress(userId);

        address.setIsDefault(true);

        Address savedAddress =
                addressRepository.save(address);

        log.info(
                "Default address set successfully: addressId={}",
                addressId
        );

        return toResponse(savedAddress);
    }

    // =========================================================
    // DELETE ADDRESS
    // =========================================================

    @Override
    public void deleteAddress(UUID addressId) {

        log.info(
                "Deleting address: {}",
                addressId
        );

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Address not found while deleting: " +
                                            "addressId={}",
                                    addressId
                            );

                            return new AddressNotFoundException(
                                    "Address not found with ID: "
                                            + addressId
                            );
                        });

        /*
         * REST_Design.md allows either:
         *
         * 1. Reject deletion of the only/default address
         * 2. Allow deletion
         *
         * Current project behavior:
         * Allow deletion.
         *
         * We are keeping that behavior unless the business
         * requirement changes later.
         */

        addressRepository.delete(address);

        log.info(
                "Address deleted successfully: addressId={}",
                addressId
        );
    }

    // =========================================================
    // REMOVE EXISTING DEFAULT ADDRESS
    // =========================================================

    private void removeExistingDefaultAddress(
            UUID userId) {

        List<Address> defaultAddresses =
                addressRepository
                        .findByUser_UserIdAndIsDefaultTrue(
                                userId
                        );

        for (Address defaultAddress : defaultAddresses) {

            defaultAddress.setIsDefault(false);

            addressRepository.save(defaultAddress);
        }
    }

    // =========================================================
    // ENTITY -> RESPONSE DTO
    // =========================================================

    private AddressResponseDTO toResponse(
            Address address) {

        return AddressResponseDTO.builder()
                .addressId(address.getAddressId())
                .userId(address.getUser().getUserId())
                .addressType(address.getAddressType())
                .recipientName(address.getRecipientName())
                .phoneNumber(address.getPhoneNumber())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .isDefault(address.getIsDefault())
                .build();
    }
}