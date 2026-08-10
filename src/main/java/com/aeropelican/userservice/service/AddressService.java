package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.exceptions.ResourceNotFoundException;
import com.aeropelican.userservice.mapper.AddressMapper;
import com.aeropelican.userservice.repository.AddressRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    // CREATE
    public AddressResponseDTO createAddress(
            AddressCreateRequestDTO requestDTO) {

        log.info("Creating address for user: {}",
                requestDTO.getUserId());

        Address address = addressMapper.toEntity(requestDTO);

        Address savedAddress = addressRepository.save(address);

        log.info("Address created successfully with id: {}",
                savedAddress.getAddressId());

        return addressMapper.toResponseDTO(savedAddress);
    }

    // GET ALL
    public List<AddressResponseDTO> getAllAddresses() {

        log.info("Fetching all addresses");

        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toResponseDTO)
                .toList();
    }

    // GET BY ID
    public AddressResponseDTO getAddressById(String addressId) {

        log.info("Fetching address with id: {}", addressId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: " + addressId
                        ));

        return addressMapper.toResponseDTO(address);
    }

    // GET BY USER ID
    public List<AddressResponseDTO> getAddressesByUserId(
            String userId) {

        log.info("Fetching addresses for user: {}", userId);

        return addressRepository.findByUserId(userId)
                .stream()
                .map(addressMapper::toResponseDTO)
                .toList();
    }

    // GET DEFAULT ADDRESS
    public List<AddressResponseDTO> getDefaultAddresses(
            String userId) {

        log.info("Fetching default address for user: {}", userId);

        return addressRepository
                .findByUserIdAndIsDefaultTrue(userId)
                .stream()
                .map(addressMapper::toResponseDTO)
                .toList();
    }

    // UPDATE
    public AddressResponseDTO updateAddress(
            String addressId,
            AddressUpdateRequestDTO requestDTO) {

        log.info("Updating address with id: {}", addressId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: " + addressId
                        ));

        addressMapper.updateEntity(requestDTO, address);

        Address updatedAddress = addressRepository.save(address);

        log.info("Address updated successfully with id: {}",
                addressId);

        return addressMapper.toResponseDTO(updatedAddress);
    }

    // DELETE
    public void deleteAddress(String addressId) {

        log.info("Deleting address with id: {}", addressId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: " + addressId
                        ));

        addressRepository.delete(address);

        log.info("Address deleted successfully with id: {}",
                addressId);
    }
}