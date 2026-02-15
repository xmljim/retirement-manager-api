package com.xmljim.retirement.repository.contribution;

import com.xmljim.retirement.domain.contribution.AccountType;
import com.xmljim.retirement.domain.contribution.ContributionLimit;
import com.xmljim.retirement.domain.contribution.LimitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link ContributionLimit} entities.
 *
 * <p>Provides CRUD operations and custom queries for contribution limit data.</p>
 *
 * @since 1.0
 */
@Repository
public interface ContributionLimitRepository extends JpaRepository<ContributionLimit, UUID> {

    /**
     * Finds all contribution limits for a given year.
     *
     * @param year the tax year
     * @return list of contribution limits for the year
     */
    List<ContributionLimit> findByYear(Integer year);

    /**
     * Finds all contribution limits for a given account type.
     *
     * @param accountType the account type
     * @return list of contribution limits for the account type
     */
    @Query("SELECT c FROM ContributionLimit c WHERE c.accountType = :accountType")
    List<ContributionLimit> findByAccountType(@Param("accountType") AccountType accountType);

    /**
     * Finds all contribution limits for a given year and account type.
     *
     * @param year        the tax year
     * @param accountType the account type
     * @return list of contribution limits matching the criteria
     */
    @Query("SELECT c FROM ContributionLimit c WHERE c.year = :year AND c.accountType = :accountType")
    List<ContributionLimit> findByYearAndAccountType(
            @Param("year") Integer year,
            @Param("accountType") AccountType accountType);

    /**
     * Finds a specific contribution limit by year, account type, and limit type.
     *
     * @param year        the tax year
     * @param accountType the account type
     * @param limitType   the limit type
     * @return the contribution limit if found
     */
    @Query("SELECT c FROM ContributionLimit c WHERE c.year = :year "
            + "AND c.accountType = :accountType AND c.limitType = :limitType")
    Optional<ContributionLimit> findByYearAndAccountTypeAndLimitType(
            @Param("year") Integer year,
            @Param("accountType") AccountType accountType,
            @Param("limitType") LimitType limitType);

    /**
     * Returns all distinct years that have contribution limit data.
     *
     * @return list of years with limit data
     */
    @Query("SELECT DISTINCT c.year FROM ContributionLimit c ORDER BY c.year DESC")
    List<Integer> findDistinctYears();

    /**
     * Checks if contribution limits exist for a given year.
     *
     * @param year the tax year
     * @return true if limits exist for the year
     */
    boolean existsByYear(Integer year);
}
