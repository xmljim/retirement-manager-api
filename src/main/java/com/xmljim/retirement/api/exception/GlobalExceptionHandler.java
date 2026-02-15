package com.xmljim.retirement.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

/**
 * Global exception handler for REST API.
 *
 * <p>Provides centralized exception handling for all REST controllers,
 * converting exceptions to consistent error responses.</p>
 *
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ResourceNotFoundException.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return HTTP 404 response with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            final ResourceNotFoundException ex,
            final HttpServletRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Resource not found: {} with id: {}", ex.getResourceType(), ex.getResourceId());
        }
        var response = ErrorResponse.create(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles DataNotFoundException.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return HTTP 404 response with error details
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFound(
            final DataNotFoundException ex,
            final HttpServletRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Data not found: {} for {}", ex.getDataType(), ex.getCriteria());
        }
        var response = ErrorResponse.create(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles validation errors from @Valid annotations.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return HTTP 400 response with field-level error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            final MethodArgumentNotValidException ex,
            final HttpServletRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Validation failed for request to {}", request.getRequestURI());
        }
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse.FieldError(
                        error.getField(),
                        error.getDefaultMessage()))
                .toList();

        var response = ErrorResponse.createWithFields(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles malformed JSON or type conversion errors in request body.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return HTTP 400 response with error details
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(
            final HttpMessageNotReadableException ex,
            final HttpServletRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Malformed JSON in request to {}: {}", request.getRequestURI(), ex.getMessage());
        }
        var response = ErrorResponse.create(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Malformed JSON request",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles type mismatch errors in path variables or request parameters.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return HTTP 400 response with error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            final MethodArgumentTypeMismatchException ex,
            final HttpServletRequest request) {
        var message = String.format("Invalid value '%s' for parameter '%s'",
                ex.getValue(), ex.getName());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Type mismatch in request to {}: {}", request.getRequestURI(), message);
        }
        var response = ErrorResponse.create(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles illegal argument exceptions from business validation.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return HTTP 400 response with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            final IllegalArgumentException ex,
            final HttpServletRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Invalid argument in request to {}: {}", request.getRequestURI(), ex.getMessage());
        }
        var response = ErrorResponse.create(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles all other unexpected exceptions.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return HTTP 500 response with generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            final Exception ex,
            final HttpServletRequest request) {
        if (LOG.isErrorEnabled()) {
            LOG.error("Unexpected error processing request to {}", request.getRequestURI(), ex);
        }
        var response = ErrorResponse.create(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
