package com.xmljim.retirement.service.contribution;

import com.xmljim.retirement.api.dto.contribution.AccountTypeLimitsDto;
import com.xmljim.retirement.api.dto.contribution.ContributionLimitDto;
import com.xmljim.retirement.api.dto.contribution.PhaseOutRangeDto;
import com.xmljim.retirement.api.dto.contribution.YearlyLimitsDto;
import com.xmljim.retirement.domain.contribution.AccountType;
import com.xmljim.retirement.domain.contribution.LimitType;
import com.xmljim.retirement.domain.contribution.PhaseOutAccountType;
import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.repository.contribution.ContributionLimitRepository;
import com.xmljim.retirement.repository.contribution.PhaseOutRangeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ContributionLimitsService}.
 *
 * <p>Provides transactional business logic for contribution limits
 * and phase-out range queries.</p>
 *
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class ContributionLimitsServiceImpl implements ContributionLimitsService {

    /** The contribution limit repository. */
    private final ContributionLimitRepository contributionLimitRepository;

    /** The phase-out range repository. */
    private final PhaseOutRangeRepository phaseOutRangeRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param limitRepository    the contribution limit repository
     * @param phaseOutRepository the phase-out range repository
     */
    public ContributionLimitsServiceImpl(final ContributionLimitRepository limitRepository,
                                         final PhaseOutRangeRepository phaseOutRepository) {
        this.contributionLimitRepository = limitRepository;
        this.phaseOutRangeRepository = phaseOutRepository;
    }

    @Override
    public Optional<YearlyLimitsDto> getLimitsByYear(final Integer year) {
        if (!hasDataForYear(year)) {
            return Optional.empty();
        }

        var limits = getContributionLimits(year);
        var phaseOuts = getPhaseOutRanges(year);

        return Optional.of(YearlyLimitsDto.create(year, limits, phaseOuts));
    }

    @Override
    public Optional<AccountTypeLimitsDto> getLimitsByYearAndAccountType(final Integer year,
                                                                         final AccountType accountType) {
        var limits = contributionLimitRepository.findByYearAndAccountType(year, accountType)
                .stream()
                .map(ContributionLimitDto::fromEntity)
                .toList();

        if (limits.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(AccountTypeLimitsDto.create(year, accountType, limits));
    }

    @Override
    public Optional<ContributionLimitDto> getLimit(final Integer year, final AccountType accountType,
                                                    final LimitType limitType) {
        return contributionLimitRepository.findByYearAndAccountTypeAndLimitType(year, accountType, limitType)
                .map(ContributionLimitDto::fromEntity);
    }

    @Override
    public List<ContributionLimitDto> getContributionLimits(final Integer year) {
        return contributionLimitRepository.findByYear(year)
                .stream()
                .map(ContributionLimitDto::fromEntity)
                .toList();
    }

    @Override
    public List<PhaseOutRangeDto> getPhaseOutRanges(final Integer year) {
        return phaseOutRangeRepository.findByYear(year)
                .stream()
                .map(PhaseOutRangeDto::fromEntity)
                .toList();
    }

    @Override
    public List<PhaseOutRangeDto> getPhaseOutRangesByFilingStatus(final Integer year,
                                                                   final FilingStatus filingStatus) {
        return phaseOutRangeRepository.findByYearAndFilingStatus(year, filingStatus)
                .stream()
                .map(PhaseOutRangeDto::fromEntity)
                .toList();
    }

    @Override
    public Optional<PhaseOutRangeDto> getPhaseOutRange(final Integer year, final FilingStatus filingStatus,
                                                        final PhaseOutAccountType accountType) {
        return phaseOutRangeRepository.findByYearAndFilingStatusAndAccountType(year, filingStatus, accountType)
                .map(PhaseOutRangeDto::fromEntity);
    }

    @Override
    public Optional<BigDecimal> calculateReducedRothIraLimit(final Integer year,
                                                              final FilingStatus filingStatus,
                                                              final BigDecimal magi) {
        // Get the base Roth IRA limit
        var baseLimitOpt = contributionLimitRepository.findByYearAndAccountTypeAndLimitType(
                year, AccountType.ROTH_IRA, LimitType.BASE);

        if (baseLimitOpt.isEmpty()) {
            return Optional.empty();
        }

        // Get the phase-out range
        var phaseOutOpt = phaseOutRangeRepository.findByYearAndFilingStatusAndAccountType(
                year, filingStatus, PhaseOutAccountType.ROTH_IRA);

        if (phaseOutOpt.isEmpty()) {
            // No phase-out defined means full contribution allowed
            return Optional.of(baseLimitOpt.get().getAmount());
        }

        var baseLimit = baseLimitOpt.get().getAmount();
        var phaseOut = phaseOutOpt.get();

        return Optional.of(phaseOut.calculateReducedLimit(baseLimit, magi));
    }

    @Override
    public List<Integer> getAvailableYears() {
        return contributionLimitRepository.findDistinctYears();
    }

    @Override
    public boolean hasDataForYear(final Integer year) {
        return contributionLimitRepository.existsByYear(year);
    }
}
