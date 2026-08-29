package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateUserRequest;
import com.aeropelican.userservice.dto.response.AuthUser;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.SortDirection;
import com.aeropelican.userservice.dto.request.UpdateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.dto.request.UserSearchRequest;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;
import com.aeropelican.userservice.exception.BusinessException;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.exception.ValidationException;
import com.aeropelican.userservice.mapper.UserMapper;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.repository.UserRoleRepository;
import com.aeropelican.userservice.service.UserService;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        logger.info("Initiating user creation for email: {}", request.email());
        validateCreateRequest(request);
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.email());
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User savedUser = userRepository.save(userMapper.toEntity(request, encodedPassword));
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> searchUsers(UserSearchRequest request) {
        int page = Optional.ofNullable(request.page()).filter(p -> p >= 0).orElse(0);
        int size = Optional.ofNullable(request.size()).filter(s -> s > 0).orElse(10);
        String sortBy = Optional.ofNullable(request.sortBy()).filter(s -> !s.trim().isEmpty()).orElse("createdAt");
        SortDirection sortDirection = (SortDirection) Optional.ofNullable(request.sortDirection()).orElse(SortDirection.ASC);
        org.springframework.data.domain.Sort sort = sortDirection == SortDirection.DESC
                ? org.springframework.data.domain.Sort.by(sortBy).descending()
                : org.springframework.data.domain.Sort.by(sortBy).ascending();
        org.springframework.data.domain.PageRequest pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        org.springframework.data.domain.Page<User> userPage = userRepository.findAll(createSearchSpecification(request), pageable);
        return PageResponse.from(userPage.map(userMapper::toResponse));
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        validateUpdateRequest(request);
        User existingUser = userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (userRepository.existsByEmailAndUserIdNot(request.email(), userId)) {
            throw new ResourceAlreadyExistsException("Email already in use: " + request.email());
        }
        userMapper.updateEntityFromDto(request, existingUser);
        return userMapper.toResponse(userRepository.save(existingUser));
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public UserResponse updateUserStatus(UUID userId, UpdateUserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setStatus(request.status());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUser findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        List<UserRole> roles = userRoleRepository.findByUser_UserId(user.getUserId());
        String role = roles.isEmpty() ? "CUSTOMER" : roles.get(0).getRole().getRoleName();

        return new AuthUser(user.getUserId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPasswordHash(), role);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (user.getStatus() == Status.DELETED) {
            throw new BusinessException("User is already deleted");
        }
        user.setStatus(Status.DELETED);
        userRepository.save(user);
    }

    private void validateCreateRequest(CreateUserRequest request) {
        if (request.password() != null && request.password().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        if (request.dateOfBirth() != null && request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be a future date");
        }
    }

    private void validateUpdateRequest(UpdateUserRequest request) {
        if (request.dateOfBirth() != null && request.dateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be a future date");
        }
    }

    private org.springframework.data.jpa.domain.Specification<User> createSearchSpecification(UserSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.keyword() != null && !request.keyword().trim().isEmpty()) {
                String pattern = "%" + request.keyword().trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), pattern)));
            }
            if (request.status() != null) predicates.add(criteriaBuilder.equal(root.get("status"), request.status()));
            if (request.gender() != null) predicates.add(criteriaBuilder.equal(root.get("gender"), request.gender()));
            if (request.emailVerified() != null) predicates.add(criteriaBuilder.equal(root.get("emailVerified"), request.emailVerified()));
            if (request.phoneVerified() != null) predicates.add(criteriaBuilder.equal(root.get("phoneVerified"), request.phoneVerified()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
