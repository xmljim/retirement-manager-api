package com.xmljim.retirement.service.contribution;

import com.xmljim.retirement.domain.contribution.AccountType;
import com.xmljim.retirement.domain.contribution.ContributionLimit;
import com.xmljim.retirement.domain.contribution.LimitType;
import com.xmljim.retirement.domain.contribution.PhaseOutAccountType;
import com.xmljim.retirement.domain.contribution.PhaseOutRange;
import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.repository.contribution.ContributionLimitRepository;
import com.xmljim.retirement.repository.contribution.PhaseOutRangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ContributionLimitsServiceImpl}.
 *
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ContributionLimitsServiceImpl")
class ContributionLimitsServiceImplTest {

    @Mock
    private ContributionLimitRepository limitRepository;

    @Mock
    private PhaseOutRangeRepository phaseOutRepository;

    @InjectMocks
    private ContributionLimitsServiceImpl service;

    private ContributionLimit sampleLimit;
    private PhaseOutRange samplePhaseOut;

    @BeforeEach
    void setUp() {
        sampleLimit = new ContributionLimit(2025, AccountType.ROTH_IRA, LimitType.BASE, new BigDecimal("7000.00"));
        samplePhaseOut = new PhaseOutRange(2025, FilingStatus.SINGLE, PhaseOutAccountType.ROTH_IRA,
                new BigDecimal("150000.00"), new BigDecimal("165000.00"));
    }

    @Nested
    @DisplayName("getLimitsByYear")
    class GetLimitsByYearTests {

        @Test
        @DisplayName("should return limits when year exists")
        void shouldReturnLimitsWhenYearExists() {
            when(limitRepository.existsByYear(2025)).thenReturn(true);
            when(limitRepository.findByYear(2025)).thenReturn(List.of(sampleLimit));
            when(phaseOutRepository.findByYear(2025)).thenReturn(List.of(samplePhaseOut));

            var result = service.getLimitsByYear(2025);

            assertTrue(result.isPresent());
            assertEquals(2025, result.get().year());
            assertEquals(1, result.get().contributionLimits().size());
            assertEquals(1, result.get().phaseOutRanges().size());
        }

        @Test
        @DisplayName("should return empty when year does not exist")
        void shouldReturnEmptyWhenYearDoesNotExist() {
            when(limitRepository.existsByYear(2030)).thenReturn(false);

            var result = service.getLimitsByYear(2030);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("getLimitsByYearAndAccountType")
    class GetLimitsByYearAndAccountTypeTests {

        @Test
        @DisplayName("should return limits when found")
        void shouldReturnLimitsWhenFound() {
            when(limitRepository.findByYearAndAccountType(2025, AccountType.ROTH_IRA))
                    .thenReturn(List.of(sampleLimit));

            var result = service.getLimitsByYearAndAccountType(2025, AccountType.ROTH_IRA);

            assertTrue(result.isPresent());
            assertEquals(AccountType.ROTH_IRA, result.get().accountType());
        }

        @Test
        @DisplayName("should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(limitRepository.findByYearAndAccountType(2025, AccountType.HSA_SELF)).thenReturn(List.of());

            var result = service.getLimitsByYearAndAccountType(2025, AccountType.HSA_SELF);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("getLimit")
    class GetLimitTests {

        @Test
        @DisplayName("should return limit when found")
        void shouldReturnLimitWhenFound() {
            when(limitRepository.findByYearAndAccountTypeAndLimitType(2025, AccountType.ROTH_IRA, LimitType.BASE))
                    .thenReturn(Optional.of(sampleLimit));

            var result = service.getLimit(2025, AccountType.ROTH_IRA, LimitType.BASE);

            assertTrue(result.isPresent());
            assertEquals(new BigDecimal("7000.00"), result.get().amount());
        }
    }

    @Nested
    @DisplayName("calculateReducedRothIraLimit")
    class CalculateReducedRothIraLimitTests {

        @Test
        @DisplayName("should return full limit when below phase-out")
        void shouldReturnFullLimitWhenBelowPhaseOut() {
            when(limitRepository.findByYearAndAccountTypeAndLimitType(2025, AccountType.ROTH_IRA, LimitType.BASE))
                    .thenReturn(Optional.of(sampleLimit));
            when(phaseOutRepository.findByYearAndFilingStatusAndAccountType(2025, FilingStatus.SINGLE,
                    PhaseOutAccountType.ROTH_IRA)).thenReturn(Optional.of(samplePhaseOut));

            var result = service.calculateReducedRothIraLimit(2025, FilingStatus.SINGLE, new BigDecimal("140000"));

            assertTrue(result.isPresent());
            assertEquals(new BigDecimal("7000"), result.get());
        }

        @Test
        @DisplayName("should return zero when above phase-out")
        void shouldReturnZeroWhenAbovePhaseOut() {
            when(limitRepository.findByYearAndAccountTypeAndLimitType(2025, AccountType.ROTH_IRA, LimitType.BASE))
                    .thenReturn(Optional.of(sampleLimit));
            when(phaseOutRepository.findByYearAndFilingStatusAndAccountType(2025, FilingStatus.SINGLE,
                    PhaseOutAccountType.ROTH_IRA)).thenReturn(Optional.of(samplePhaseOut));

            var result = service.calculateReducedRothIraLimit(2025, FilingStatus.SINGLE, new BigDecimal("200000"));

            assertTrue(result.isPresent());
            assertEquals(BigDecimal.ZERO.setScale(0), result.get());
        }

        @Test
        @DisplayName("should return empty when no base limit exists")
        void shouldReturnEmptyWhenNoBaseLimitExists() {
            when(limitRepository.findByYearAndAccountTypeAndLimitType(2025, AccountType.ROTH_IRA, LimitType.BASE))
                    .thenReturn(Optional.empty());

            var result = service.calculateReducedRothIraLimit(2025, FilingStatus.SINGLE, new BigDecimal("150000"));

            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("getAvailableYears")
    class GetAvailableYearsTests {

        @Test
        @DisplayName("should return available years")
        void shouldReturnAvailableYears() {
            when(limitRepository.findDistinctYears()).thenReturn(List.of(2026, 2025));

            var result = service.getAvailableYears();

            assertEquals(2, result.size());
            assertEquals(2026, result.get(0));
        }
    }

    @Nested
    @DisplayName("hasDataForYear")
    class HasDataForYearTests {

        @Test
        @DisplayName("should return true when data exists")
        void shouldReturnTrueWhenDataExists() {
            when(limitRepository.existsByYear(2025)).thenReturn(true);
            assertTrue(service.hasDataForYear(2025));
        }

        @Test
        @DisplayName("should return false when no data exists")
        void shouldReturnFalseWhenNoDataExists() {
            when(limitRepository.existsByYear(2030)).thenReturn(false);
            assertFalse(service.hasDataForYear(2030));
        }
    }
}
