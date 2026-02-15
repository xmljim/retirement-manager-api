package com.xmljim.retirement.api.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Standard error response for API errors.
 *
 * <p>Provides a consistent format for all error responses returned by the API.</p>
 *
 * @param timestamp   when the error occurred
 * @param status      the HTTP status code
 * @param error       the error type (e.g., "Not Found", "Bad Request")
 * @param message     a human-readable error message
 * @param path        the request path that caused the error
 * @param fieldErrors list of field-level validation errors (for 400 responses)
 * @since 1.0
 */
@Schema(description = "Standard error response")
public record ErrorResponse(
        @Schema(description = "When the error occurred")
        Instant timestamp,

        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "Error type", example = "Not Found")
        String error,

        @Schema(description = "Error message", example = "Person not found with id: 550e8400...")
        String message,

        @Schema(description = "Request path", example = "/api/v1/persons/550e8400...")
        String path,

        @Schema(description = "Field validation errors")
        List<FieldError> fieldErrors
) {

    /**
     * Compact constructor that creates a defensive copy of the field errors list.
     *
     * @param fieldErrors the field errors to copy
     */
    public ErrorResponse {
        fieldErrors = fieldErrors == null ? List.of() : List.copyOf(fieldErrors);
    }

    /**
     * Creates an ErrorResponse without field errors.
     *
     * @param status  the HTTP status code
     * @param error   the error type
     * @param message the error message
     * @param path    the request path
     * @return the error response
     */
    public static ErrorResponse create(final int status, final String error,
                                        final String message, final String path) {
        return new ErrorResponse(Instant.now(), status, error, message, path, List.of());
    }

    /**
     * Creates an ErrorResponse with field errors.
     *
     * @param status      the HTTP status code
     * @param error       the error type
     * @param message     the error message
     * @param path        the request path
     * @param fieldErrors the field validation errors
     * @return the error response
     */
    public static ErrorResponse createWithFields(final int status, final String error,
                                                  final String message, final String path,
                                                  final List<FieldError> fieldErrors) {
        return new ErrorResponse(Instant.now(), status, error, message, path, fieldErrors);
    }

    /**
     * Represents a field-level validation error.
     *
     * @param field   the field name
     * @param message the validation error message
     */
    @Schema(description = "Field validation error")
    public record FieldError(
            @Schema(description = "Field name", example = "firstName")
            String field,

            @Schema(description = "Validation error message", example = "First name is required")
            String message
    ) {
    }
}
