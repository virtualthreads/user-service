package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.AddressResponse;
import com.aeropelican.userservice.dto.CreateAddressRequest;
import com.aeropelican.userservice.dto.UpdateAddressRequest;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AddressMapper {

    private static final Logger logger = LoggerFactory.getLogger(AddressMapper.class);

    public Address toEntity(CreateAddressRequest request, User user) {
        logger.debug("Mapping CreateAddressRequest to Address entity for userId: {}", user != null ? user.getUserId() : null);
        Address address = new Address();
        address.setUser(user);
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
        address.setIsDefault(Boolean.TRUE.equals(request.isDefault()));
        return address;
    }

    public void updateEntityFromDto(UpdateAddressRequest request, Address address) {
        logger.debug("Updating Address entity (addressId: {}) from UpdateAddressRequest", address.getAddressId());
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
        if (request.isDefault() != null) {
            address.setIsDefault(request.isDefault());
        }
        address.setUpdatedAt(LocalDateTime.now());
    }

    public AddressResponse toResponse(Address address) {
        if (address == null) {
            logger.debug("Attempted to map null Address entity to AddressResponse");
            return null;
        }
        logger.debug("Mapping Address entity to AddressResponse for addressId: {}", address.getAddressId());
        return new AddressResponse(
                address.getAddressId(),
                address.getUser() != null ? address.getUser().getUserId() : null,
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
}
