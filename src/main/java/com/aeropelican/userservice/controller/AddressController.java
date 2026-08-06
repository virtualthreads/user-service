package com.aeropelican.userservice.controller;
import com.aeropelican.userservice.dto.request.AddressCreateRequestDTO;
import com.aeropelican.userservice.dto.request.AddressUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.AddressResponseDTO;
import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AddressController {

    private final AddressService addressService;
    //To Create Address
    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> createAddress(
            @PathVariable UUID userId,
            @Valid @RequestBody AddressCreateRequestDTO requestDTO) {

        log.info("Create Address request received for User ID : {}", userId);

        AddressResponseDTO response = addressService.createAddress(userId, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AddressResponseDTO>builder()
                        .success(true)
                        .message("Address created successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
     //To Get Address By Id
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> getAddressById(
            @PathVariable UUID addressId) {

        log.info("Get Address request received : {}", addressId);

        AddressResponseDTO response = addressService.getAddressById(addressId);

        return ResponseEntity.ok(
                ApiResponse.<AddressResponseDTO>builder()
                        .success(true)
                        .message("Address fetched successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
     //To Get User Addresses
    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> getUserAddresses(
            @PathVariable UUID userId) {

        log.info("Get User Addresses request received for User ID : {}", userId);

        List<AddressResponseDTO> response = addressService.getUserAddresses(userId);

        return ResponseEntity.ok(
                ApiResponse.<List<AddressResponseDTO>>builder()
                        .success(true)
                        .message("Addresses fetched successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
    //To Update Address
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> updateAddress(
            @PathVariable UUID addressId,
            @Valid @RequestBody AddressUpdateRequestDTO requestDTO) {

        log.info("Update Address request received : {}", addressId);

        AddressResponseDTO response =
                addressService.updateAddress(addressId, requestDTO);

        return ResponseEntity.ok(
                ApiResponse.<AddressResponseDTO>builder()
                        .success(true)
                        .message("Address updated successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    //To Set Default Address
    @PatchMapping("/addresses/{addressId}/default")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> setDefaultAddress(
            @PathVariable UUID addressId) {

        log.info("Set Default Address request received : {}", addressId);

        AddressResponseDTO response =
                addressService.setDefaultAddress(addressId);

        return ResponseEntity.ok(
                ApiResponse.<AddressResponseDTO>builder()
                        .success(true)
                        .message("Default address updated successfully.")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
    //To Delete Address
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable UUID addressId) {

        log.info("Delete Address request received : {}", addressId);

        addressService.deleteAddress(addressId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Address deleted successfully.")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

}