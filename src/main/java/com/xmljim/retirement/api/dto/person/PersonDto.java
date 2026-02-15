package com.xmljim.retirement.api.dto.person;

import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data transfer object representing a Person for API responses.
 *
 * @param id              the unique identifier
 * @param firstName       the first name
 * @param lastName        the last name
 * @param dateOfBirth     the date of birth
 * @param filingStatus    the tax filing status
 * @param stateOfResidence the two-letter state code of residence
 * @param createdAt       the creation timestamp
 * @param updatedAt       the last update timestamp
 * @since 1.0
 */
@Schema(description = "Person information for retirement planning")
public record PersonDto(
        @Schema(description = "Unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "First name", example = "John")
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        String lastName,

        @Schema(description = "Date of birth", example = "1980-05-15")
        LocalDate dateOfBirth,

        @Schema(description = "Tax filing status")
        FilingStatus filingStatus,

        @Schema(description = "Two-letter US state code", example = "CA")
        String stateOfResidence,

        @Schema(description = "Record creation timestamp")
        Instant createdAt,

        @Schema(description = "Record last update timestamp")
        Instant updatedAt
) {
    /**
     * Creates a PersonDto from a Person entity.
     *
     * @param person the person entity
     * @return the DTO representation
     */
    public static PersonDto fromEntity(final Person person) {
        return new PersonDto(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getDateOfBirth(),
                person.getFilingStatus(),
                person.getStateOfResidence(),
                person.getCreatedAt(),
                person.getUpdatedAt()
        );
    }
}
