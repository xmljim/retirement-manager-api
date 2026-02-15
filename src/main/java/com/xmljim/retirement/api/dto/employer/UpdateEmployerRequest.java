package com.xmljim.retirement.api.dto.employer;

import com.xmljim.retirement.domain.employer.Employer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating an existing Employer.
 *
 * @param name         the employer name (required)
 * @param ein          the Employer Identification Number (optional)
 * @param addressLine1 address line 1 (optional)
 * @param addressLine2 address line 2 (optional)
 * @param city         the city (optional)
 * @param state        the two-letter state code (optional)
 * @param zipCode      the ZIP code (optional)
 * @since 1.0
 */
@Schema(description = "Request to update an existing employer")
public record UpdateEmployerRequest(
        @Schema(description = "Employer name", example = "Acme Corporation")
        @NotBlank(message = "Employer name is required")
        @Size(max = Employer.MAX_NAME_LENGTH, message = "Name must not exceed 200 characters")
        String name,

        @Schema(description = "Employer Identification Number", example = "12-3456789")
        @Pattern(regexp = "^\\d{2}-\\d{7}$", message = "EIN must be in XX-XXXXXXX format")
        String ein,

        @Schema(description = "Address line 1", example = "123 Main Street")
        @Size(max = Employer.MAX_ADDRESS_LENGTH, message = "Address must not exceed 200 characters")
        String addressLine1,

        @Schema(description = "Address line 2", example = "Suite 100")
        @Size(max = Employer.MAX_ADDRESS_LENGTH, message = "Address must not exceed 200 characters")
        String addressLine2,

        @Schema(description = "City", example = "San Francisco")
        @Size(max = Employer.MAX_CITY_LENGTH, message = "City must not exceed 100 characters")
        String city,

        @Schema(description = "Two-letter US state code", example = "CA")
        @Pattern(regexp = "^[A-Z]{2}$", message = "State must be a valid 2-letter code")
        String state,

        @Schema(description = "ZIP code", example = "94102")
        @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "ZIP code must be in XXXXX or XXXXX-XXXX format")
        String zipCode
) {
}
