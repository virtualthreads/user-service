package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;
import com.aeropelican.userservice.entity.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-10T15:25:43+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public Address toEntity(AddressCreateRequestDTO requestDTO) {
        if ( requestDTO == null ) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        address.userId( requestDTO.getUserId() );
        address.addressType( requestDTO.getAddressType() );
        address.recipientName( requestDTO.getRecipientName() );
        address.phoneNumber( requestDTO.getPhoneNumber() );
        address.addressLine1( requestDTO.getAddressLine1() );
        address.addressLine2( requestDTO.getAddressLine2() );
        address.landmark( requestDTO.getLandmark() );
        address.city( requestDTO.getCity() );
        address.state( requestDTO.getState() );
        address.country( requestDTO.getCountry() );
        address.postalCode( requestDTO.getPostalCode() );
        address.latitude( requestDTO.getLatitude() );
        address.longitude( requestDTO.getLongitude() );
        address.isDefault( requestDTO.getIsDefault() );

        return address.build();
    }

    @Override
    public AddressResponseDTO toResponseDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponseDTO addressResponseDTO = new AddressResponseDTO();

        addressResponseDTO.setAddressId( address.getAddressId() );
        addressResponseDTO.setUserId( address.getUserId() );
        addressResponseDTO.setAddressType( address.getAddressType() );
        addressResponseDTO.setRecipientName( address.getRecipientName() );
        addressResponseDTO.setPhoneNumber( address.getPhoneNumber() );
        addressResponseDTO.setAddressLine1( address.getAddressLine1() );
        addressResponseDTO.setAddressLine2( address.getAddressLine2() );
        addressResponseDTO.setLandmark( address.getLandmark() );
        addressResponseDTO.setCity( address.getCity() );
        addressResponseDTO.setState( address.getState() );
        addressResponseDTO.setCountry( address.getCountry() );
        addressResponseDTO.setPostalCode( address.getPostalCode() );
        addressResponseDTO.setLatitude( address.getLatitude() );
        addressResponseDTO.setLongitude( address.getLongitude() );
        addressResponseDTO.setIsDefault( address.getIsDefault() );
        addressResponseDTO.setCreatedAt( address.getCreatedAt() );
        addressResponseDTO.setUpdatedAt( address.getUpdatedAt() );

        return addressResponseDTO;
    }

    @Override
    public void updateEntity(AddressUpdateRequestDTO requestDTO, Address address) {
        if ( requestDTO == null ) {
            return;
        }

        address.setUserId( requestDTO.getUserId() );
        address.setAddressType( requestDTO.getAddressType() );
        address.setRecipientName( requestDTO.getRecipientName() );
        address.setPhoneNumber( requestDTO.getPhoneNumber() );
        address.setAddressLine1( requestDTO.getAddressLine1() );
        address.setAddressLine2( requestDTO.getAddressLine2() );
        address.setLandmark( requestDTO.getLandmark() );
        address.setCity( requestDTO.getCity() );
        address.setState( requestDTO.getState() );
        address.setCountry( requestDTO.getCountry() );
        address.setPostalCode( requestDTO.getPostalCode() );
        address.setLatitude( requestDTO.getLatitude() );
        address.setLongitude( requestDTO.getLongitude() );
        address.setIsDefault( requestDTO.getIsDefault() );
    }
}
