package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.entity.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddressCreateRequestDTO {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Address type is required")
    private AddressType addressType;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    private String phoneNumber;

    @NotBlank(message = "Address line 1 is required")
    private String addressLine1;

    private String addressLine2;

    private String landmark;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Boolean isDefault;
}