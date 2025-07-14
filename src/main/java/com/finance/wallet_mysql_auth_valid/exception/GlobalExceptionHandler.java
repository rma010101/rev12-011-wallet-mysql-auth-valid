package com.finance.wallet_mysql_auth_valid.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        // Add errors in preferred order
        String[] preferredOrder = {"ownerName", "balance", "currency"};
        for (String field : preferredOrder) {
            ex.getBindingResult().getFieldErrors().stream()
                .filter(error -> error.getField().equals(field))
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        }
        // Add any other errors not in preferredOrder
        ex.getBindingResult().getFieldErrors().stream()
            .filter(error -> !errors.containsKey(error.getField()))
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        String[] preferredOrder = {"ownerName", "balance", "currency"};
        for (String fieldName : preferredOrder) {
            ex.getConstraintViolations().stream()
                .filter(violation -> {
                    String field = violation.getPropertyPath().toString();
                    return field.substring(field.lastIndexOf('.') + 1).equals(fieldName);
                })
                .forEach(violation -> {
                    String field = violation.getPropertyPath().toString();
                    errors.put(field.substring(field.lastIndexOf('.') + 1), violation.getMessage());
                });
        }
        // Add any other errors not in preferredOrder
        ex.getConstraintViolations().stream()
            .filter(violation -> {
                String field = violation.getPropertyPath().toString();
                return !errors.containsKey(field.substring(field.lastIndexOf('.') + 1));
            })
            .forEach(violation -> {
                String field = violation.getPropertyPath().toString();
                errors.put(field.substring(field.lastIndexOf('.') + 1), violation.getMessage());
            });
        return errors;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
