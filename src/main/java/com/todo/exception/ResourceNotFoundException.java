package com.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;

public class ResourceNotFoundException extends TodoAppException {

    @Serial
    private static final long serialVersionUID = 120389290543575104L;

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message, HttpStatusCode status) {
        super(message, status);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, HttpStatus.NOT_FOUND, cause);
    }

    public ResourceNotFoundException(String message, HttpStatusCode status, Throwable cause) {
        super(message, status, cause);
    }
}
