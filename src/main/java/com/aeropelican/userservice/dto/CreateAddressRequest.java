package com.aeropelican.userservice.dto;

import com.aeropelican.userservice.entity.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAddressRequest(
        @NotNull(message = "Address type is mandatory")
        AddressType addressType,

        @NotBlank(message = "Recipient name is mandatory")
        String recipientName,

        String phoneNumber,

        @NotBlank(message = "Address line 1 is mandatory")
        String addressLine1,

        String addressLine2,

        String landmark,

        @NotBlank(message = "City is mandatory")
        String city,

        @NotBlank(message = "State is mandatory")
        String state,

        @NotBlank(message = "Country is mandatory")
        String country,

        @NotBlank(message = "Postal code is mandatory")
        String postalCode,

        Double latitude,

        Double longitude,

        Boolean isDefault
) {
}
