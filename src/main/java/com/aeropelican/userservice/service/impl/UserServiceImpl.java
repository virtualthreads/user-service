package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.request.UserSearchRequest;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exception.BusinessException;
import com.aeropelican.userservice.exception.InvalidRequestException;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.exception.ValidationException;
import com.aeropelican.userservice.mapper.UserMapper;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user for email: {}", request.email());
        validateCreateRequest(request);

        if (userRepository.existsByEmail(request.email())) {
            log.warn("User creation failed: email already exists {}", request.email());
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.email());
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = userMapper.toEntity(request, encodedPassword);
        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getUserId());
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        log.info("Fetching user by id: {}", userId);
        validateUserId(userId);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> searchUsers(UserSearchRequest request) {
        log.info("Searching users with request: {}", request);
        int page = request.page() == null ? 0 : request.page();
        int size = request.size() == null ? 10 : request.size();
        String sortBy = request.sortBy() == null || request.sortBy().isBlank() ? "firstName" : request.sortBy();
        Sort.Direction direction = request.sortDirection() == null || request.sortDirection() == com.aeropelican.userservice.dto.SortDirection.ASC
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Specification<User> specification = buildUserSpecification(request);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<User> userPage = userRepository.findAll(specification, pageable);

        List<UserResponse> content = userPage.getContent().stream()
                .map(userMapper::toResponse)
                .toList();

        return PageResponse.<UserResponse>builder()
                .content(content)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .first(userPage.isFirst())
                .last(userPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        log.info("Updating user {} with request {}", userId, request);
        validateUserId(userId);
        validateUpdateRequest(request);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (userRepository.existsByEmailAndUserIdNot(request.email(), userId)) {
            log.warn("User update failed: email already in use {}", request.email());
            throw new ResourceAlreadyExistsException("Email already in use: " + request.email());
        }

        userMapper.updateEntityFromDto(request, user);
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getUserId());
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUserStatus(UUID userId, UpdateUserStatusRequest request) {
        log.info("Updating status for user {} to {}", userId, request.status());
        validateUserId(userId);
        if (request.status() == null) {
            throw new ValidationException("Status is mandatory");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setStatus(request.status());
        User updatedUser = userRepository.save(user);
        log.info("User status updated successfully for id: {}", updatedUser.getUserId());
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        log.info("Soft deleting user {}", userId);
        validateUserId(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (user.getStatus() == Status.DELETED) {
            log.warn("Delete failed: user already deleted {}", userId);
            throw new BusinessException("User is already deleted");
        }

        user.setStatus(Status.DELETED);
        userRepository.save(user);
        log.info("User soft deleted successfully: {}", userId);
    }

    private void validateCreateRequest(CreateUserRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Request body is required");
        }
        if (request.firstName() == null || request.firstName().isBlank()) {
            throw new ValidationException("First name is mandatory");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new ValidationException("Email is mandatory");
        }
        if (request.password() == null || request.password().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }
        if (request.dateOfBirth() != null && request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be a future date");
        }
        if (request.gender() == null) {
            throw new ValidationException("Gender is mandatory");
        }
    }

    private void validateUpdateRequest(UpdateUserRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Request body is required");
        }
        if (request.firstName() == null || request.firstName().isBlank()) {
            throw new ValidationException("First name is mandatory");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new ValidationException("Email is mandatory");
        }
        if (request.dateOfBirth() != null && request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be a future date");
        }
        if (request.gender() == null) {
            throw new ValidationException("Gender is mandatory");
        }
    }

    private void validateUserId(UUID userId) {
        if (userId == null) {
            throw new InvalidRequestException("User id is required");
        }
    }

    private Specification<User> buildUserSpecification(UserSearchRequest request) {
        Specification<User> spec = Specification.where((root, query, cb) -> cb.conjunction());
        List<Specification<User>> specs = new ArrayList<>();

        if (request.keyword() != null && !request.keyword().isBlank()) {
            String keyword = "%" + request.keyword().toLowerCase() + "%";
            specs.add((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("firstName")), keyword),
                    cb.like(cb.lower(root.get("lastName")), keyword),
                    cb.like(cb.lower(root.get("email")), keyword)
            ));
        }
        if (request.status() != null) {
            specs.add((root, query, cb) -> cb.equal(root.get("status"), request.status()));
        }
        if (request.gender() != null) {
            specs.add((root, query, cb) -> cb.equal(root.get("gender"), request.gender()));
        }
        if (request.emailVerified() != null) {
            specs.add((root, query, cb) -> cb.equal(root.get("emailVerified"), request.emailVerified()));
        }
        if (request.phoneVerified() != null) {
            specs.add((root, query, cb) -> cb.equal(root.get("phoneVerified"), request.phoneVerified()));
        }

        for (Specification<User> s : specs) {
            spec = spec.and(s);
        }

        return spec;
    }
}
