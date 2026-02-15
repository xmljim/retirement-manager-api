package com.xmljim.retirement.api.employer;

import com.xmljim.retirement.api.dto.employer.CreateEmployerRequest;
import com.xmljim.retirement.api.dto.employer.EmployerDto;
import com.xmljim.retirement.api.dto.employer.UpdateEmployerRequest;
import com.xmljim.retirement.api.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

/**
 * API contract for Employer management operations.
 *
 * <p>Defines the REST API endpoints for managing employers in the retirement planning system.
 * This interface contains all OpenAPI/Swagger documentation annotations, separating the
 * API contract from the implementation.</p>
 *
 * @since 1.0
 */
@Tag(name = "Employers", description = "Employer management operations")
@RequestMapping("/api/v1/employers")
@SuppressWarnings("PMD.AvoidDuplicateLiterals") // Swagger response codes are intentionally repeated
public interface EmployerOperations {

    /**
     * Creates a new employer.
     *
     * @param request the creation request containing employer details
     * @return the created employer with HTTP 201 status and Location header
     */
    @Operation(
        summary = "Create an employer",
        description = "Creates a new employer in the retirement planning system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Employer created successfully",
            content = @Content(schema = @Schema(implementation = EmployerDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<EmployerDto> createEmployer(
            @Valid @RequestBody CreateEmployerRequest request);

    /**
     * Retrieves an employer by their unique identifier.
     *
     * @param id the employer's UUID
     * @return the employer if found
     */
    @Operation(
        summary = "Get an employer by ID",
        description = "Retrieves an employer by their unique identifier"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employer found",
            content = @Content(schema = @Schema(implementation = EmployerDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employer not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<EmployerDto> getEmployerById(
            @Parameter(description = "Employer ID", required = true)
            @PathVariable UUID id);

    /**
     * Retrieves all employers in the system with pagination.
     *
     * @param pageable pagination parameters
     * @return page of employers
     */
    @Operation(
        summary = "List all employers",
        description = "Retrieves all employers in the retirement planning system with pagination"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employers retrieved successfully"
        )
    })
    @GetMapping
    ResponseEntity<Page<EmployerDto>> getAllEmployers(
            @Parameter(description = "Pagination parameters")
            Pageable pageable);

    /**
     * Searches employers by name.
     *
     * @param name the name fragment to search for
     * @return list of matching employers
     */
    @Operation(
        summary = "Search employers by name",
        description = "Searches employers by name (case-insensitive partial match)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Search results"
        )
    })
    @GetMapping("/search")
    ResponseEntity<List<EmployerDto>> searchEmployersByName(
            @Parameter(description = "Name to search for", required = true)
            @RequestParam String name);

    /**
     * Updates an existing employer's information.
     *
     * @param id      the employer's UUID
     * @param request the update request containing updated employer details
     * @return the updated employer
     */
    @Operation(
        summary = "Update an employer",
        description = "Updates an existing employer's information"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employer updated successfully",
            content = @Content(schema = @Schema(implementation = EmployerDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employer not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<EmployerDto> updateEmployer(
            @Parameter(description = "Employer ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEmployerRequest request);

    /**
     * Deletes an employer from the system.
     *
     * @param id the employer's UUID
     * @return HTTP 204 No Content on success
     */
    @Operation(
        summary = "Delete an employer",
        description = "Deletes an employer from the retirement planning system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Employer deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employer not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEmployer(
            @Parameter(description = "Employer ID", required = true)
            @PathVariable UUID id);
}
