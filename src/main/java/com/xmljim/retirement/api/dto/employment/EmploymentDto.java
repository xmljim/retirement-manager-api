package com.xmljim.retirement.api.dto.employment;

import com.xmljim.retirement.domain.employment.Employment;
import com.xmljim.retirement.domain.employment.EmploymentType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data transfer object representing an Employment for API responses.
 *
 * @param id                      the unique identifier
 * @param personId                the person's ID
 * @param employerId              the employer's ID
 * @param employerName            the employer's name (denormalized for convenience)
 * @param jobTitle                the job title
 * @param startDate               the employment start date
 * @param endDate                 the employment end date (null if current)
 * @param employmentType          the type of employment
 * @param retirementPlanEligible  whether eligible for retirement plan
 * @param current                 whether this is current employment
 * @param createdAt               the creation timestamp
 * @param updatedAt               the last update timestamp
 * @since 1.0
 */
@Schema(description = "Employment information")
public record EmploymentDto(
        @Schema(description = "Unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Person ID")
        UUID personId,

        @Schema(description = "Employer ID")
        UUID employerId,

        @Schema(description = "Employer name", example = "Acme Corporation")
        String employerName,

        @Schema(description = "Job title", example = "Senior Software Engineer")
        String jobTitle,

        @Schema(description = "Start date", example = "2020-01-15")
        LocalDate startDate,

        @Schema(description = "End date (null if current)", example = "2023-06-30")
        LocalDate endDate,

        @Schema(description = "Employment type")
        EmploymentType employmentType,

        @Schema(description = "Eligible for employer retirement plan")
        boolean retirementPlanEligible,

        @Schema(description = "Currently employed here")
        boolean current,

        @Schema(description = "Record creation timestamp")
        Instant createdAt,

        @Schema(description = "Record last update timestamp")
        Instant updatedAt
) {
    /**
     * Creates an EmploymentDto from an Employment entity.
     *
     * @param employment the employment entity
     * @return the DTO representation
     */
    public static EmploymentDto fromEntity(final Employment employment) {
        return new EmploymentDto(
                employment.getId(),
                employment.getPerson().getId(),
                employment.getEmployer().getId(),
                employment.getEmployer().getName(),
                employment.getJobTitle(),
                employment.getStartDate(),
                employment.getEndDate(),
                employment.getEmploymentType(),
                employment.isRetirementPlanEligible(),
                employment.isCurrent(),
                employment.getCreatedAt(),
                employment.getUpdatedAt()
        );
    }
}
