package com.chariot.quizzographql.security;

import org.springframework.http.HttpStatus;

// from https://github.com/murraco/spring-boot-jwt/blob/master/src/main/java/murraco/exception/CustomException.java
public class TokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String message;
    private final HttpStatus status;

    public TokenException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }
}
