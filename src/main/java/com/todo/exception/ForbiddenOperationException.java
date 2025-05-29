package com.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;

public class ForbiddenOperationException extends TodoAppException {

    @Serial
    private static final long serialVersionUID = 120389290543575104L;

    public ForbiddenOperationException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ForbiddenOperationException(String message, HttpStatusCode status) {
        super(message, status);
    }

    public ForbiddenOperationException(String message, Throwable cause) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, cause);
    }

    public ForbiddenOperationException(String message, HttpStatusCode status, Throwable cause) {
        super(message, status, cause);
    }
}
