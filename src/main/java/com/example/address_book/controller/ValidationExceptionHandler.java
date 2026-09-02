package com.example.address_book.controller;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import java.util.HashMap;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        Map<String, String> errorMessages = new HashMap<>();

        //拡張for文（enhanced for loop）foreach相当
        for (FieldError error : errors) {
            errorMessages.put(
                error.getField(),
                error.getDefaultMessage()
            );
        }

        return ResponseEntity
                .badRequest()
                .body(errorMessages);
    }
}
