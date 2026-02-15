package com.xmljim.retirement.api.marriage;

import com.xmljim.retirement.api.dto.marriage.CreateMarriageRequest;
import com.xmljim.retirement.api.dto.marriage.MarriageDto;
import com.xmljim.retirement.api.dto.marriage.UpdateMarriageRequest;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

/**
 * API contract for Marriage management operations.
 *
 * <p>Defines the REST API endpoints for managing marriage records in the
 * retirement planning system. Marriage history is important for determining
 * Social Security spousal benefit eligibility.</p>
 *
 * @since 1.0
 */
@Tag(name = "Marriages", description = "Marriage management operations")
@SuppressWarnings("PMD.AvoidDuplicateLiterals") // Swagger response codes are intentionally repeated
public interface MarriageOperations {

    /**
     * Records a new marriage for a person.
     *
     * @param personId the ID of the person to record the marriage for
     * @param request  the creation request containing marriage details
     * @return the created marriage with HTTP 201 status and Location header
     */
    @Operation(
        summary = "Record a marriage for a person",
        description = "Creates a new marriage record for the specified person"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Marriage recorded successfully",
            content = @Content(schema = @Schema(implementation = MarriageDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<MarriageDto> createMarriage(
            @Parameter(description = "Person ID", required = true)
            @PathVariable UUID personId,
            @Valid @RequestBody CreateMarriageRequest request);

    /**
     * Retrieves all marriages for a person.
     *
     * @param personId the ID of the person
     * @return list of marriages for the person
     */
    @Operation(
        summary = "List marriages for a person",
        description = "Retrieves all marriage records involving the specified person"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Marriages retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<List<MarriageDto>> getMarriagesByPersonId(
            @Parameter(description = "Person ID", required = true)
            @PathVariable UUID personId);

    /**
     * Updates an existing marriage record.
     *
     * @param id      the marriage ID
     * @param request the update request containing updated marriage details
     * @return the updated marriage
     */
    @Operation(
        summary = "Update a marriage",
        description = "Updates an existing marriage record (e.g., record divorce)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Marriage updated successfully",
            content = @Content(schema = @Schema(implementation = MarriageDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Marriage not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<MarriageDto> updateMarriage(
            @Parameter(description = "Marriage ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMarriageRequest request);

    /**
     * Retrieves a marriage by its unique identifier.
     *
     * @param id the marriage UUID
     * @return the marriage if found
     */
    @Operation(
        summary = "Get a marriage by ID",
        description = "Retrieves a marriage record by its unique identifier"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Marriage found",
            content = @Content(schema = @Schema(implementation = MarriageDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Marriage not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<MarriageDto> getMarriageById(
            @Parameter(description = "Marriage ID", required = true)
            @PathVariable UUID id);
}
