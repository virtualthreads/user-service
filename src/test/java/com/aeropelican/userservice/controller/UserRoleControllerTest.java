package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.AssignRoleRequest;
import com.aeropelican.userservice.dto.RoleResponse;
import com.aeropelican.userservice.exception.GlobalExceptionHandler;
import com.aeropelican.userservice.service.UserRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRoleService userRoleService;

    @InjectMocks
    private UserRoleController userRoleController;

    private UUID userId;
    private UUID roleId;
    private RoleResponse roleResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userRoleController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        userId = UUID.randomUUID();
        roleId = UUID.randomUUID();
        roleResponse = new RoleResponse(roleId, "ADMIN", "Administrator", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void assignRole_Returns201Created() throws Exception {
        String jsonPayload = String.format("""
                {
                    "roleId": "%s"
                }
                """, roleId);

        when(userRoleService.assignRole(eq(userId), any(AssignRoleRequest.class))).thenReturn(roleResponse);

        mockMvc.perform(post("/api/v1/users/{userId}/roles", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.roleName").value("ADMIN"));
    }

    @Test
    void getUserRoles_Returns200OK() throws Exception {
        when(userRoleService.getUserRoles(userId)).thenReturn(List.of(roleResponse));

        mockMvc.perform(get("/api/v1/users/{userId}/roles", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].roleName").value("ADMIN"));
    }

    @Test
    void removeRole_Returns200OK() throws Exception {
        doNothing().when(userRoleService).removeRole(userId, roleId);

        mockMvc.perform(delete("/api/v1/users/{userId}/roles/{roleId}", userId, roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Role removed successfully"));
    }
}
