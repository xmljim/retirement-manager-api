package com.xmljim.retirement.service.employment;

import com.xmljim.retirement.api.dto.employment.CreateEmploymentRequest;
import com.xmljim.retirement.api.dto.employment.EmploymentDto;
import com.xmljim.retirement.api.dto.employment.UpdateEmploymentRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for Employment management operations.
 *
 * <p>Provides business logic for creating, reading, updating, and
 * deleting employment records.</p>
 *
 * @since 1.0
 */
public interface EmploymentService {

    /**
     * Creates a new employment record.
     *
     * @param request the creation request
     * @return the created employment DTO
     */
    EmploymentDto createEmployment(CreateEmploymentRequest request);

    /**
     * Retrieves an employment by ID.
     *
     * @param id the employment ID
     * @return an Optional containing the employment DTO if found
     */
    Optional<EmploymentDto> getEmploymentById(UUID id);

    /**
     * Retrieves all employment records for a person.
     *
     * @param personId the person's ID
     * @return list of employment DTOs
     */
    List<EmploymentDto> getEmploymentByPersonId(UUID personId);

    /**
     * Retrieves current employment records for a person.
     *
     * @param personId the person's ID
     * @return list of current employment DTOs
     */
    List<EmploymentDto> getCurrentEmploymentByPersonId(UUID personId);

    /**
     * Updates an existing employment record.
     *
     * @param id      the employment ID
     * @param request the update request
     * @return an Optional containing the updated employment DTO if found
     */
    Optional<EmploymentDto> updateEmployment(UUID id, UpdateEmploymentRequest request);

    /**
     * Deletes an employment record by ID.
     *
     * @param id the employment ID
     * @return true if the employment was deleted, false if not found
     */
    boolean deleteEmployment(UUID id);
}
