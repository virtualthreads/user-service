package com.aeropelican.userservice.controller;

import com.aeropelican.userservice.dto.CreateUserRequest;
import com.aeropelican.userservice.dto.PageResponse;
import com.aeropelican.userservice.dto.UpdateUserRequest;
import com.aeropelican.userservice.dto.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.UserResponse;
import com.aeropelican.userservice.dto.UserSearchRequest;
import com.aeropelican.userservice.entity.Gender;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.exception.GlobalExceptionHandler;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        userId = UUID.randomUUID();
        userResponse = new UserResponse(
                userId,
                "John",
                "Doe",
                "john@example.com",
                "9876543210",
                Gender.MALE,
                LocalDate.of(1995, 5, 20),
                true,
                true,
                Status.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void registerUser_Returns201Created() throws Exception {
        String jsonPayload = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john@example.com",
                    "phoneNumber": "9876543210",
                    "password": "Password123!",
                    "gender": "MALE",
                    "dateOfBirth": "1995-05-20"
                }
                """;

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    void registerUser_Returns400BadRequest_WhenValidationFails() throws Exception {
        String invalidJsonPayload = """
                {
                    "firstName": "",
                    "lastName": "Doe",
                    "email": "invalid-email",
                    "phoneNumber": "9876543210",
                    "password": "",
                    "gender": "MALE",
                    "dateOfBirth": "1995-05-20"
                }
                """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getUserById_Returns200OK() throws Exception {
        when(userService.getUserById(userId)).thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(userId.toString()))
                .andExpect(jsonPath("$.data.firstName").value("John"));
    }

    @Test
    void getUserById_Returns404NotFound_WhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(userId)).thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User not found with id: " + userId));
    }

    @Test
    void searchUsers_Returns200OK() throws Exception {
        String searchJsonPayload = """
                {
                    "keyword": "john",
                    "status": "ACTIVE",
                    "gender": "MALE",
                    "emailVerified": true,
                    "phoneVerified": true,
                    "page": 0,
                    "size": 10,
                    "sortBy": "firstName",
                    "sortDirection": "ASC"
                }
                """;

        PageResponse<UserResponse> pageResponse = new PageResponse<>(List.of(userResponse), 0, 10, 1, 1, true, true);

        when(userService.searchUsers(any(UserSearchRequest.class))).thenReturn(pageResponse);

        mockMvc.perform(post("/api/v1/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(searchJsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].firstName").value("John"));
    }

    @Test
    void updateUser_Returns200OK() throws Exception {
        String updateJsonPayload = """
                {
                    "firstName": "Johnathan",
                    "lastName": "Doe",
                    "email": "john@example.com",
                    "phoneNumber": "9876543210",
                    "gender": "MALE",
                    "dateOfBirth": "1995-05-20"
                }
                """;

        when(userService.updateUser(eq(userId), any(UpdateUserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(put("/api/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User updated successfully"));
    }

    @Test
    void updateUserStatus_Returns200OK() throws Exception {
        String statusJsonPayload = """
                {
                    "status": "LOCKED"
                }
                """;

        when(userService.updateUserStatus(eq(userId), any(UpdateUserStatusRequest.class))).thenReturn(userResponse);

        mockMvc.perform(patch("/api/v1/users/{userId}/status", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User status updated successfully"));
    }

    @Test
    void deleteUser_Returns200OK() throws Exception {
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }
}
