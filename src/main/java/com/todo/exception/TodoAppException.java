package com.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;

public class TodoAppException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4200617482569438523L;

    private final HttpStatusCode status;

    public TodoAppException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public TodoAppException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }

    public TodoAppException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public TodoAppException(String message, HttpStatusCode status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
