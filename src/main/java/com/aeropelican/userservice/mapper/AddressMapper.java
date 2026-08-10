package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;
import com.aeropelican.userservice.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressCreateRequestDTO requestDTO);

    AddressResponseDTO toResponseDTO(Address address);

    void updateEntity(
            AddressUpdateRequestDTO requestDTO,
            @MappingTarget Address address
    );
}