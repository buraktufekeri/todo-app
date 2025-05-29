package com.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;

public class UnauthorizedException extends TodoAppException {

    @Serial
    private static final long serialVersionUID = 120389290543575104L;

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, HttpStatusCode status) {
        super(message, status);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, HttpStatus.UNAUTHORIZED, cause);
    }

    public UnauthorizedException(String message, HttpStatusCode status, Throwable cause) {
        super(message, status, cause);
    }
}
