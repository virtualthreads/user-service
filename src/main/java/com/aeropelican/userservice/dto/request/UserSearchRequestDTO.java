package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.enums.Gender;
import com.aeropelican.userservice.enums.UserStatus;
import lombok.Builder;

@Builder
public class UserSearchRequestDTO {
    private String keyword;
    private UserStatus status;
    private Gender gender;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    @Builder.Default
    private Integer page=0;
    @Builder.Default
    private Integer size=10;
    @Builder.Default
    private String sortBy="createdAt";
    @Builder.Default
    private String sortDirection="DESC";
}
