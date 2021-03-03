package com.application.orderservice.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

public class ApplicationError {
    private final HttpStatus status;
    private final String message;
    private List<FieldError> fieldErrors = new ArrayList<>();

    public ApplicationError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public void addFieldError(String objectName,String path, String message) {
        FieldError error = new FieldError(objectName,path,message);
        fieldErrors.add(error);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
