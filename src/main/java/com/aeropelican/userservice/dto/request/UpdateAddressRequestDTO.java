package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressRequestDTO {

    @NotNull(message = "Address type is required")
    private AddressType addressType;

    @NotBlank(message = "Recipient name is required")
    @Size(max = 150)
    private String recipientName;

    @Size(max = 20)
    @Pattern(
            regexp = "^[0-9+\\- ]*$",
            message = "Invalid phone number"
    )
    private String phoneNumber;

    @NotBlank(message = "Address line 1 is required")
    @Size(max = 255)
    private String addressLine1;

    @Size(max = 255)
    private String addressLine2;

    @Size(max = 255)
    private String landmark;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100)
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100)
    private String country;

    @NotBlank(message = "Postal code is required")
    @Size(max = 20)
    private String postalCode;

    private Double latitude;

    private Double longitude;

    private Boolean isDefault;
}