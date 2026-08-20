package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.dto.SortDirection;
import com.aeropelican.userservice.entity.Gender;
import com.aeropelican.userservice.entity.Status;

public record UserSearchRequest(
        String keyword,
        Status status,
        Gender gender,
        Boolean emailVerified,
        Boolean phoneVerified,
        Integer page,
        Integer size,
        String sortBy,
        SortDirection sortDirection
) {
}
