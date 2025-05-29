package com.todo.exception.handler;

import com.todo.exception.*;
import com.todo.exception.dto.ErrorResponseDto;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    private void logException(ErrorResponseDto errorResponseDto) {
        if (log.isErrorEnabled())
            log.error("Exception occurred - Timestamp:{}, Status:{}, ErrorDetails:{}, Path:{}",
                    errorResponseDto.timestamp(), errorResponseDto.status(), errorResponseDto.errorDetails(), errorResponseDto.path());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({TodoAppException.class, UnauthorizedException.class, ResourceNotFoundException.class, ForbiddenOperationException.class, ConflictException.class})
    public ResponseEntity<ErrorResponseDto> handleTodoAppExceptions(TodoAppException exception, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(exception.getStatus(), exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, exception.getStatus());
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(HttpStatus.FORBIDDEN, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleDataAccessException(DataAccessException exception, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (exception instanceof DataIntegrityViolationException || exception instanceof OptimisticLockingFailureException)
            status = HttpStatus.CONFLICT;
        else if (exception instanceof PessimisticLockingFailureException)
            status = HttpStatus.LOCKED;
        else if (exception instanceof QueryTimeoutException)
            status = HttpStatus.REQUEST_TIMEOUT;
        else if (exception instanceof EmptyResultDataAccessException)
            status = HttpStatus.NOT_FOUND;

        String error = exception.getMostSpecificCause().getMessage();
        String errorDetails = exception.getClass().getName() + ": " + error;

        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, error, List.of(errorDetails), request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException exception, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(HttpStatus.BAD_REQUEST, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errorDetails = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    Object[] arguments = fieldError.getArguments();
                    String minLength = arguments.length > 2 ? arguments[2].toString() : "N/A";
                    String maxLength = arguments.length > 1 ? arguments[1].toString() : "N/A";
                    String rejectedValue = fieldError.getRejectedValue().toString();

                    return String.format("%s.%s minLength:%s maxLength:%s rejectedValue:%s message:%s", fieldError.getObjectName(),
                            fieldError.getField(), minLength, maxLength, rejectedValue, fieldError.getDefaultMessage());
                })
                .toList();

        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, errorDetails, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleErrorResponseException(ErrorResponseException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.getErrorResponseDto(status, exception, request);
        logException(errorResponseDto);

        return new ResponseEntity<>(errorResponseDto, status);
    }
}
