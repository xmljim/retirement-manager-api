package com.xmljim.retirement.repository.employer;

import com.xmljim.retirement.domain.employer.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link Employer} entities.
 *
 * <p>Provides CRUD operations and custom queries for employer management.</p>
 *
 * @since 1.0
 */
@Repository
public interface EmployerRepository extends JpaRepository<Employer, UUID> {

    /**
     * Finds all employers with names containing the given string (case-insensitive).
     *
     * @param name the name fragment to search for
     * @return list of employers with matching names
     */
    List<Employer> findByNameContainingIgnoreCase(String name);

    /**
     * Finds an employer by EIN.
     *
     * @param ein the Employer Identification Number
     * @return the employer if found
     */
    Optional<Employer> findByEin(String ein);

    /**
     * Checks if an employer exists with the given EIN.
     *
     * @param ein the Employer Identification Number
     * @return true if an employer exists with the EIN
     */
    boolean existsByEin(String ein);

    /**
     * Finds all employers in the given state.
     *
     * @param state the two-letter state code
     * @return list of employers in the given state
     */
    List<Employer> findByState(String state);
}
