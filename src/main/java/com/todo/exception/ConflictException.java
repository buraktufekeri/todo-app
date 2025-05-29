package com.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;

public class ConflictException extends TodoAppException {

    @Serial
    private static final long serialVersionUID = 120389290543575104L;

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException(String message, HttpStatusCode status) {
        super(message, status);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, HttpStatus.CONFLICT, cause);
    }

    public ConflictException(String message, HttpStatusCode status, Throwable cause) {
        super(message, status, cause);
    }
}
