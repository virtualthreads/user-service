package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponse;
import com.aeropelican.userservice.entity.Address;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exception.AddressNotFoundException;
import com.aeropelican.userservice.exception.UserNotFoundException;
import com.aeropelican.userservice.mapper.AddressMapper;
import com.aeropelican.userservice.repository.AddressRepository;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse createAddress(AddressCreateRequestDTO request) {

        log.info("Creating address for user: {}", request.userId());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + request.userId()));

        Address address = AddressMapper.toEntity(request, user);

        addressRepository.save(address);

        log.info("Address created successfully with id: {}",
                address.getAddressId());

        return AddressMapper.toResponse(address);
    }

    @Override
    public AddressResponse getAddressById(String id) {

        log.info("Fetching address with id: {}", id);

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new AddressNotFoundException(
                                "Address not found with id: " + id));

        log.info("Address fetched successfully");

        return AddressMapper.toResponse(address);
    }

    @Override
    public Page<AddressResponse> getAllAddresses(int page, int size) {

        log.info("Fetching addresses. Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<AddressResponse> response = addressRepository.findAll(pageable)
                .map(AddressMapper::toResponse);

        log.info("Fetched {} addresses",
                response.getNumberOfElements());

        return response;
    }

    @Override
    public AddressResponse updateAddress(
            String id,
            AddressUpdateRequestDTO request) {

        log.info("Updating address with id: {}", id);

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new AddressNotFoundException(
                                "Address not found with id: " + id));

        AddressMapper.updateEntity(address, request);

        addressRepository.save(address);

        log.info("Address updated successfully with id: {}",
                address.getAddressId());

        return AddressMapper.toResponse(address);
    }

    @Override
    public void deleteAddress(String id) {

        log.info("Deleting address with id: {}", id);

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new AddressNotFoundException(
                                "Address not found with id: " + id));

        addressRepository.delete(address);

        log.info("Address deleted successfully with id: {}", id);
    }
}