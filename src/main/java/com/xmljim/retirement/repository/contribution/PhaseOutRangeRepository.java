package com.xmljim.retirement.repository.contribution;

import com.xmljim.retirement.domain.contribution.PhaseOutAccountType;
import com.xmljim.retirement.domain.contribution.PhaseOutRange;
import com.xmljim.retirement.domain.person.FilingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link PhaseOutRange} entities.
 *
 * <p>Provides CRUD operations and custom queries for phase-out range data.</p>
 *
 * @since 1.0
 */
@Repository
public interface PhaseOutRangeRepository extends JpaRepository<PhaseOutRange, UUID> {

    /**
     * Finds all phase-out ranges for a given year.
     *
     * @param year the tax year
     * @return list of phase-out ranges for the year
     */
    List<PhaseOutRange> findByYear(Integer year);

    /**
     * Finds all phase-out ranges for a given filing status.
     *
     * @param filingStatus the filing status
     * @return list of phase-out ranges for the filing status
     */
    List<PhaseOutRange> findByFilingStatus(FilingStatus filingStatus);

    /**
     * Finds all phase-out ranges for a given account type.
     *
     * @param accountType the account type
     * @return list of phase-out ranges for the account type
     */
    List<PhaseOutRange> findByAccountType(PhaseOutAccountType accountType);

    /**
     * Finds all phase-out ranges for a given year and filing status.
     *
     * @param year         the tax year
     * @param filingStatus the filing status
     * @return list of phase-out ranges matching the criteria
     */
    List<PhaseOutRange> findByYearAndFilingStatus(Integer year, FilingStatus filingStatus);

    /**
     * Finds all phase-out ranges for a given year and account type.
     *
     * @param year        the tax year
     * @param accountType the account type
     * @return list of phase-out ranges matching the criteria
     */
    List<PhaseOutRange> findByYearAndAccountType(Integer year, PhaseOutAccountType accountType);

    /**
     * Finds a specific phase-out range by year, filing status, and account type.
     *
     * @param year         the tax year
     * @param filingStatus the filing status
     * @param accountType  the account type
     * @return the phase-out range if found
     */
    @Query("SELECT p FROM PhaseOutRange p WHERE p.year = :year "
            + "AND p.filingStatus = :filingStatus AND p.accountType = :accountType")
    Optional<PhaseOutRange> findByYearAndFilingStatusAndAccountType(
            @Param("year") Integer year,
            @Param("filingStatus") FilingStatus filingStatus,
            @Param("accountType") PhaseOutAccountType accountType);

    /**
     * Returns all distinct years that have phase-out range data.
     *
     * @return list of years with phase-out data
     */
    @Query("SELECT DISTINCT p.year FROM PhaseOutRange p ORDER BY p.year DESC")
    List<Integer> findDistinctYears();

    /**
     * Checks if phase-out ranges exist for a given year.
     *
     * @param year the tax year
     * @return true if ranges exist for the year
     */
    boolean existsByYear(Integer year);
}
