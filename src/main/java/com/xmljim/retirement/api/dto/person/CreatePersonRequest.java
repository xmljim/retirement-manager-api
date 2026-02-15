package com.xmljim.retirement.api.dto.person;

import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for creating a new Person.
 *
 * @param firstName       the first name (required)
 * @param lastName        the last name (required)
 * @param dateOfBirth     the date of birth (required, must be in the past)
 * @param filingStatus    the tax filing status (required)
 * @param stateOfResidence the two-letter state code (optional)
 * @since 1.0
 */
@Schema(description = "Request to create a new person")
public record CreatePersonRequest(
        @Schema(description = "First name", example = "John")
        @NotBlank(message = "First name is required")
        @Size(max = Person.MAX_NAME_LENGTH, message = "First name must not exceed 100 characters")
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        @NotBlank(message = "Last name is required")
        @Size(max = Person.MAX_NAME_LENGTH, message = "Last name must not exceed 100 characters")
        String lastName,

        @Schema(description = "Date of birth", example = "1980-05-15")
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @Schema(description = "Tax filing status")
        @NotNull(message = "Filing status is required")
        FilingStatus filingStatus,

        @Schema(description = "Two-letter US state code", example = "CA")
        @Pattern(regexp = "^[A-Z]{2}$", message = "State must be a valid 2-letter code")
        String stateOfResidence
) {
}
