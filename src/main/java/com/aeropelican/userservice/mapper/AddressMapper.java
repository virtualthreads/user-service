package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponse;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class AddressMapper {

    private AddressMapper() {
    }

    public static Address toEntity(
            AddressCreateRequestDTO request,
            User user) {

        return Address.builder()
                .addressId(UUID.randomUUID().toString())
                .user(user)
                .addressType(request.addressType())
                .recipientName(request.recipientName())
                .phoneNumber(request.phoneNumber())
                .addressLine1(request.addressLine1())
                .addressLine2(request.addressLine2())
                .landmark(request.landmark())
                .city(request.city())
                .state(request.state())
                .country(request.country())
                .postalCode(request.postalCode())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .isDefault(request.isDefault())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static AddressResponse toResponse(Address address) {

        return new AddressResponse(
                address.getAddressId(),
                address.getUser().getUserId(),
                address.getAddressType(),
                address.getRecipientName(),
                address.getPhoneNumber(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getLandmark(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode(),
                address.getLatitude(),
                address.getLongitude(),
                address.getIsDefault(),
                address.getCreatedAt(),
                address.getUpdatedAt()
        );
    }

    public static void updateEntity(
            Address address,
            AddressUpdateRequestDTO request) {

        address.setAddressType(request.addressType());
        address.setRecipientName(request.recipientName());
        address.setPhoneNumber(request.phoneNumber());
        address.setAddressLine1(request.addressLine1());
        address.setAddressLine2(request.addressLine2());
        address.setLandmark(request.landmark());
        address.setCity(request.city());
        address.setState(request.state());
        address.setCountry(request.country());
        address.setPostalCode(request.postalCode());
        address.setLatitude(request.latitude());
        address.setLongitude(request.longitude());
        address.setIsDefault(request.isDefault());
        address.setUpdatedAt(LocalDateTime.now());
    }
}