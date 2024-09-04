package com.example.global.exception.custom;

public class SocialLoginException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SocialLoginException() {
        super("social login is failed.");
    }

    public SocialLoginException(String message) {
        super(message);
    }
}
