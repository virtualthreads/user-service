package com.aeropelican.userservice.service.impl;

import com.aeropelican.userservice.dto.request.CreateUserRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateUserRequestDTO;
import com.aeropelican.userservice.dto.request.UserSearchRequestDTO;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.enums.SortDirection;
import com.aeropelican.userservice.enums.UserStatus;
import com.aeropelican.userservice.exception.EmailAlreadyExistsException;
import com.aeropelican.userservice.exception.InvalidRequestException;
import com.aeropelican.userservice.exception.PhoneNumberAlreadyExistsException;
import com.aeropelican.userservice.exception.UserNotFoundException;
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

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    // =========================================================
    // CREATE USER
    // =========================================================

    @Override
    public UserResponseDTO createUser(
            CreateUserRequestDTO request) {

        log.info(
                "Creating user with email: {}",
                request.getEmail()
        );

        if (userRepository.existsByEmail(
                request.getEmail())) {

            log.warn(
                    "Email already exists: {}",
                    request.getEmail()
            );

            throw new EmailAlreadyExistsException(
                    "Email is already registered"
            );
        }

        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().isBlank()
                && userRepository.existsByPhoneNumber(
                request.getPhoneNumber())) {

            log.warn(
                    "Phone number already exists: {}",
                    request.getPhoneNumber()
            );

            throw new PhoneNumberAlreadyExistsException(
                    "Phone number is already registered"
            );
        }

        User user =
                UserMapper.toEntity(request);

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        User savedUser =
                userRepository.save(user);

        log.info(
                "User created successfully. User ID: {}",
                savedUser.getUserId()
        );

        return UserMapper.toResponse(savedUser);
    }


    // =========================================================
    // GET USER
    // =========================================================

    @Override
    public UserResponseDTO getUser(
            UUID userId) {

        log.info(
                "Fetching user with ID: {}",
                userId
        );

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "User not found with ID: {}",
                                    userId
                            );

                            return new UserNotFoundException(
                                    "User not found with ID: "
                                            + userId
                            );
                        });

        // User must not be returned after soft deletion
        if (user.getStatus() == UserStatus.DELETED) {

            log.warn(
                    "Attempted to fetch deleted user: {}",
                    userId
            );

            throw new UserNotFoundException(
                    "User not found with ID: "
                            + userId
            );
        }

        log.info(
                "User fetched successfully."
        );

        return UserMapper.toResponse(user);
    }


    // =========================================================
    // SEARCH USERS
    // =========================================================

    @Override
    public PageResponse<UserResponseDTO> searchUsers(
            UserSearchRequestDTO request) {

        log.info(
                "Searching users. keyword={}, status={}, gender={}, " +
                        "emailVerified={}, phoneVerified={}",
                request.getKeyword(),
                request.getStatus(),
                request.getGender(),
                request.getEmailVerified(),
                request.getPhoneVerified()
        );

        // -----------------------------------------------------
        // PAGE
        // -----------------------------------------------------

        int page =
                request.getPage() != null
                        ? request.getPage()
                        : 0;

        if (page < 0) {

            log.warn(
                    "Invalid page value: {}",
                    page
            );

            throw new InvalidRequestException(
                    "Page cannot be negative"
            );
        }

        // -----------------------------------------------------
        // SIZE
        // -----------------------------------------------------

        int size =
                request.getSize() != null
                        ? request.getSize()
                        : 10;

        if (size <= 0) {

            log.warn(
                    "Invalid page size: {}",
                    size
            );

            throw new InvalidRequestException(
                    "Page size must be greater than 0"
            );
        }

        if (size > 100) {

            log.warn(
                    "Page size exceeds maximum allowed value: {}",
                    size
            );

            throw new InvalidRequestException(
                    "Page size cannot be greater than 100"
            );
        }

        // -----------------------------------------------------
        // SORT FIELD
        // -----------------------------------------------------

        String sortBy =
                request.getSortBy() != null
                        && !request.getSortBy().isBlank()
                        ? request.getSortBy().trim()
                        : "firstName";

        Set<String> allowedSortFields = Set.of(
                "firstName",
                "lastName",
                "email",
                "phoneNumber",
                "gender",
                "dateOfBirth",
                "emailVerified",
                "phoneVerified",
                "status",
                "createdAt",
                "updatedAt"
        );

        if (!allowedSortFields.contains(sortBy)) {

            log.warn(
                    "Invalid sort field: {}",
                    sortBy
            );

            throw new InvalidRequestException(
                    "Invalid sort field: " + sortBy
            );
        }

        // -----------------------------------------------------
        // SORT DIRECTION
        // -----------------------------------------------------

        SortDirection sortDirection =
                request.getSortDirection() != null
                        ? request.getSortDirection()
                        : SortDirection.ASC;

        Sort sort =
                sortDirection == SortDirection.DESC
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );

        // -----------------------------------------------------
        // BASE SPECIFICATION
        // -----------------------------------------------------

        Specification<User> specification =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction();

        // -----------------------------------------------------
        // KEYWORD FILTER
        // -----------------------------------------------------

        if (request.getKeyword() != null
                && !request.getKeyword().isBlank()) {

            String keyword =
                    "%"
                            + request.getKeyword()
                            .trim()
                            .toLowerCase()
                            + "%";

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.or(

                                            criteriaBuilder.like(
                                                    criteriaBuilder.lower(
                                                            root.get(
                                                                    "firstName"
                                                            )
                                                    ),
                                                    keyword
                                            ),

                                            criteriaBuilder.like(
                                                    criteriaBuilder.lower(
                                                            root.get(
                                                                    "lastName"
                                                            )
                                                    ),
                                                    keyword
                                            ),

                                            criteriaBuilder.like(
                                                    criteriaBuilder.lower(
                                                            root.get(
                                                                    "email"
                                                            )
                                                    ),
                                                    keyword
                                            )
                                    )
                    );
        }

        // -----------------------------------------------------
        // STATUS FILTER
        // -----------------------------------------------------

        if (request.getStatus() != null) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(
                                            root.get("status"),
                                            request.getStatus()
                                    )
                    );
        }

        // -----------------------------------------------------
        // GENDER FILTER
        // -----------------------------------------------------

        if (request.getGender() != null) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(
                                            root.get("gender"),
                                            request.getGender()
                                    )
                    );
        }

        // -----------------------------------------------------
        // EMAIL VERIFIED FILTER
        // -----------------------------------------------------

        if (request.getEmailVerified() != null) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(
                                            root.get("emailVerified"),
                                            request.getEmailVerified()
                                    )
                    );
        }

        // -----------------------------------------------------
        // PHONE VERIFIED FILTER
        // -----------------------------------------------------

        if (request.getPhoneVerified() != null) {

            specification =
                    specification.and(
                            (root, query, criteriaBuilder) ->
                                    criteriaBuilder.equal(
                                            root.get("phoneVerified"),
                                            request.getPhoneVerified()
                                    )
                    );
        }

        // -----------------------------------------------------
        // DON'T RETURN DELETED USERS
        // -----------------------------------------------------

        specification =
                specification.and(
                        (root, query, criteriaBuilder) ->
                                criteriaBuilder.notEqual(
                                        root.get("status"),
                                        UserStatus.DELETED
                                )
                );

        // -----------------------------------------------------
        // EXECUTE SEARCH
        // -----------------------------------------------------

        Page<User> userPage =
                userRepository.findAll(
                        specification,
                        pageable
                );

        log.info(
                "Total users found: {}",
                userPage.getTotalElements()
        );

        // -----------------------------------------------------
        // BUILD RESPONSE
        // -----------------------------------------------------

        return PageResponse
                .<UserResponseDTO>builder()
                .content(
                        userPage
                                .getContent()
                                .stream()
                                .map(UserMapper::toResponse)
                                .toList()
                )
                .page(
                        userPage.getNumber()
                )
                .size(
                        userPage.getSize()
                )
                .totalElements(
                        userPage.getTotalElements()
                )
                .totalPages(
                        userPage.getTotalPages()
                )
                .first(
                        userPage.isFirst()
                )
                .last(
                        userPage.isLast()
                )
                .build();
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    @Override
    public UserResponseDTO updateUser(
            UUID userId,
            UpdateUserRequestDTO request) {

        log.info(
                "Updating user with ID: {}",
                userId
        );

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "User not found with ID: {}",
                                    userId
                            );

                            return new UserNotFoundException(
                                    "User not found with ID: "
                                            + userId
                            );
                        });

        // -----------------------------------------------------
        // DELETED USER CANNOT BE UPDATED
        // -----------------------------------------------------

        if (user.getStatus() == UserStatus.DELETED) {

            throw new UserNotFoundException(
                    "User not found with ID: "
                            + userId
            );
        }

        // -----------------------------------------------------
        // EMAIL DUPLICATE CHECK
        // -----------------------------------------------------

        if (request.getEmail() != null
                && !request.getEmail()
                .equalsIgnoreCase(user.getEmail())
                && userRepository.existsByEmail(
                request.getEmail())) {

            log.warn(
                    "Email already exists: {}",
                    request.getEmail()
            );

            throw new EmailAlreadyExistsException(
                    "Email is already registered"
            );
        }

        // -----------------------------------------------------
        // PHONE DUPLICATE CHECK
        // -----------------------------------------------------

        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber()
                .equals(user.getPhoneNumber())
                && userRepository.existsByPhoneNumber(
                request.getPhoneNumber())) {

            log.warn(
                    "Phone number already exists: {}",
                    request.getPhoneNumber()
            );

            throw new PhoneNumberAlreadyExistsException(
                    "Phone number is already registered"
            );
        }

        // -----------------------------------------------------
        // UPDATE ENTITY
        // -----------------------------------------------------

        UserMapper.updateEntity(
                user,
                request
        );

        User updatedUser =
                userRepository.save(user);

        log.info(
                "User updated successfully. User ID: {}",
                updatedUser.getUserId()
        );

        return UserMapper.toResponse(
                updatedUser
        );
    }


    // =========================================================
    // UPDATE USER STATUS
    // =========================================================

    @Override
    public UserResponseDTO updateUserStatus(
            UUID userId,
            String status) {

        log.info(
                "Updating status for user: {}",
                userId
        );

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "User not found with ID: {}",
                                    userId
                            );

                            return new UserNotFoundException(
                                    "User not found with ID: "
                                            + userId
                            );
                        });

        UserStatus newStatus;

        try {

            newStatus =
                    UserStatus.valueOf(
                            status.toUpperCase()
                    );

        } catch (IllegalArgumentException ex) {

            log.warn(
                    "Invalid user status: {}",
                    status
            );

            throw new InvalidRequestException(
                    "Invalid user status: " + status
            );
        }

        user.setStatus(newStatus);

        User updatedUser =
                userRepository.save(user);

        log.info(
                "User status updated successfully to {}",
                updatedUser.getStatus()
        );

        return UserMapper.toResponse(
                updatedUser
        );
    }


    // =========================================================
    // DELETE USER - SOFT DELETE
    // =========================================================

    @Override
    public void deleteUser(
            UUID userId) {

        log.info(
                "Deleting user with ID: {}",
                userId
        );

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() -> {

                            log.warn(
                                    "User not found with ID: {}",
                                    userId
                            );

                            return new UserNotFoundException(
                                    "User not found with ID: "
                                            + userId
                            );
                        });

        // -----------------------------------------------------
        // ALREADY DELETED CHECK
        // -----------------------------------------------------

        if (user.getStatus() == UserStatus.DELETED) {

            log.warn(
                    "User already deleted. User ID: {}",
                    userId
            );

            throw new InvalidRequestException(
                    "User is already deleted."
            );
        }

        // -----------------------------------------------------
        // SOFT DELETE
        // -----------------------------------------------------

        user.setStatus(
                UserStatus.DELETED
        );

        userRepository.save(user);

        log.info(
                "User soft deleted successfully. User ID: {}",
                userId
        );
    }
}