package com.xmljim.retirement.api.dto.employer;

import com.xmljim.retirement.domain.employer.Employer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object representing an Employer for API responses.
 *
 * @param id           the unique identifier
 * @param name         the employer name
 * @param ein          the Employer Identification Number
 * @param addressLine1 address line 1
 * @param addressLine2 address line 2
 * @param city         the city
 * @param state        the two-letter state code
 * @param zipCode      the ZIP code
 * @param createdAt    the creation timestamp
 * @param updatedAt    the last update timestamp
 * @since 1.0
 */
@Schema(description = "Employer information")
public record EmployerDto(
        @Schema(description = "Unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Employer name", example = "Acme Corporation")
        String name,

        @Schema(description = "Employer Identification Number", example = "12-3456789")
        String ein,

        @Schema(description = "Address line 1", example = "123 Main Street")
        String addressLine1,

        @Schema(description = "Address line 2", example = "Suite 100")
        String addressLine2,

        @Schema(description = "City", example = "San Francisco")
        String city,

        @Schema(description = "Two-letter US state code", example = "CA")
        String state,

        @Schema(description = "ZIP code", example = "94102")
        String zipCode,

        @Schema(description = "Record creation timestamp")
        Instant createdAt,

        @Schema(description = "Record last update timestamp")
        Instant updatedAt
) {
    /**
     * Creates an EmployerDto from an Employer entity.
     *
     * @param employer the employer entity
     * @return the DTO representation
     */
    public static EmployerDto fromEntity(final Employer employer) {
        return new EmployerDto(
                employer.getId(),
                employer.getName(),
                employer.getEin(),
                employer.getAddressLine1(),
                employer.getAddressLine2(),
                employer.getCity(),
                employer.getState(),
                employer.getZipCode(),
                employer.getCreatedAt(),
                employer.getUpdatedAt()
        );
    }
}
