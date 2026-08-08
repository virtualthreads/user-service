package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.request.CreateAddressRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateAddressRequestDTO;
import com.aeropelican.userservice.dto.response.APIResponse;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;
import com.aeropelican.userservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    // =========================================================
    // CREATE ADDRESS
    // POST /api/v1/users/{userId}/addresses
    // =========================================================

    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<
            APIResponse<AddressResponseDTO>> createAddress(
            @PathVariable UUID userId,
            @Valid @RequestBody CreateAddressRequestDTO request) {

        log.info(
                "Creating address for user: {}",
                userId
        );

        AddressResponseDTO response =
                addressService.createAddress(
                        userId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponse.success(
                                response,
                                "Address created successfully"
                        )
                );
    }

    // =========================================================
    // GET ADDRESS
    // GET /api/v1/addresses/{addressId}
    // =========================================================

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<
            APIResponse<AddressResponseDTO>> getAddress(
            @PathVariable UUID addressId) {

        log.info(
                "Fetching address: {}",
                addressId
        );

        AddressResponseDTO response =
                addressService.getAddress(addressId);

        return ResponseEntity.ok(
                APIResponse.success(
                        response,
                        "Address fetched successfully"
                )
        );
    }

    // =========================================================
    // GET USER ADDRESSES
    // GET /api/v1/users/{userId}/addresses
    // =========================================================

    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<
            APIResponse<List<AddressResponseDTO>>> getUserAddresses(
            @PathVariable UUID userId) {

        log.info(
                "Fetching addresses for user: {}",
                userId
        );

        List<AddressResponseDTO> response =
                addressService.getUserAddresses(userId);

        return ResponseEntity.ok(
                APIResponse.success(
                        response,
                        "User addresses fetched successfully"
                )
        );
    }

    // =========================================================
    // UPDATE ADDRESS
    // PUT /api/v1/addresses/{addressId}
    // =========================================================

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<
            APIResponse<AddressResponseDTO>> updateAddress(
            @PathVariable UUID addressId,
            @Valid @RequestBody UpdateAddressRequestDTO request) {

        log.info(
                "Updating address: {}",
                addressId
        );

        AddressResponseDTO response =
                addressService.updateAddress(
                        addressId,
                        request
                );

        return ResponseEntity.ok(
                APIResponse.success(
                        response,
                        "Address updated successfully"
                )
        );
    }

    // =========================================================
    // SET DEFAULT ADDRESS
    // PATCH /api/v1/addresses/{addressId}/default
    // =========================================================

    @PatchMapping("/addresses/{addressId}/default")
    public ResponseEntity<
            APIResponse<AddressResponseDTO>> setDefaultAddress(
            @PathVariable UUID addressId) {

        log.info(
                "Setting default address: {}",
                addressId
        );

        AddressResponseDTO response =
                addressService.setDefaultAddress(
                        addressId
                );

        return ResponseEntity.ok(
                APIResponse.success(
                        response,
                        "Default address set successfully"
                )
        );
    }

    // =========================================================
    // DELETE ADDRESS
    // DELETE /api/v1/addresses/{addressId}
    // =========================================================

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<APIResponse<Void>> deleteAddress(
            @PathVariable UUID addressId) {

        log.info(
                "Deleting address: {}",
                addressId
        );

        addressService.deleteAddress(addressId);

        return ResponseEntity.ok(
                APIResponse.success(
                        null,
                        "Address deleted successfully"
                )
        );
    }
}