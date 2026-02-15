package com.xmljim.retirement.api.dto.income;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating a new Income record.
 *
 * @param employmentId      the employment ID (required)
 * @param year              the tax year (required)
 * @param annualSalary      the annual base salary (required)
 * @param bonus             the annual bonus (optional, defaults to 0)
 * @param otherCompensation other compensation (optional, defaults to 0)
 * @param w2Wages           W-2 wages for SECURE 2.0 calculations (optional)
 * @since 1.0
 */
@Schema(description = "Request to create a new income record")
public record CreateIncomeRequest(
        @Schema(description = "Employment ID")
        @NotNull(message = "Employment ID is required")
        UUID employmentId,

        @Schema(description = "Tax year", example = "2024")
        @NotNull(message = "Year is required")
        @Min(value = MIN_YEAR, message = "Year must be 1900 or later")
        @Max(value = MAX_YEAR, message = "Year must be 2100 or earlier")
        Integer year,

        @Schema(description = "Annual base salary", example = "120000.00")
        @NotNull(message = "Annual salary is required")
        @PositiveOrZero(message = "Annual salary must be zero or positive")
        BigDecimal annualSalary,

        @Schema(description = "Annual bonus", example = "15000.00")
        @PositiveOrZero(message = "Bonus must be zero or positive")
        BigDecimal bonus,

        @Schema(description = "Other compensation (stock, commissions, etc.)", example = "5000.00")
        @PositiveOrZero(message = "Other compensation must be zero or positive")
        BigDecimal otherCompensation,

        @Schema(description = "W-2 wages for SECURE 2.0 high earner rules", example = "140000.00")
        @PositiveOrZero(message = "W-2 wages must be zero or positive")
        BigDecimal w2Wages
) {
    /** Minimum valid year for income records. */
    public static final int MIN_YEAR = 1900;

    /** Maximum valid year for income records. */
    public static final int MAX_YEAR = 2100;

    /**
     * Returns bonus, defaulting to zero if not specified.
     *
     * @return the bonus amount
     */
    public BigDecimal getBonus() {
        return bonus != null ? bonus : BigDecimal.ZERO;
    }

    /**
     * Returns other compensation, defaulting to zero if not specified.
     *
     * @return the other compensation amount
     */
    public BigDecimal getOtherCompensation() {
        return otherCompensation != null ? otherCompensation : BigDecimal.ZERO;
    }
}
