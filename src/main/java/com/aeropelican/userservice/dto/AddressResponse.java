package com.aeropelican.userservice.dto.response;

import com.aeropelican.userservice.entity.AddressType;

import java.time.LocalDateTime;
import java.util.UUID;

public record AddressResponse(
        UUID addressId,
        UUID userId,
        AddressType addressType,
        String recipientName,
        String phoneNumber,
        String addressLine1,
        String addressLine2,
        String landmark,
        String city,
        String state,
        String country,
        String postalCode,
        Double latitude,
        Double longitude,
        Boolean isDefault,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
