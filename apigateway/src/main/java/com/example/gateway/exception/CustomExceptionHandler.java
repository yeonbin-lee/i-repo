package com.example.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MissingAuthorizationHeaderException.class)
    public ResponseEntity<String> handleMissingAuthorizationHeaderException(MissingAuthorizationHeaderException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing authorization header");
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access to application");
    }

    @ExceptionHandler(InvalidAuthorizationHeaderFormatException.class)
    public ResponseEntity<String> handleInvalidAuthorizationHeaderFormatException(InvalidAuthorizationHeaderFormatException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authorization header format");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}