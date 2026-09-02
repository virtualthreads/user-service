package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.response.PageResponse;
import com.aeropelican.userservice.dto.response.UserResponseDTO;
import com.aeropelican.userservice.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponseMapper {
    public static PageResponse<UserResponseDTO> toPageResponse(
            Page<User> pageResult,
            List<UserResponseDTO> content) {

        return PageResponse.<UserResponseDTO>builder()
                .content(content)
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalElement(pageResult.getTotalElements())
                .totalPage(pageResult.getTotalPages())
                .hasNext(pageResult.hasNext())
                .hasPrevious(pageResult.hasPrevious())
                .build();

    }
}
