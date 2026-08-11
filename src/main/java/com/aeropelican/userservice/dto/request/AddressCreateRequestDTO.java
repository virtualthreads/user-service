package com.aeropelican.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AddressCreateRequestDTO(

        @NotBlank(message = "User Id is required")
        String userId,

        @NotBlank(message = "Address type is required")
        String addressType,

        @NotBlank(message = "Recipient name is required")
        String recipientName,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        @NotBlank(message = "Address Line 1 is required")
        String addressLine1,

        String addressLine2,

        String landmark,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "State is required")
        String state,

        @NotBlank(message = "Country is required")
        String country,

        @NotBlank(message = "Postal code is required")
        String postalCode,

        BigDecimal latitude,

        BigDecimal longitude,

        @NotNull(message = "Default address flag is required")
        Boolean isDefault

) {
}