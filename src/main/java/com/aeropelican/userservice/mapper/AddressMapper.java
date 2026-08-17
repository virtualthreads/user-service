package com.aeropelican.userservice.mapper;
import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;
import com.aeropelican.userservice.entity.Address;
import java.util.UUID;
public class AddressMapper {

    private AddressMapper() {
    }
    public static Address toEntity(AddressCreateRequestDTO requestDTO, UUID userId) {

        return Address.builder()
                .addressId(UUID.randomUUID())
                .userId(userId)
                .addressType(requestDTO.getAddressType())
                .recipientName(requestDTO.getRecipientName())
                .phoneNumber(requestDTO.getPhoneNumber())
                .addressLine1(requestDTO.getAddressLine1())
                .addressLine2(requestDTO.getAddressLine2())
                .landmark(requestDTO.getLandmark())
                .city(requestDTO.getCity())
                .state(requestDTO.getState())
                .country(requestDTO.getCountry())
                .postalCode(requestDTO.getPostalCode())
                .latitude(requestDTO.getLatitude())
                .longitude(requestDTO.getLongitude())
                .isDefault(requestDTO.getIsDefault())
                .build();
    }
    public static void updateEntity(Address entity, AddressCreateRequestDTO requestDTO) {

        entity.setAddressType(requestDTO.getAddressType());
        entity.setRecipientName(requestDTO.getRecipientName());
        entity.setPhoneNumber(requestDTO.getPhoneNumber());
        entity.setAddressLine1(requestDTO.getAddressLine1());
        entity.setAddressLine2(requestDTO.getAddressLine2());
        entity.setLandmark(requestDTO.getLandmark());
        entity.setCity(requestDTO.getCity());
        entity.setState(requestDTO.getState());
        entity.setCountry(requestDTO.getCountry());
        entity.setPostalCode(requestDTO.getPostalCode());
        entity.setLatitude(requestDTO.getLatitude());
        entity.setLongitude(requestDTO.getLongitude());

        if (requestDTO.getIsDefault() != null) {
            entity.setIsDefault(requestDTO.getIsDefault());
        }
    }
    public static AddressResponseDTO toResponseDTO(Address entity) {

        return AddressResponseDTO.builder()
                .addressId(entity.getAddressId())
                .userId(entity.getUserId())
                .addressType(entity.getAddressType())
                .recipientName(entity.getRecipientName())
                .phoneNumber(entity.getPhoneNumber())
                .addressLine1(entity.getAddressLine1())
                .addressLine2(entity.getAddressLine2())
                .landmark(entity.getLandmark())
                .city(entity.getCity())
                .state(entity.getState())
                .country(entity.getCountry())
                .postalCode(entity.getPostalCode())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .isDefault(entity.getIsDefault())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}