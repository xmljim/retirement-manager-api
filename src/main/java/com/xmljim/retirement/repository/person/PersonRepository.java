package com.xmljim.retirement.repository.person;

import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link Person} entities.
 *
 * <p>Provides CRUD operations and custom queries for person management.</p>
 *
 * @since 1.0
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    /**
     * Finds all persons with the given last name.
     *
     * @param lastName the last name to search for
     * @return list of persons with the matching last name
     */
    List<Person> findByLastName(String lastName);

    /**
     * Finds all persons with the given filing status.
     *
     * @param filingStatus the filing status to search for
     * @return list of persons with the matching filing status
     */
    List<Person> findByFilingStatus(FilingStatus filingStatus);

    /**
     * Finds all persons residing in the given state.
     *
     * @param stateOfResidence the two-letter state code
     * @return list of persons residing in the given state
     */
    List<Person> findByStateOfResidence(String stateOfResidence);

    /**
     * Checks if a person exists with the given first name, last name, and date of birth.
     *
     * @param firstName   the first name
     * @param lastName    the last name
     * @param dateOfBirth the date of birth
     * @return true if a matching person exists
     */
    boolean existsByFirstNameAndLastNameAndDateOfBirth(
            String firstName,
            String lastName,
            LocalDate dateOfBirth);
}
