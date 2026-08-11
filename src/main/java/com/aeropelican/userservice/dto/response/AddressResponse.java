package com.aeropelican.userservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddressResponse(

        String addressId,

        String userId,

        String addressType,

        String recipientName,

        String phoneNumber,

        String addressLine1,

        String addressLine2,

        String landmark,

        String city,

        String state,

        String country,

        String postalCode,

        BigDecimal latitude,

        BigDecimal longitude,

        Boolean isDefault,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}