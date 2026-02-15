package com.xmljim.retirement.api.contribution;

import com.xmljim.retirement.api.dto.contribution.AccountTypeLimitsDto;
import com.xmljim.retirement.api.dto.contribution.ContributionLimitDto;
import com.xmljim.retirement.api.dto.contribution.PhaseOutRangeDto;
import com.xmljim.retirement.api.dto.contribution.YearlyLimitsDto;
import com.xmljim.retirement.domain.contribution.AccountType;
import com.xmljim.retirement.domain.contribution.LimitType;
import com.xmljim.retirement.domain.contribution.PhaseOutAccountType;
import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.service.contribution.ContributionLimitsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MockMvc integration tests for {@link LimitsController}.
 *
 * @since 1.0
 */
@WebMvcTest(LimitsController.class)
@DisplayName("LimitsController")
class LimitsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContributionLimitsService limitsService;

    private static final int TEST_YEAR = 2025;
    private static final UUID TEST_ID = UUID.randomUUID();

    @Nested
    @DisplayName("GET /api/v1/limits/{year}")
    class GetLimitsByYearTests {

        @Test
        @DisplayName("should return yearly limits when data exists")
        void shouldReturnYearlyLimitsWhenDataExists() throws Exception {
            var limitDto = new ContributionLimitDto(
                    TEST_ID, TEST_YEAR, AccountType.ROTH_IRA, LimitType.BASE, new BigDecimal("7000.00"));
            var phaseOutDto = new PhaseOutRangeDto(
                    TEST_ID, TEST_YEAR, FilingStatus.SINGLE, PhaseOutAccountType.ROTH_IRA,
                    new BigDecimal("150000.00"), new BigDecimal("165000.00"));
            var yearlyLimits = new YearlyLimitsDto(TEST_YEAR, List.of(limitDto), List.of(phaseOutDto));

            when(limitsService.getLimitsByYear(TEST_YEAR)).thenReturn(Optional.of(yearlyLimits));

            mockMvc.perform(get("/api/v1/limits/{year}", TEST_YEAR)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.year", is(TEST_YEAR)))
                    .andExpect(jsonPath("$.contributionLimits", hasSize(1)))
                    .andExpect(jsonPath("$.contributionLimits[0].accountType", is("ROTH_IRA")))
                    .andExpect(jsonPath("$.phaseOutRanges", hasSize(1)));
        }

        @Test
        @DisplayName("should return 404 when year not found")
        void shouldReturn404WhenYearNotFound() throws Exception {
            when(limitsService.getLimitsByYear(2030)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/limits/{year}", 2030)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.error", is("Not Found")));
        }

        @Test
        @DisplayName("should return 400 for invalid year format")
        void shouldReturn400ForInvalidYearFormat() throws Exception {
            mockMvc.perform(get("/api/v1/limits/{year}", "invalid")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/limits/{year}/{accountType}")
    class GetLimitsByYearAndAccountTypeTests {

        @Test
        @DisplayName("should return account type limits when data exists")
        void shouldReturnAccountTypeLimitsWhenDataExists() throws Exception {
            var limitDto = new ContributionLimitDto(
                    TEST_ID, TEST_YEAR, AccountType.TRADITIONAL_401K, LimitType.BASE, new BigDecimal("23500.00"));
            var accountLimits = new AccountTypeLimitsDto(TEST_YEAR, AccountType.TRADITIONAL_401K, List.of(limitDto));

            when(limitsService.getLimitsByYearAndAccountType(TEST_YEAR, AccountType.TRADITIONAL_401K))
                    .thenReturn(Optional.of(accountLimits));

            mockMvc.perform(get("/api/v1/limits/{year}/{accountType}", TEST_YEAR, "TRADITIONAL_401K")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.year", is(TEST_YEAR)))
                    .andExpect(jsonPath("$.accountType", is("TRADITIONAL_401K")))
                    .andExpect(jsonPath("$.limits", hasSize(1)))
                    .andExpect(jsonPath("$.limits[0].amount", is(23500.00)));
        }

        @Test
        @DisplayName("should return 404 when account type not found")
        void shouldReturn404WhenAccountTypeNotFound() throws Exception {
            when(limitsService.getLimitsByYearAndAccountType(TEST_YEAR, AccountType.HSA_SELF))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/limits/{year}/{accountType}", TEST_YEAR, "HSA_SELF")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)));
        }

        @Test
        @DisplayName("should return 400 for invalid account type")
        void shouldReturn400ForInvalidAccountType() throws Exception {
            mockMvc.perform(get("/api/v1/limits/{year}/{accountType}", TEST_YEAR, "INVALID_TYPE")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/limits/years")
    class GetAvailableYearsTests {

        @Test
        @DisplayName("should return available years sorted descending")
        void shouldReturnAvailableYearsSortedDescending() throws Exception {
            when(limitsService.getAvailableYears()).thenReturn(List.of(2026, 2025, 2024));

            mockMvc.perform(get("/api/v1/limits/years")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0]", is(2026)))
                    .andExpect(jsonPath("$[1]", is(2025)))
                    .andExpect(jsonPath("$[2]", is(2024)));
        }

        @Test
        @DisplayName("should return empty list when no data available")
        void shouldReturnEmptyListWhenNoDataAvailable() throws Exception {
            when(limitsService.getAvailableYears()).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/limits/years")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }
}
