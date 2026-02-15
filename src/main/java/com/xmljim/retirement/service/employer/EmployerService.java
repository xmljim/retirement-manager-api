package com.xmljim.retirement.service.employer;

import com.xmljim.retirement.api.dto.employer.CreateEmployerRequest;
import com.xmljim.retirement.api.dto.employer.EmployerDto;
import com.xmljim.retirement.api.dto.employer.UpdateEmployerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for Employer management operations.
 *
 * <p>Provides business logic for creating, reading, updating, and
 * deleting employer records.</p>
 *
 * @since 1.0
 */
public interface EmployerService {

    /**
     * Creates a new employer.
     *
     * @param request the creation request
     * @return the created employer DTO
     */
    EmployerDto createEmployer(CreateEmployerRequest request);

    /**
     * Retrieves an employer by ID.
     *
     * @param id the employer ID
     * @return an Optional containing the employer DTO if found
     */
    Optional<EmployerDto> getEmployerById(UUID id);

    /**
     * Retrieves all employers with pagination.
     *
     * @param pageable pagination parameters
     * @return page of employer DTOs
     */
    Page<EmployerDto> getAllEmployers(Pageable pageable);

    /**
     * Searches employers by name.
     *
     * @param name the name fragment to search for
     * @return list of matching employer DTOs
     */
    List<EmployerDto> searchEmployersByName(String name);

    /**
     * Retrieves an employer by EIN.
     *
     * @param ein the Employer Identification Number
     * @return an Optional containing the employer DTO if found
     */
    Optional<EmployerDto> getEmployerByEin(String ein);

    /**
     * Updates an existing employer.
     *
     * @param id      the employer ID
     * @param request the update request
     * @return an Optional containing the updated employer DTO if found
     */
    Optional<EmployerDto> updateEmployer(UUID id, UpdateEmployerRequest request);

    /**
     * Deletes an employer by ID.
     *
     * @param id the employer ID
     * @return true if the employer was deleted, false if not found
     */
    boolean deleteEmployer(UUID id);
}
