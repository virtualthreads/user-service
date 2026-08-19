package com.aeropelican.userservice.dto.request;
import com.aeropelican.userservice.enums.AddressType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressUpdateRequestDTO {

    @NotNull(message = "Address type is required")
    private AddressType addressType;

    @NotBlank(message = "Recipient name is required")
    @Size(max = 150, message = "Recipient name cannot exceed 150 characters")
    private String recipientName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian mobile number"
    )
    private String phoneNumber;

    @NotBlank(message = "Address Line 1 is required")
    @Size(max = 255, message = "Address Line 1 cannot exceed 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address Line 2 cannot exceed 255 characters")
    private String addressLine2;

    @Size(max = 255, message = "Landmark cannot exceed 255 characters")
    private String landmark;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @NotBlank(message = "Postal code is required")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Postal code must be a valid 6-digit PIN code"
    )
    private String postalCode;

    @Digits(integer = 2, fraction = 8,
            message = "Latitude must have up to 2 integer digits and 8 decimal places")
    @DecimalMin(value = "-90.00000000", message = "Latitude must be greater than or equal to -90")
    @DecimalMax(value = "90.00000000", message = "Latitude must be less than or equal to 90")
    private BigDecimal latitude;

    @Digits(integer = 3, fraction = 8,
            message = "Longitude must have up to 3 integer digits and 8 decimal places")
    @DecimalMin(value = "-180.00000000", message = "Longitude must be greater than or equal to -180")
    @DecimalMax(value = "180.00000000", message = "Longitude must be less than or equal to 180")
    private BigDecimal longitude;

    private Boolean isDefault;
}