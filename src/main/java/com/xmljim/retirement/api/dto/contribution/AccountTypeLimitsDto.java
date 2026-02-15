package com.xmljim.retirement.api.dto.contribution;

import com.xmljim.retirement.domain.contribution.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Data transfer object containing contribution limits for a specific account type.
 *
 * @param year          the tax year
 * @param accountType   the account type
 * @param limits        list of contribution limits (base, catch-up, etc.)
 * @since 1.0
 */
@Schema(description = "Contribution limits for a specific account type and year")
public record AccountTypeLimitsDto(
        @Schema(description = "Tax year", example = "2025")
        Integer year,

        @Schema(description = "Account type")
        AccountType accountType,

        @Schema(description = "All applicable limits for this account type")
        List<ContributionLimitDto> limits
) {
    /**
     * Compact constructor that creates defensive copies of mutable fields.
     *
     * @param year        the tax year
     * @param accountType the account type
     * @param limits      the contribution limits (defensively copied)
     */
    public AccountTypeLimitsDto {
        limits = limits == null ? List.of() : List.copyOf(limits);
    }

    /**
     * Creates an AccountTypeLimitsDto.
     *
     * @param limitsYear  the tax year
     * @param type        the account type
     * @param limitsList  the contribution limits
     * @return the DTO
     */
    public static AccountTypeLimitsDto create(final Integer limitsYear,
                                               final AccountType type,
                                               final List<ContributionLimitDto> limitsList) {
        return new AccountTypeLimitsDto(limitsYear, type, limitsList);
    }
}
