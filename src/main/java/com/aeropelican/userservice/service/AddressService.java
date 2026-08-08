package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateAddressRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateAddressRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    AddressResponseDTO createAddress(
            UUID userId,
            CreateAddressRequestDTO request
    );

    AddressResponseDTO getAddress(
            UUID addressId
    );

    List<AddressResponseDTO> getUserAddresses(
            UUID userId
    );

    AddressResponseDTO updateAddress(
            UUID addressId,
            UpdateAddressRequestDTO request
    );

    AddressResponseDTO setDefaultAddress(
            UUID addressId
    );

    void deleteAddress(
            UUID addressId
    );
}