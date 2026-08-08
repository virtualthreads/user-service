package com.aeropelican.userservice.dto.response;

import com.aeropelican.userservice.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {

    private UUID addressId;

    private UUID userId;

    private AddressType addressType;

    private String recipientName;

    private String phoneNumber;

    private String addressLine1;

    private String addressLine2;

    private String landmark;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private Double latitude;

    private Double longitude;

    private Boolean isDefault;
}