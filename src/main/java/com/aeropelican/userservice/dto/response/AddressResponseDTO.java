package com.aeropelican.userservice.dto.response;
import com.aeropelican.userservice.enums.AddressType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO {

    private UUID addressId;

    private String userId;

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

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Boolean isDefault;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
