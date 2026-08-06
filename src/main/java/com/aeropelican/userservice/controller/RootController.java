package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.Map;
@Hidden

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, String>>> rootHealthCheck() {
        Map<String, String> status = Map.of(
                "service", "User Service API",
                "status", "UP",
                "version", "1.0.0"
        );
        return ResponseEntity.ok(ApiResponse.success("Service is running", status));
    }
}