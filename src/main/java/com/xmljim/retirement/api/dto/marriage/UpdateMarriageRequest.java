package com.xmljim.retirement.api.dto.marriage;

import com.xmljim.retirement.domain.marriage.MarriageStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * Request DTO for updating an existing Marriage record.
 *
 * <p>Note: The persons in a marriage cannot be changed after creation.
 * Only the dates, status, and notes can be updated.</p>
 *
 * @param marriageDate the date of marriage (required, must be past or present)
 * @param divorceDate  the date of divorce (optional, must be after marriage date)
 * @param status       the status of the marriage (required)
 * @param notes        optional notes about the marriage
 * @since 1.0
 */
@Schema(description = "Request to update an existing marriage record")
public record UpdateMarriageRequest(
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
