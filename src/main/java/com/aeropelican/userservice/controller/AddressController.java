package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponse;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse> createAddress(
            @Valid @RequestBody AddressCreateRequestDTO request) {

        log.info("Received request to create address");

        AddressResponse response =
                addressService.createAddress(request);

        log.info("Address created successfully with id: {}",
                response.addressId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(
                        true,
                        "Address created successfully",
                        response
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAddressById(
            @PathVariable String id) {

        log.info("Fetching address with id: {}", id);

        AddressResponse response =
                addressService.getAddressById(id);

        log.info("Address fetched successfully");

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Address fetched successfully",
                        response
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAddresses(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size) {

        log.info("Fetching addresses - Page: {}, Size: {}",
                page, size);

        Page<AddressResponse> response =
                addressService.getAllAddresses(page, size);

        log.info("Fetched {} addresses",
                response.getNumberOfElements());

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Addresses fetched successfully",
                        response
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAddress(
            @PathVariable String id,
            @Valid @RequestBody AddressUpdateRequestDTO request) {

        log.info("Updating address with id: {}", id);

        AddressResponse response =
                addressService.updateAddress(id, request);

        log.info("Address updated successfully");

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Address updated successfully",
                        response
                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAddress(
            @PathVariable String id) {

        log.info("Deleting address with id: {}", id);

        addressService.deleteAddress(id);

        log.info("Address deleted successfully");

        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Address deleted successfully",
                        null
                ));
    }
}