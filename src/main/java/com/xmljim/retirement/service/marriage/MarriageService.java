package com.xmljim.retirement.service.marriage;

import com.xmljim.retirement.api.dto.marriage.CreateMarriageRequest;
import com.xmljim.retirement.api.dto.marriage.MarriageDto;
import com.xmljim.retirement.api.dto.marriage.UpdateMarriageRequest;
import com.xmljim.retirement.domain.marriage.MarriageStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for Marriage management operations.
 *
 * <p>Provides business logic for creating, reading, updating, and
 * deleting marriage records, including Social Security spousal benefit
 * eligibility checks.</p>
 *
 * @since 1.0
 */
public interface MarriageService {

    /**
     * Creates a new marriage record.
     *
     * @param request the creation request
     * @return the created marriage DTO
     * @throws IllegalArgumentException if person IDs are not found or invalid
     */
    MarriageDto createMarriage(CreateMarriageRequest request);

    /**
     * Retrieves a marriage by ID.
     *
     * @param id the marriage ID
     * @return an Optional containing the marriage DTO if found
     */
    Optional<MarriageDto> getMarriageById(UUID id);

    /**
     * Retrieves all marriages.
     *
     * @return list of all marriage DTOs
     */
    List<MarriageDto> getAllMarriages();

    /**
     * Retrieves all marriages involving a specific person.
     *
     * @param personId the ID of the person
     * @return list of marriage DTOs involving the person
     */
    List<MarriageDto> getMarriagesByPersonId(UUID personId);

    /**
     * Retrieves marriages by status.
     *
     * @param status the marriage status to search for
     * @return list of matching marriage DTOs
     */
    List<MarriageDto> getMarriagesByStatus(MarriageStatus status);

    /**
     * Updates an existing marriage record.
     *
     * @param id      the marriage ID
     * @param request the update request
     * @return an Optional containing the updated marriage DTO if found
     */
    Optional<MarriageDto> updateMarriage(UUID id, UpdateMarriageRequest request);

    /**
     * Deletes a marriage by ID.
     *
     * @param id the marriage ID
     * @return true if the marriage was deleted, false if not found
     */
    boolean deleteMarriage(UUID id);

    /**
     * Checks if a person has any active (MARRIED status) marriages.
     *
     * @param personId the ID of the person
     * @return true if the person has an active marriage
     */
    boolean hasActiveMarriage(UUID personId);

    /**
     * Checks if a marriage qualifies for Social Security spousal benefits.
     *
     * <p>SS divorced spouse benefits require the marriage to have lasted
     * at least 10 years.</p>
     *
     * @param marriageId the ID of the marriage
     * @return an Optional containing true if eligible, false if not,
     *         or empty if the marriage is not found
     */
    Optional<Boolean> checkSpousalBenefitEligibility(UUID marriageId);

    /**
     * Retrieves all marriages for a person that qualify for SS spousal benefits.
     *
     * <p>Returns marriages that lasted 10 or more years.</p>
     *
     * @param personId the ID of the person
     * @return list of marriage DTOs eligible for spousal benefits
     */
    List<MarriageDto> getSpousalBenefitEligibleMarriages(UUID personId);
}
