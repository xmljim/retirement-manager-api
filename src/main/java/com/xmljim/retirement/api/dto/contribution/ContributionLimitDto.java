package com.xmljim.retirement.api.dto.contribution;

import com.xmljim.retirement.domain.contribution.AccountType;
import com.xmljim.retirement.domain.contribution.ContributionLimit;
import com.xmljim.retirement.domain.contribution.LimitType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data transfer object representing a contribution limit for API responses.
 *
 * @param id          the unique identifier
 * @param year        the tax year
 * @param accountType the account type
 * @param limitType   the type of limit
 * @param amount      the contribution limit amount
 * @since 1.0
 */
@Schema(description = "IRS contribution limit for a specific account type and year")
public record ContributionLimitDto(
        @Schema(description = "Unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Tax year", example = "2025")
        Integer year,

        @Schema(description = "Account type")
        AccountType accountType,

        @Schema(description = "Type of limit (base, catch-up, etc.)")
        LimitType limitType,

        @Schema(description = "Contribution limit amount in dollars", example = "23500.00")
        BigDecimal amount
) {
    /**
     * Creates a ContributionLimitDto from a ContributionLimit entity.
     *
     * @param entity the contribution limit entity
     * @return the DTO representation
     */
    public static ContributionLimitDto fromEntity(final ContributionLimit entity) {
        return new ContributionLimitDto(
                entity.getId(),
                entity.getYear(),
                entity.getAccountType(),
                entity.getLimitType(),
                entity.getAmount()
        );
    }
}
