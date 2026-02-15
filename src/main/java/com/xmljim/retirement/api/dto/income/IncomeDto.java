package com.xmljim.retirement.api.dto.income;

import com.xmljim.retirement.domain.income.Income;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object representing an Income for API responses.
 *
 * @param id                the unique identifier
 * @param employmentId      the employment ID
 * @param year              the tax year
 * @param annualSalary      the annual base salary
 * @param bonus             the annual bonus
 * @param otherCompensation other compensation
 * @param w2Wages           W-2 wages for SECURE 2.0 calculations
 * @param totalCompensation calculated total compensation
 * @param createdAt         the creation timestamp
 * @param updatedAt         the last update timestamp
 * @since 1.0
 */
@Schema(description = "Income information for a tax year")
public record IncomeDto(
        @Schema(description = "Unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Employment ID")
        UUID employmentId,

        @Schema(description = "Tax year", example = "2024")
        int year,

        @Schema(description = "Annual base salary", example = "120000.00")
        BigDecimal annualSalary,

        @Schema(description = "Annual bonus", example = "15000.00")
        BigDecimal bonus,

        @Schema(description = "Other compensation (stock, commissions, etc.)", example = "5000.00")
        BigDecimal otherCompensation,

        @Schema(description = "W-2 wages for SECURE 2.0 high earner rules", example = "140000.00")
        BigDecimal w2Wages,

        @Schema(description = "Total compensation (salary + bonus + other)", example = "140000.00")
        BigDecimal totalCompensation,

        @Schema(description = "Record creation timestamp")
        Instant createdAt,

        @Schema(description = "Record last update timestamp")
        Instant updatedAt
) {
    /**
     * Creates an IncomeDto from an Income entity.
     *
     * @param income the income entity
     * @return the DTO representation
     */
    public static IncomeDto fromEntity(final Income income) {
        return new IncomeDto(
                income.getId(),
                income.getEmployment().getId(),
                income.getYear(),
                income.getAnnualSalary(),
                income.getBonus(),
                income.getOtherCompensation(),
                income.getW2Wages(),
                income.getTotalCompensation(),
                income.getCreatedAt(),
                income.getUpdatedAt()
        );
    }
}
