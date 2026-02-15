package com.xmljim.retirement.api.dto.contribution;

import com.xmljim.retirement.domain.contribution.PhaseOutAccountType;
import com.xmljim.retirement.domain.contribution.PhaseOutRange;
import com.xmljim.retirement.domain.person.FilingStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data transfer object representing a phase-out range for API responses.
 *
 * @param id           the unique identifier
 * @param year         the tax year
 * @param filingStatus the tax filing status
 * @param accountType  the account type
 * @param magiStart    the MAGI where phase-out begins
 * @param magiEnd      the MAGI where phase-out is complete
 * @since 1.0
 */
@Schema(description = "MAGI phase-out range for IRA contributions or deductions")
public record PhaseOutRangeDto(
        @Schema(description = "Unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Tax year", example = "2025")
        Integer year,

        @Schema(description = "Tax filing status")
        FilingStatus filingStatus,

        @Schema(description = "Account type for phase-out")
        PhaseOutAccountType accountType,

        @Schema(description = "MAGI where phase-out begins", example = "150000.00")
        BigDecimal magiStart,

        @Schema(description = "MAGI where phase-out is complete", example = "165000.00")
        BigDecimal magiEnd
) {
    /**
     * Creates a PhaseOutRangeDto from a PhaseOutRange entity.
     *
     * @param entity the phase-out range entity
     * @return the DTO representation
     */
    public static PhaseOutRangeDto fromEntity(final PhaseOutRange entity) {
        return new PhaseOutRangeDto(
                entity.getId(),
                entity.getYear(),
                entity.getFilingStatus(),
                entity.getAccountType(),
                entity.getMagiStart(),
                entity.getMagiEnd()
        );
    }
}
