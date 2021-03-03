package com.application.orderservice.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.application.orderservice.exception.ApplicationError;
import com.application.orderservice.exception.OrderNotFoundException;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    private static final String VALIDATION_ERROR_OCCURRED = "validation error occurred";

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApplicationError processOrderNotFoundException(OrderNotFoundException ex) {
        return new ApplicationError(NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class, ConstraintViolationException.class})
    public ApplicationError processMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.debug(ex.getMessage(), ex);
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private ApplicationError processFieldErrors(List<FieldError> fieldErrors) {
        ApplicationError error = new ApplicationError(BAD_REQUEST, VALIDATION_ERROR_OCCURRED);
        for (FieldError fieldError : fieldErrors) {
            error.addFieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
        }
        return error;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApplicationError handleMethodNotSupported(Exception ex) {
        log.debug(ex.getMessage(), ex);
        return new ApplicationError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ExceptionHandler({InternalServerError.class, NullPointerException.class})
    public ApplicationError handle(Exception ex) {
        log.debug(ex.getMessage(), ex);
        return new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR, "request can not be processed");
    }
}

