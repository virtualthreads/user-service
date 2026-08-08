package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.enums.Gender;
import com.aeropelican.userservice.enums.SortDirection;
import com.aeropelican.userservice.enums.UserStatus;
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
public class UserSearchRequestDTO {

    private String keyword;

    private UserStatus status;

    private Gender gender;

    private Boolean emailVerified;

    private Boolean phoneVerified;

    private Integer page;

    private Integer size;

    private String sortBy;

    private SortDirection sortDirection;
}