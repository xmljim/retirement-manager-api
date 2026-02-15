package com.xmljim.retirement.api.employment;

import com.xmljim.retirement.api.dto.employment.CreateEmploymentRequest;
import com.xmljim.retirement.api.dto.employment.EmploymentDto;
import com.xmljim.retirement.api.dto.employment.UpdateEmploymentRequest;
import com.xmljim.retirement.api.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
 * API contract for Employment management operations.
 *
 * <p>Defines the REST API endpoints for managing employment records in the retirement
 * planning system. This interface contains all OpenAPI/Swagger documentation annotations,
 * separating the API contract from the implementation.</p>
 *
 * @since 1.0
 */
@Tag(name = "Employment", description = "Employment management operations")
@RequestMapping("/api/v1/employment")
@SuppressWarnings("PMD.AvoidDuplicateLiterals") // Swagger response codes are intentionally repeated
public interface EmploymentOperations {

    /**
     * Creates a new employment record.
     *
     * @param request the creation request containing employment details
     * @return the created employment with HTTP 201 status and Location header
     */
    @Operation(
        summary = "Create an employment record",
        description = "Creates a new employment record linking a person to an employer"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Employment created successfully",
            content = @Content(schema = @Schema(implementation = EmploymentDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person or Employer not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<EmploymentDto> createEmployment(
            @Valid @RequestBody CreateEmploymentRequest request);

    /**
     * Retrieves an employment record by its unique identifier.
     *
     * @param id the employment's UUID
     * @return the employment if found
     */
    @Operation(
        summary = "Get an employment by ID",
        description = "Retrieves an employment record by its unique identifier"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employment found",
            content = @Content(schema = @Schema(implementation = EmploymentDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employment not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<EmploymentDto> getEmploymentById(
            @Parameter(description = "Employment ID", required = true)
            @PathVariable UUID id);

    /**
     * Retrieves all employment records for a person.
     *
     * @param personId the person's UUID
     * @return list of employment records
     */
    @Operation(
        summary = "Get employment by person",
        description = "Retrieves all employment records for a person"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employment records retrieved successfully"
        )
    })
    @GetMapping
    ResponseEntity<List<EmploymentDto>> getEmploymentByPersonId(
            @Parameter(description = "Person ID", required = true)
            @RequestParam UUID personId);

    /**
     * Retrieves current employment records for a person.
     *
     * @param personId the person's UUID
     * @return list of current employment records
     */
    @Operation(
        summary = "Get current employment by person",
        description = "Retrieves current employment records (no end date) for a person"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Current employment records retrieved successfully"
        )
    })
    @GetMapping("/current")
    ResponseEntity<List<EmploymentDto>> getCurrentEmploymentByPersonId(
            @Parameter(description = "Person ID", required = true)
            @RequestParam UUID personId);

    /**
     * Updates an existing employment record.
     *
     * @param id      the employment's UUID
     * @param request the update request containing updated employment details
     * @return the updated employment
     */
    @Operation(
        summary = "Update an employment record",
        description = "Updates an existing employment record"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employment updated successfully",
            content = @Content(schema = @Schema(implementation = EmploymentDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employment or Employer not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<EmploymentDto> updateEmployment(
            @Parameter(description = "Employment ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEmploymentRequest request);

    /**
     * Deletes an employment record from the system.
     *
     * @param id the employment's UUID
     * @return HTTP 204 No Content on success
     */
    @Operation(
        summary = "Delete an employment record",
        description = "Deletes an employment record from the system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Employment deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employment not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEmployment(
            @Parameter(description = "Employment ID", required = true)
            @PathVariable UUID id);
}
