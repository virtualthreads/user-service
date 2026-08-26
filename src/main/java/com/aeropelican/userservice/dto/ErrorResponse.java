package com.aeropelican.userservice.dto.response;

import java.util.List;

public class ErrorResponse {

    private String errorCode;
    private int status;
    private String path;
    private List<FieldError> fieldErrors;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorCode, int status, String path, List<FieldError> fieldErrors) {
        this.errorCode = errorCode;
        this.status = status;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public static class Builder {

        private String errorCode;
        private int status;
        private String path;
        private List<FieldError> fieldErrors;

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder fieldErrors(List<FieldError> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(
                    errorCode,
                    status,
                    path,
                    fieldErrors
            );
        }
    }
}