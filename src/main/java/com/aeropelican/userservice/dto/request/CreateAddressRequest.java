package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.entity.AddressType;

public record CreateAddressRequest(
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