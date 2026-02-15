package com.xmljim.retirement.service.contribution;

import com.xmljim.retirement.api.dto.contribution.AccountTypeLimitsDto;
import com.xmljim.retirement.api.dto.contribution.ContributionLimitDto;
import com.xmljim.retirement.api.dto.contribution.PhaseOutRangeDto;
import com.xmljim.retirement.api.dto.contribution.YearlyLimitsDto;
import com.xmljim.retirement.domain.contribution.AccountType;
import com.xmljim.retirement.domain.contribution.LimitType;
import com.xmljim.retirement.domain.contribution.PhaseOutAccountType;
import com.xmljim.retirement.domain.person.FilingStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for contribution limits and phase-out range operations.
 *
 * <p>Provides methods to retrieve IRS contribution limits and income-based
 * phase-out ranges for various retirement account types.</p>
 *
 * @since 1.0
 */
public interface ContributionLimitsService {

    /**
     * Retrieves all contribution limits and phase-out ranges for a given year.
     *
     * @param year the tax year
     * @return the yearly limits data, or empty if no data exists for the year
     */
    Optional<YearlyLimitsDto> getLimitsByYear(Integer year);

    /**
     * Retrieves contribution limits for a specific account type and year.
     *
     * @param year        the tax year
     * @param accountType the account type
     * @return the account type limits, or empty if not found
     */
    Optional<AccountTypeLimitsDto> getLimitsByYearAndAccountType(Integer year, AccountType accountType);

    /**
     * Retrieves a specific contribution limit.
     *
     * @param year        the tax year
     * @param accountType the account type
     * @param limitType   the limit type
     * @return the contribution limit, or empty if not found
     */
    Optional<ContributionLimitDto> getLimit(Integer year, AccountType accountType, LimitType limitType);

    /**
     * Retrieves all contribution limits for a year.
     *
     * @param year the tax year
     * @return list of contribution limits
     */
    List<ContributionLimitDto> getContributionLimits(Integer year);

    /**
     * Retrieves all phase-out ranges for a year.
     *
     * @param year the tax year
     * @return list of phase-out ranges
     */
    List<PhaseOutRangeDto> getPhaseOutRanges(Integer year);

    /**
     * Retrieves phase-out ranges for a specific year and filing status.
     *
     * @param year         the tax year
     * @param filingStatus the filing status
     * @return list of matching phase-out ranges
     */
    List<PhaseOutRangeDto> getPhaseOutRangesByFilingStatus(Integer year, FilingStatus filingStatus);

    /**
     * Retrieves a specific phase-out range.
     *
     * @param year         the tax year
     * @param filingStatus the filing status
     * @param accountType  the phase-out account type
     * @return the phase-out range, or empty if not found
     */
    Optional<PhaseOutRangeDto> getPhaseOutRange(Integer year, FilingStatus filingStatus,
                                                 PhaseOutAccountType accountType);

    /**
     * Calculates the reduced contribution limit based on MAGI.
     *
     * <p>For Roth IRA contributions, the limit phases out based on income.
     * This method calculates the allowed contribution after applying phase-out.</p>
     *
     * @param year         the tax year
     * @param filingStatus the filing status
     * @param magi         the modified adjusted gross income
     * @return the reduced contribution limit, or empty if no phase-out applies
     */
    Optional<BigDecimal> calculateReducedRothIraLimit(Integer year, FilingStatus filingStatus,
                                                       BigDecimal magi);

    /**
     * Returns all years that have contribution limit data.
     *
     * @return list of years with data, sorted descending
     */
    List<Integer> getAvailableYears();

    /**
     * Checks if contribution limit data exists for a given year.
     *
     * @param year the tax year
     * @return true if data exists
     */
    boolean hasDataForYear(Integer year);
}
