package com.example.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class MissingAuthorizationHeaderException extends RuntimeException {
    public MissingAuthorizationHeaderException() {
        super("Missing authorization header");
    }
}
