package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.response.ApiResponse;
import com.aeropelican.userservice.dto.response.UserAuthResponse;
import com.aeropelican.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final UserService userService;

    @GetMapping("/user/email")
    public ResponseEntity<ApiResponse<UserAuthResponse>> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.success(userService.findByEmail(email), "User fetched"));
    }

    @GetMapping("/ad")
    public String adminRole() {
        log.info("Admin endpoint triggered successfully");
        return "Success";
    }
}
