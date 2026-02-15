package com.xmljim.retirement.api.contribution;

import com.xmljim.retirement.api.dto.contribution.AccountTypeLimitsDto;
import com.xmljim.retirement.api.dto.contribution.YearlyLimitsDto;
import com.xmljim.retirement.api.exception.ErrorResponse;
import com.xmljim.retirement.domain.contribution.AccountType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * API contract for contribution limits operations.
 *
 * <p>Defines the REST API endpoints for retrieving IRS contribution limits
 * and phase-out ranges for various retirement account types. This interface
 * contains all OpenAPI/Swagger documentation annotations, separating the
 * API contract from the implementation.</p>
 *
 * @since 1.0
 */
@Tag(name = "Contribution Limits", description = "IRS contribution limits and phase-out ranges")
@RequestMapping("/api/v1/limits")
public interface LimitsOperations {

    /**
     * Retrieves all contribution limits and phase-out ranges for a given year.
     *
     * @param year the tax year
     * @return the yearly limits data
     */
    @Operation(
        summary = "Get all limits for a year",
        description = "Retrieves all IRS contribution limits and phase-out ranges for a specific tax year"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Limits retrieved successfully",
            content = @Content(schema = @Schema(implementation = YearlyLimitsDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No limit data found for the specified year",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{year}")
    ResponseEntity<YearlyLimitsDto> getLimitsByYear(
            @Parameter(description = "Tax year (e.g., 2025)", required = true, example = "2025")
            @PathVariable Integer year);

    /**
     * Retrieves contribution limits for a specific account type and year.
     *
     * @param year        the tax year
     * @param accountType the account type
     * @return the account type limits
     */
    @Operation(
        summary = "Get limits for a specific account type",
        description = "Retrieves contribution limits for a specific account type and tax year"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account type limits retrieved successfully",
            content = @Content(schema = @Schema(implementation = AccountTypeLimitsDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No limit data found for the specified year and account type",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{year}/{accountType}")
    ResponseEntity<AccountTypeLimitsDto> getLimitsByYearAndAccountType(
            @Parameter(description = "Tax year (e.g., 2025)", required = true, example = "2025")
            @PathVariable Integer year,
            @Parameter(description = "Account type", required = true)
            @PathVariable AccountType accountType);

    /**
     * Retrieves all years that have contribution limit data available.
     *
     * @return list of years with data, sorted descending
     */
    @Operation(
        summary = "Get available years",
        description = "Returns a list of all tax years that have contribution limit data available"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Available years retrieved successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(
                    implementation = Integer.class, example = "2025")))
        )
    })
    @GetMapping("/years")
    ResponseEntity<List<Integer>> getAvailableYears();
}
