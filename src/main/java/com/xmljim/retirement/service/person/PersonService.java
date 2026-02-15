package com.xmljim.retirement.service.person;

import com.xmljim.retirement.api.dto.person.CreatePersonRequest;
import com.xmljim.retirement.api.dto.person.PersonDto;
import com.xmljim.retirement.api.dto.person.UpdatePersonRequest;
import com.xmljim.retirement.domain.person.FilingStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for Person management operations.
 *
 * <p>Provides business logic for creating, reading, updating, and
 * deleting person records.</p>
 *
 * @since 1.0
 */
public interface PersonService {

    /**
     * Creates a new person.
     *
     * @param request the creation request
     * @return the created person DTO
     */
    PersonDto createPerson(CreatePersonRequest request);

    /**
     * Retrieves a person by ID.
     *
     * @param id the person ID
     * @return an Optional containing the person DTO if found
     */
    Optional<PersonDto> getPersonById(UUID id);

    /**
     * Retrieves all persons with pagination.
     *
     * @param pageable pagination parameters
     * @return page of person DTOs
     */
    Page<PersonDto> getAllPersons(Pageable pageable);

    /**
     * Retrieves persons by last name.
     *
     * @param lastName the last name to search for
     * @return list of matching person DTOs
     */
    List<PersonDto> getPersonsByLastName(String lastName);

    /**
     * Retrieves persons by filing status.
     *
     * @param filingStatus the filing status to search for
     * @return list of matching person DTOs
     */
    List<PersonDto> getPersonsByFilingStatus(FilingStatus filingStatus);

    /**
     * Updates an existing person.
     *
     * @param id      the person ID
     * @param request the update request
     * @return an Optional containing the updated person DTO if found
     */
    Optional<PersonDto> updatePerson(UUID id, UpdatePersonRequest request);

    /**
     * Deletes a person by ID.
     *
     * @param id the person ID
     * @return true if the person was deleted, false if not found
     */
    boolean deletePerson(UUID id);

    /**
     * Checks if a person exists with the given attributes.
     *
     * @param firstName   the first name
     * @param lastName    the last name
     * @param dateOfBirth the date of birth
     * @return true if a matching person exists
     */
    boolean existsByNameAndDateOfBirth(String firstName, String lastName,
                                        LocalDate dateOfBirth);
}
