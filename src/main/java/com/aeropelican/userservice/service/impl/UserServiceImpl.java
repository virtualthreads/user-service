package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateUserRequest;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.SortDirection;
import com.aeropelican.userservice.dto.request.UpdateUserRequest;
import com.aeropelican.userservice.dto.request.UpdateUserStatusRequest;
import com.aeropelican.userservice.dto.response.UserResponse;
import com.aeropelican.userservice.dto.request.UserSearchRequest;
import com.aeropelican.userservice.entity.Status;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.exception.BusinessException;
import com.aeropelican.userservice.exception.ResourceAlreadyExistsException;
import com.aeropelican.userservice.exception.ResourceNotFoundException;
import com.aeropelican.userservice.exception.ValidationException;
import com.aeropelican.userservice.mapper.UserMapper;
import com.aeropelican.userservice.repository.UserRepository;
import com.aeropelican.userservice.service.UserService;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        logger.info("Initiating user creation for email: {}", request.email());

        validateCreateRequest(request);

        if (userRepository.existsByEmail(request.email())) {
            logger.warn("User registration failed: Email {} is already registered", request.email());
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.email());
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        User userEntity = userMapper.toEntity(request, encodedPassword);

        User savedUser = userRepository.save(userEntity);
        logger.info("Successfully created user with ID: {}", savedUser.getUserId());

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public UserResponse getUserById(UUID userId) {
        logger.debug("Fetching user details for userId: {}", userId);

        User user = userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)
                .orElseThrow(() -> {
                    logger.warn("User search failed: User not found or deleted with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> searchUsers(UserSearchRequest request) {
        logger.debug("Executing user search with keyword: '{}', status: '{}', gender: '{}'",
                request.keyword(), request.status(), request.gender());

        int page = Optional.ofNullable(request.page()).filter(p -> p >= 0).orElse(0);
        int size = Optional.ofNullable(request.size()).filter(s -> s > 0).orElse(10);
        String sortBy = Optional.ofNullable(request.sortBy())
                .filter(s -> !s.trim().isEmpty())
                .orElse("createdAt");
        SortDirection sortDirection = (SortDirection) Optional.ofNullable(request.sortDirection()).orElse(SortDirection.ASC);

        Sort sort = sortDirection == SortDirection.DESC
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<User> spec = createSearchSpecification(request);
        Page<User> userPage = userRepository.findAll(spec, pageable);

        logger.info("User search completed. Found {} users across {} pages",
                userPage.getTotalElements(), userPage.getTotalPages());

        Page<UserResponse> dtoPage = userPage.map(userMapper::toResponse);
        return PageResponse.from(dtoPage);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        logger.info("Updating profile for user ID: {}", userId);

        validateUpdateRequest(request);

        User existingUser = userRepository.findByUserIdAndStatusNot(userId, Status.DELETED)
                .orElseThrow(() -> {
                    logger.warn("User update failed: Active user not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        if (userRepository.existsByEmailAndUserIdNot(request.email(), userId)) {
            logger.warn("User update failed: Email {} is already in use by another account", request.email());
            throw new ResourceAlreadyExistsException("Email already in use: " + request.email());
        }

        userMapper.updateEntityFromDto(request, existingUser);
        User updatedUser = userRepository.save(existingUser);

        logger.info("Successfully updated user profile for ID: {}", userId);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public UserResponse updateUserStatus(UUID userId, UpdateUserStatusRequest request) {
        logger.info("Updating status for user ID: {} to {}", userId, request.status());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Status update failed: User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        user.setStatus(request.status());
        User updatedUser = userRepository.save(user);

        logger.info("Successfully updated status for user ID: {} to {}", userId, request.status());
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(UUID userId) {
        logger.info("Request received to soft-delete user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Soft delete failed: User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        if (user.getStatus() == Status.DELETED) {
            logger.warn("Soft delete skipped: User ID {} is already deleted", userId);
            throw new BusinessException("User is already deleted");
        }

        user.setStatus(Status.DELETED);
        userRepository.save(user);
        logger.info("Successfully soft-deleted user with ID: {}", userId);
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

    private Specification<User> createSearchSpecification(UserSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.keyword() != null && !request.keyword().trim().isEmpty()) {
                String pattern = "%" + request.keyword().trim().toLowerCase() + "%";
                Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern);
                Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern);
                Predicate emailMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern);
                Predicate phoneMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), pattern);
                predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch, emailMatch, phoneMatch));
            }

            if (request.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.status()));
            }

            if (request.gender() != null) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), request.gender()));
            }

            if (request.emailVerified() != null) {
                predicates.add(criteriaBuilder.equal(root.get("emailVerified"), request.emailVerified()));
            }

            if (request.phoneVerified() != null) {
                predicates.add(criteriaBuilder.equal(root.get("phoneVerified"), request.phoneVerified()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
