package com.xmljim.retirement.api.income;

import com.xmljim.retirement.api.dto.income.CreateIncomeRequest;
import com.xmljim.retirement.api.dto.income.IncomeDto;
import com.xmljim.retirement.api.dto.income.UpdateIncomeRequest;
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

import java.util.List;
import java.util.UUID;

/**
 * API contract for Income management operations.
 *
 * <p>Defines the REST API endpoints for managing income records in the retirement
 * planning system. This interface contains all OpenAPI/Swagger documentation annotations,
 * separating the API contract from the implementation.</p>
 *
 * @since 1.0
 */
@Tag(name = "Income", description = "Income management operations")
@RequestMapping("/api/v1/income")
@SuppressWarnings("PMD.AvoidDuplicateLiterals") // Swagger response codes are intentionally repeated
public interface IncomeOperations {

    /**
     * Creates a new income record.
     *
     * @param request the creation request containing income details
     * @return the created income with HTTP 201 status and Location header
     */
    @Operation(
        summary = "Create an income record",
        description = "Creates a new annual income record for an employment"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Income created successfully",
            content = @Content(schema = @Schema(implementation = IncomeDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employment not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<IncomeDto> createIncome(
            @Valid @RequestBody CreateIncomeRequest request);

    /**
     * Retrieves an income record by its unique identifier.
     *
     * @param id the income's UUID
     * @return the income if found
     */
    @Operation(
        summary = "Get an income by ID",
        description = "Retrieves an income record by its unique identifier"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Income found",
            content = @Content(schema = @Schema(implementation = IncomeDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Income not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<IncomeDto> getIncomeById(
            @Parameter(description = "Income ID", required = true)
            @PathVariable UUID id);

    /**
     * Retrieves all income records for an employment.
     *
     * @param employmentId the employment's UUID
     * @return list of income records
     */
    @Operation(
        summary = "Get income by employment",
        description = "Retrieves all income records for an employment"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Income records retrieved successfully"
        )
    })
    @GetMapping("/employment/{employmentId}")
    ResponseEntity<List<IncomeDto>> getIncomeByEmploymentId(
            @Parameter(description = "Employment ID", required = true)
            @PathVariable UUID employmentId);

    /**
     * Retrieves all income records for a person.
     *
     * @param personId the person's UUID
     * @return list of income records across all employments
     */
    @Operation(
        summary = "Get income by person",
        description = "Retrieves all income records for a person across all employments"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Income records retrieved successfully"
        )
    })
    @GetMapping("/person/{personId}")
    ResponseEntity<List<IncomeDto>> getIncomeByPersonId(
            @Parameter(description = "Person ID", required = true)
            @PathVariable UUID personId);

    /**
     * Retrieves all income records for a person in a specific year.
     *
     * @param personId the person's UUID
     * @param year     the tax year
     * @return list of income records for the year
     */
    @Operation(
        summary = "Get income by person and year",
        description = "Retrieves all income records for a person in a specific year"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Income records retrieved successfully"
        )
    })
    @GetMapping("/person/{personId}/year/{year}")
    ResponseEntity<List<IncomeDto>> getIncomeByPersonIdAndYear(
            @Parameter(description = "Person ID", required = true)
            @PathVariable UUID personId,
            @Parameter(description = "Tax year", required = true)
            @PathVariable int year);

    /**
     * Updates an existing income record.
     *
     * @param id      the income's UUID
     * @param request the update request containing updated income details
     * @return the updated income
     */
    @Operation(
        summary = "Update an income record",
        description = "Updates an existing income record"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Income updated successfully",
            content = @Content(schema = @Schema(implementation = IncomeDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Income not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<IncomeDto> updateIncome(
            @Parameter(description = "Income ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdateIncomeRequest request);

    /**
     * Deletes an income record from the system.
     *
     * @param id the income's UUID
     * @return HTTP 204 No Content on success
     */
    @Operation(
        summary = "Delete an income record",
        description = "Deletes an income record from the system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Income deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Income not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteIncome(
            @Parameter(description = "Income ID", required = true)
            @PathVariable UUID id);
}
