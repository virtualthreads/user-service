package com.aeropelican.userservice.dto.Response;

import com.aeropelican.userservice.entity.enums.AddressType;

public record AddressResponse(
        String addressId,
        String userId,
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
        Boolean isDefault
) {}