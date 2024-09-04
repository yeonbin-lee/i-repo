package com.example.global.exception.custom;

public class NotEqualsPasswordException extends IllegalArgumentException{

    public NotEqualsPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
    public NotEqualsPasswordException(String message) {
        super(message);
    }
}

