package com.example.global.exception.custom;

public class NotEqualsCodeException extends RuntimeException{

    public NotEqualsCodeException() {
        super("인증코드가 일치하지 않습니다.");
    }
    public NotEqualsCodeException(String message) {
        super(message);
    }
}
