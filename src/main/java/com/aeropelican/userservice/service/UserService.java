package com.aeropelican.userservice.service;
import com.aeropelican.userservice.dto.request.PageRequestDTO;
import com.aeropelican.userservice.dto.request.UserUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.enums.UserStatus;
import com.aeropelican.userservice.exceptions.UserNotFound;
import com.aeropelican.userservice.mapper.PageResponseMapper;
import com.aeropelican.userservice.mapper.UserMapper;
import com.aeropelican.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.aeropelican.userservice.dto.request.UserCreateRequestDTO;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
        // Get User By Id
        public UserResponseDTO getUser(UUID userId) {
            log.info("Request to fetch user by id:");
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFound("User not found"));
            return UserMapper.toResponseDTO(user);
        }

        // Get Users List
        public PageResponse<UserResponseDTO> usersList(PageRequestDTO requestDTO) {
            log.info("Fetching all users");
            Sort sort = requestDTO.getSortDir().equalsIgnoreCase("DESC")
                    ? Sort.by(requestDTO.getSortBy()).descending()
                    : Sort.by(requestDTO.getSortBy()).ascending();

            Pageable pageable = PageRequest.of(
                    requestDTO.getPage(),
                    requestDTO.getSize(),
                    sort);

            Page<User> pageResult = userRepository.findAll(pageable);

            List<UserResponseDTO> content = pageResult.getContent()
                    .stream()
                    .map(UserMapper::toResponseDTO)
                    .toList();

            return PageResponseMapper.toPageResponse(pageResult, content);
        }

        // Create User
        public UserResponseDTO registerUser(UserCreateRequestDTO request){
            log.info("Creating user with name");

            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPasswordHash(request.getPassword());
            user.setGender(request.getGender());
            user.setDateOfBirth(request.getDateOfBirth());
            User savedUser = userRepository.save(user);
            log.info("Role created successfully with id: {}", savedUser.getUserId());
            return UserMapper.toResponseDTO(savedUser);
        }

        public UserResponseDTO updateUser(UUID userId, UserUpdateRequestDTO request) {
            log.info("Updating role with id: {}", userId);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFound("User not found"));
            log.error("Role not found with id: {}", userId);
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());

            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPasswordHash(request.getPassword());
            }

            user.setGender(request.getGender());
            user.setDateOfBirth(request.getDateOfBirth());

            User updatedUser = userRepository.save(user);
            log.info("Role updated successfully with id: {}", userId);
            return UserMapper.toResponseDTO(updatedUser);
    }
    public UserResponseDTO deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("User not found"));
        if (user.getStatus() == UserStatus.DELETED) {
            throw new RuntimeException("User is already deleted");
        }
        user.setStatus(UserStatus.DELETED);
        User deletedUser = userRepository.save(user);
        log.info("Role deleted successfully with id: {}", userId);
        return UserMapper.toResponseDTO(deletedUser);
    }
}

