package com.xmljim.retirement.api.dto.employment;

import com.xmljim.retirement.domain.employment.Employment;
import com.xmljim.retirement.domain.employment.EmploymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for creating a new Employment.
 *
 * @param personId               the person's ID (required)
 * @param employerId             the employer's ID (required)
 * @param jobTitle               the job title (optional)
 * @param startDate              the employment start date (required)
 * @param endDate                the employment end date (optional)
 * @param employmentType         the type of employment (required)
 * @param retirementPlanEligible whether eligible for retirement plan
 * @since 1.0
 */
@Schema(description = "Request to create a new employment record")
public record CreateEmploymentRequest(
        @Schema(description = "Person ID")
        @NotNull(message = "Person ID is required")
        UUID personId,

        @Schema(description = "Employer ID")
        @NotNull(message = "Employer ID is required")
        UUID employerId,

        @Schema(description = "Job title", example = "Senior Software Engineer")
        @Size(max = Employment.MAX_JOB_TITLE_LENGTH, message = "Job title must not exceed 200 characters")
        String jobTitle,

        @Schema(description = "Start date", example = "2020-01-15")
        @NotNull(message = "Start date is required")
        @PastOrPresent(message = "Start date cannot be in the future")
        LocalDate startDate,

        @Schema(description = "End date (omit if current)", example = "2023-06-30")
        LocalDate endDate,

        @Schema(description = "Employment type")
        @NotNull(message = "Employment type is required")
        EmploymentType employmentType,

        @Schema(description = "Eligible for employer retirement plan", defaultValue = "true")
        Boolean retirementPlanEligible
) {
    /**
     * Returns whether the person is retirement plan eligible, defaulting to true.
     *
     * @return true if eligible (or not specified)
     */
    public boolean isRetirementPlanEligible() {
        return retirementPlanEligible == null || retirementPlanEligible;
    }
}
