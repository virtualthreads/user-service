package com.aeropelican.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldError {
    private String field;
    private Object rejectedValue;
    private String message;
}
