package com.xmljim.retirement.service.income;

import com.xmljim.retirement.api.dto.income.CreateIncomeRequest;
import com.xmljim.retirement.api.dto.income.IncomeDto;
import com.xmljim.retirement.api.dto.income.UpdateIncomeRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for Income management operations.
 *
 * <p>Provides business logic for creating, reading, updating, and
 * deleting income records.</p>
 *
 * @since 1.0
 */
public interface IncomeService {

    /**
     * Creates a new income record.
     *
     * @param request the creation request
     * @return the created income DTO
     */
    IncomeDto createIncome(CreateIncomeRequest request);

    /**
     * Retrieves an income by ID.
     *
     * @param id the income ID
     * @return an Optional containing the income DTO if found
     */
    Optional<IncomeDto> getIncomeById(UUID id);

    /**
     * Retrieves all income records for an employment.
     *
     * @param employmentId the employment ID
     * @return list of income DTOs
     */
    List<IncomeDto> getIncomeByEmploymentId(UUID employmentId);

    /**
     * Retrieves income for a specific employment and year.
     *
     * @param employmentId the employment ID
     * @param year         the tax year
     * @return an Optional containing the income DTO if found
     */
    Optional<IncomeDto> getIncomeByEmploymentIdAndYear(UUID employmentId, int year);

    /**
     * Retrieves all income records for a person.
     *
     * @param personId the person's ID
     * @return list of income DTOs across all employments
     */
    List<IncomeDto> getIncomeByPersonId(UUID personId);

    /**
     * Retrieves all income records for a person in a specific year.
     *
     * @param personId the person's ID
     * @param year     the tax year
     * @return list of income DTOs for the year
     */
    List<IncomeDto> getIncomeByPersonIdAndYear(UUID personId, int year);

    /**
     * Updates an existing income record.
     *
     * @param id      the income ID
     * @param request the update request
     * @return an Optional containing the updated income DTO if found
     */
    Optional<IncomeDto> updateIncome(UUID id, UpdateIncomeRequest request);

    /**
     * Deletes an income record by ID.
     *
     * @param id the income ID
     * @return true if the income was deleted, false if not found
     */
    boolean deleteIncome(UUID id);
}
