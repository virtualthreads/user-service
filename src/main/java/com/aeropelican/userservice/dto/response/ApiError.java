package com.aeropelican.userservice.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private String errorcode;
    private Integer status;
    private String path;
    private String error;
    private String message;
    private Timestamp timestamp;
}