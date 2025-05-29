package com.todo.exception.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record ErrorResponseDto(
        String timestamp,
        int status,
        String error,
        List<String> errorDetails,
        String path) implements Serializable {

    @Serial
    private static final long serialVersionUID = -1013249837481138333L;

    private static ErrorResponseDto buildErrorResponseDto(HttpStatusCode status, String error, List<String> errorDetails, WebRequest request) {
        String path = getPath(request);

        return new ErrorResponseDto(
                Instant.now().toString(),
                status.value(),
                error,
                errorDetails,
                path
        );
    }

    private static String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
            String path = httpServletRequest.getRequestURI();
            String query = httpServletRequest.getQueryString();

            return ObjectUtils.isNotEmpty(query) ? path + "?" + query : path;
        }

        return "Unknown Path";
    }

    public static ErrorResponseDto getErrorResponseDto(HttpStatusCode status, Exception exception, WebRequest request) {
        return buildErrorResponseDto(status, exception.getMessage(), List.of(exception.toString()), request);
    }

    public static ErrorResponseDto getErrorResponseDto(HttpStatusCode status, Exception exception, List<String> errorDetails, WebRequest request) {
        return buildErrorResponseDto(status, exception.getClass().getName(), errorDetails, request);
    }

    public static ErrorResponseDto getErrorResponseDto(HttpStatusCode status, String error, List<String> errorDetails, WebRequest request) {
        return buildErrorResponseDto(status, error, errorDetails, request);
    }
}
