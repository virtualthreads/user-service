package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.CreateAddressRequest;
import com.aeropelican.userservice.dto.response.AddressResponse;
import com.aeropelican.userservice.entity.Address;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class AddressMapper {

    public Address toEntity(CreateAddressRequest request, String userId, boolean isDefault) {
        if (request == null) return null;

        Address address = new Address();
        address.setAddressId(UUID.randomUUID().toString());
        address.setUserId(userId);

        if (request.addressType() != null) {
            address.setAddressType(String.valueOf(request.addressType()));
        }

        address.setRecipientName(request.recipientName());
        address.setPhoneNumber(request.phoneNumber());
        address.setAddressLine1(request.addressLine1());
        address.setAddressLine2(request.addressLine2());
        address.setLandmark(request.landmark());
        address.setCity(request.city());
        address.setState(request.state());
        address.setCountry(request.country());
        address.setPostalCode(request.postalCode());

        if (request.latitude() != null) {
            address.setLatitude(BigDecimal.valueOf(request.latitude()));
        }
        if (request.longitude() != null) {
            address.setLongitude(BigDecimal.valueOf(request.longitude()));
        }

        address.setIsDefault(isDefault);

        return address;
    }

    public AddressResponse toResponse(Address address) {
        if (address == null) return null;

        return new AddressResponse(
                address.getAddressId(),
                address.getUserId(),
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
                address.getIsDefault()
        );
    }
}