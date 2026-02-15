package com.xmljim.retirement.api.dto.contribution;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Data transfer object containing all contribution limits and phase-out ranges for a year.
 *
 * @param year               the tax year
 * @param contributionLimits list of contribution limits for the year
 * @param phaseOutRanges     list of phase-out ranges for the year
 * @since 1.0
 */
@Schema(description = "All contribution limits and phase-out ranges for a tax year")
public record YearlyLimitsDto(
        @Schema(description = "Tax year", example = "2025")
        Integer year,

        @Schema(description = "Contribution limits for all account types")
        List<ContributionLimitDto> contributionLimits,

        @Schema(description = "Phase-out ranges for IRA accounts")
        List<PhaseOutRangeDto> phaseOutRanges
) {
    /**
     * Compact constructor that creates defensive copies of mutable fields.
     *
     * @param year               the tax year
     * @param contributionLimits the contribution limits (defensively copied)
     * @param phaseOutRanges     the phase-out ranges (defensively copied)
     */
    public YearlyLimitsDto {
        contributionLimits = contributionLimits == null ? List.of() : List.copyOf(contributionLimits);
        phaseOutRanges = phaseOutRanges == null ? List.of() : List.copyOf(phaseOutRanges);
    }

    /**
     * Creates a YearlyLimitsDto from lists of contribution limits and phase-out ranges.
     *
     * @param limitsYear         the tax year
     * @param limits             the contribution limits
     * @param phaseOuts          the phase-out ranges
     * @return the combined DTO
     */
    public static YearlyLimitsDto create(final Integer limitsYear,
                                          final List<ContributionLimitDto> limits,
                                          final List<PhaseOutRangeDto> phaseOuts) {
        return new YearlyLimitsDto(limitsYear, limits, phaseOuts);
    }
}
