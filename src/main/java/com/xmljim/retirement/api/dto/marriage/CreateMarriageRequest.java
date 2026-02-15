package com.xmljim.retirement.api.dto.marriage;

import com.xmljim.retirement.domain.marriage.MarriageStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for creating a new Marriage record.
 *
 * @param person1Id    the ID of the first person (required)
 * @param person2Id    the ID of the second person (required)
 * @param marriageDate the date of marriage (required, must be past or present)
 * @param divorceDate  the date of divorce (optional, must be after marriage date)
 * @param status       the status of the marriage (required)
 * @param notes        optional notes about the marriage
 * @since 1.0
 */
@Schema(description = "Request to create a new marriage record")
public record CreateMarriageRequest(
        @Schema(description = "ID of the first person in the marriage",
                example = "550e8400-e29b-41d4-a716-446655440001")
        @NotNull(message = "Person 1 ID is required")
        UUID person1Id,

        @Schema(description = "ID of the second person in the marriage",
                example = "550e8400-e29b-41d4-a716-446655440002")
        @NotNull(message = "Person 2 ID is required")
        UUID person2Id,

        @Schema(description = "Date of marriage", example = "2010-06-15")
        @NotNull(message = "Marriage date is required")
        @PastOrPresent(message = "Marriage date must be in the past or present")
        LocalDate marriageDate,

        @Schema(description = "Date of divorce (optional)", example = "2020-06-15")
        LocalDate divorceDate,

        @Schema(description = "Status of the marriage")
        @NotNull(message = "Marriage status is required")
        MarriageStatus status,

        @Schema(description = "Optional notes about the marriage",
                example = "First marriage")
        String notes
) {
}
