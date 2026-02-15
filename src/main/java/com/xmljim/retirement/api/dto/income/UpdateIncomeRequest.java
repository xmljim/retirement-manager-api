package com.xmljim.retirement.api.dto.income;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * Request DTO for updating an existing Income record.
 *
 * @param annualSalary      the annual base salary (required)
 * @param bonus             the annual bonus (required)
 * @param otherCompensation other compensation (required)
 * @param w2Wages           W-2 wages for SECURE 2.0 calculations (optional)
 * @since 1.0
 */
@Schema(description = "Request to update an existing income record")
public record UpdateIncomeRequest(
        @Schema(description = "Annual base salary", example = "120000.00")
        @NotNull(message = "Annual salary is required")
        @PositiveOrZero(message = "Annual salary must be zero or positive")
        BigDecimal annualSalary,

        @Schema(description = "Annual bonus", example = "15000.00")
        @NotNull(message = "Bonus is required")
        @PositiveOrZero(message = "Bonus must be zero or positive")
        BigDecimal bonus,

        @Schema(description = "Other compensation (stock, commissions, etc.)", example = "5000.00")
        @NotNull(message = "Other compensation is required")
        @PositiveOrZero(message = "Other compensation must be zero or positive")
        BigDecimal otherCompensation,

        @Schema(description = "W-2 wages for SECURE 2.0 high earner rules", example = "140000.00")
        @PositiveOrZero(message = "W-2 wages must be zero or positive")
        BigDecimal w2Wages
) {
}
