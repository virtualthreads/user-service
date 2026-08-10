package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;
import com.aeropelican.userservice.service.AddressService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // CREATE
    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(
            @Valid @RequestBody AddressCreateRequestDTO requestDTO) {

        AddressResponseDTO response =
                addressService.createAddress(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {

        return ResponseEntity.ok(
                addressService.getAllAddresses()
        );
    }

    // GET BY ID
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> getAddressById(
            @PathVariable String addressId) {

        return ResponseEntity.ok(
                addressService.getAddressById(addressId)
        );
    }

    // GET BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> getAddressesByUserId(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                addressService.getAddressesByUserId(userId)
        );
    }

    // GET DEFAULT ADDRESS
    @GetMapping("/user/{userId}/default")
    public ResponseEntity<List<AddressResponseDTO>> getDefaultAddresses(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                addressService.getDefaultAddresses(userId)
        );
    }

    // UPDATE
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable String addressId,
            @Valid @RequestBody AddressUpdateRequestDTO requestDTO) {

        return ResponseEntity.ok(
                addressService.updateAddress(
                        addressId,
                        requestDTO
                )
        );
    }

    // DELETE
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable String addressId) {

        addressService.deleteAddress(addressId);

        return ResponseEntity.noContent().build();
    }
}
