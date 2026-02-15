package com.xmljim.retirement.repository.employment;

import com.xmljim.retirement.domain.employment.Employment;
import com.xmljim.retirement.domain.employment.EmploymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link Employment} entities.
 *
 * <p>Provides CRUD operations and custom queries for employment management.</p>
 *
 * @since 1.0
 */
@Repository
public interface EmploymentRepository extends JpaRepository<Employment, UUID> {

    /**
     * Finds all employment records for a person.
     *
     * @param personId the person's ID
     * @return list of employment records
     */
    List<Employment> findByPersonId(UUID personId);

    /**
     * Finds all employment records for an employer.
     *
     * @param employerId the employer's ID
     * @return list of employment records
     */
    List<Employment> findByEmployerId(UUID employerId);

    /**
     * Finds current employment records for a person (no end date).
     *
     * @param personId the person's ID
     * @return list of current employment records
     */
    @Query("SELECT e FROM Employment e WHERE e.person.id = :personId AND e.endDate IS NULL")
    List<Employment> findCurrentByPersonId(@Param("personId") UUID personId);

    /**
     * Finds all employment records of a specific type for a person.
     *
     * @param personId       the person's ID
     * @param employmentType the type of employment
     * @return list of matching employment records
     */
    List<Employment> findByPersonIdAndEmploymentType(UUID personId, EmploymentType employmentType);

    /**
     * Finds all retirement-plan-eligible employment records for a person.
     *
     * @param personId the person's ID
     * @return list of retirement-plan-eligible employment records
     */
    @Query("SELECT e FROM Employment e WHERE e.person.id = :personId "
            + "AND e.retirementPlanEligible = true AND e.endDate IS NULL")
    List<Employment> findCurrentRetirementEligibleByPersonId(@Param("personId") UUID personId);
}
