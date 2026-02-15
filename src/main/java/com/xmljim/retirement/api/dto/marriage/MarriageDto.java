package com.xmljim.retirement.api.dto.marriage;

import com.xmljim.retirement.domain.marriage.Marriage;
import com.xmljim.retirement.domain.marriage.MarriageStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data transfer object representing a Marriage for API responses.
 *
 * @param id                     the unique identifier
 * @param person1Id              the ID of the first person
 * @param person2Id              the ID of the second person
 * @param marriageDate           the date of marriage
 * @param divorceDate            the date of divorce (null if still married)
 * @param status                 the marriage status
 * @param notes                  optional notes about the marriage
 * @param marriageDurationYears  the duration of the marriage in years
 * @param eligibleForSpousalBenefits whether the marriage qualifies for SS benefits
 * @param createdAt              the creation timestamp
 * @param updatedAt              the last update timestamp
 * @since 1.0
 */
@Schema(description = "Marriage record for tracking relationship history")
public record MarriageDto(
        @Schema(description = "Unique identifier",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "ID of the first person in the marriage",
                example = "550e8400-e29b-41d4-a716-446655440001")
        UUID person1Id,

        @Schema(description = "ID of the second person in the marriage",
                example = "550e8400-e29b-41d4-a716-446655440002")
        UUID person2Id,

        @Schema(description = "Date of marriage", example = "2010-06-15")
        LocalDate marriageDate,

        @Schema(description = "Date of divorce (null if still married)",
                example = "2020-06-15")
        LocalDate divorceDate,

        @Schema(description = "Current status of the marriage")
        MarriageStatus status,

        @Schema(description = "Optional notes about the marriage",
                example = "First marriage")
        String notes,

        @Schema(description = "Duration of marriage in years", example = "10")
        long marriageDurationYears,

        @Schema(description = "Whether eligible for SS spousal benefits (10+ years)",
                example = "true")
        boolean eligibleForSpousalBenefits,

        @Schema(description = "Record creation timestamp")
        Instant createdAt,

        @Schema(description = "Record last update timestamp")
        Instant updatedAt
) {
    /**
     * Creates a MarriageDto from a Marriage entity.
     *
     * @param marriage the marriage entity
     * @return the DTO representation
     */
    public static MarriageDto fromEntity(final Marriage marriage) {
        return new MarriageDto(
                marriage.getId(),
                marriage.getPerson1().getId(),
                marriage.getPerson2().getId(),
                marriage.getMarriageDate(),
                marriage.getDivorceDate(),
                marriage.getStatus(),
                marriage.getNotes(),
                marriage.getMarriageDurationYears(),
                marriage.isEligibleForSpousalBenefits(),
                marriage.getCreatedAt(),
                marriage.getUpdatedAt()
        );
    }
}
