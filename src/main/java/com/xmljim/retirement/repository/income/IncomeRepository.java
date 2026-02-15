package com.xmljim.retirement.repository.income;

import com.xmljim.retirement.domain.income.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link Income} entities.
 *
 * <p>Provides CRUD operations and custom queries for income management.</p>
 *
 * @since 1.0
 */
@Repository
public interface IncomeRepository extends JpaRepository<Income, UUID> {

    /**
     * Finds all income records for an employment.
     *
     * @param employmentId the employment ID
     * @return list of income records
     */
    List<Income> findByEmploymentId(UUID employmentId);

    /**
     * Finds income for a specific employment and year.
     *
     * @param employmentId the employment ID
     * @param year         the tax year
     * @return the income record if found
     */
    Optional<Income> findByEmploymentIdAndYear(UUID employmentId, int year);

    /**
     * Finds all income records for a specific year.
     *
     * @param year the tax year
     * @return list of income records for the year
     */
    List<Income> findByYear(int year);

    /**
     * Finds all income records for a person across all employments.
     *
     * @param personId the person's ID
     * @return list of income records
     */
    @Query("SELECT i FROM Income i WHERE i.employment.person.id = :personId")
    List<Income> findByPersonId(@Param("personId") UUID personId);

    /**
     * Finds all income records for a person in a specific year.
     *
     * @param personId the person's ID
     * @param year     the tax year
     * @return list of income records
     */
    @Query("SELECT i FROM Income i WHERE i.employment.person.id = :personId AND i.year = :year")
    List<Income> findByPersonIdAndYear(@Param("personId") UUID personId, @Param("year") int year);

    /**
     * Checks if an income record exists for an employment and year.
     *
     * @param employmentId the employment ID
     * @param year         the tax year
     * @return true if an income record exists
     */
    boolean existsByEmploymentIdAndYear(UUID employmentId, int year);
}
