package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserStatusRequestDTO {

    @NotNull(message = "Status is required")
    private UserStatus status;

}