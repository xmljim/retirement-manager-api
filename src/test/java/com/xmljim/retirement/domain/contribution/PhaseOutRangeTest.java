package com.xmljim.retirement.domain.contribution;

import com.xmljim.retirement.domain.person.FilingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link PhaseOutRange}.
 *
 * @since 1.0
 */
@DisplayName("PhaseOutRange")
class PhaseOutRangeTest {

    private PhaseOutRange range;

    @BeforeEach
    void setUp() {
        range = new PhaseOutRange(
                2025,
                FilingStatus.SINGLE,
                PhaseOutAccountType.ROTH_IRA,
                new BigDecimal("150000.00"),
                new BigDecimal("165000.00")
        );
    }

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should create with all required fields")
        void shouldCreateWithAllRequiredFields() {
            assertEquals(2025, range.getYear());
            assertEquals(FilingStatus.SINGLE, range.getFilingStatus());
            assertEquals(PhaseOutAccountType.ROTH_IRA, range.getAccountType());
            assertEquals(new BigDecimal("150000.00"), range.getMagiStart());
            assertEquals(new BigDecimal("165000.00"), range.getMagiEnd());
        }

        @Test
        @DisplayName("should have null id before persistence")
        void shouldHaveNullIdBeforePersistence() {
            assertNull(range.getId());
        }
    }

    @Nested
    @DisplayName("calculatePhaseOutPercentage")
    class PhaseOutPercentageTests {

        @Test
        @DisplayName("should return zero when MAGI is below start")
        void shouldReturnZeroWhenMagiIsBelowStart() {
            var pct = range.calculatePhaseOutPercentage(new BigDecimal("140000.00"));
            assertEquals(BigDecimal.ZERO, pct);
        }

        @Test
        @DisplayName("should return zero when MAGI equals start")
        void shouldReturnZeroWhenMagiEqualsStart() {
            var pct = range.calculatePhaseOutPercentage(new BigDecimal("150000.00"));
            assertEquals(BigDecimal.ZERO, pct);
        }

        @Test
        @DisplayName("should return one when MAGI equals end")
        void shouldReturnOneWhenMagiEqualsEnd() {
            var pct = range.calculatePhaseOutPercentage(new BigDecimal("165000.00"));
            assertEquals(BigDecimal.ONE, pct);
        }

        @Test
        @DisplayName("should return one when MAGI is above end")
        void shouldReturnOneWhenMagiIsAboveEnd() {
            var pct = range.calculatePhaseOutPercentage(new BigDecimal("200000.00"));
            assertEquals(BigDecimal.ONE, pct);
        }

        @ParameterizedTest
        @CsvSource({
            "157500.00, 0.5000",
            "153750.00, 0.2500",
            "161250.00, 0.7500"
        })
        @DisplayName("should calculate correct percentage within range")
        void shouldCalculateCorrectPercentageWithinRange(final String magi, final String expected) {
            var pct = range.calculatePhaseOutPercentage(new BigDecimal(magi));
            assertEquals(new BigDecimal(expected), pct);
        }
    }

    @Nested
    @DisplayName("calculateReducedLimit")
    class ReducedLimitTests {

        private static final BigDecimal BASE_LIMIT = new BigDecimal("7000.00");

        @Test
        @DisplayName("should return full limit when below phase-out")
        void shouldReturnFullLimitWhenBelowPhaseOut() {
            var reduced = range.calculateReducedLimit(BASE_LIMIT, new BigDecimal("140000.00"));
            assertEquals(new BigDecimal("7000"), reduced);
        }

        @Test
        @DisplayName("should return zero when above phase-out")
        void shouldReturnZeroWhenAbovePhaseOut() {
            var reduced = range.calculateReducedLimit(BASE_LIMIT, new BigDecimal("200000.00"));
            assertEquals(BigDecimal.ZERO.setScale(0), reduced);
        }

        @Test
        @DisplayName("should return half limit at midpoint")
        void shouldReturnHalfLimitAtMidpoint() {
            var reduced = range.calculateReducedLimit(BASE_LIMIT, new BigDecimal("157500.00"));
            assertEquals(new BigDecimal("3500"), reduced);
        }
    }

    @Nested
    @DisplayName("equals and hashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(range, range);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertNotEquals(null, range);
        }

        @Test
        @DisplayName("should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            assertNotEquals("string", range);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringTests {

        @Test
        @DisplayName("should contain relevant fields")
        void shouldContainRelevantFields() {
            var str = range.toString();
            assertTrue(str.contains("PhaseOutRange"));
            assertTrue(str.contains("2025"));
            assertTrue(str.contains("SINGLE"));
            assertTrue(str.contains("ROTH_IRA"));
            assertTrue(str.contains("150000"));
            assertTrue(str.contains("165000"));
        }
    }

    @Nested
    @DisplayName("onCreate")
    class OnCreateTests {

        @Test
        @DisplayName("should set createdAt timestamp")
        void shouldSetCreatedAtTimestamp() {
            range.onCreate();
            assertNotNull(range.getCreatedAt());
        }
    }

    @Nested
    @DisplayName("constants")
    class ConstantsTests {

        @Test
        @DisplayName("should have correct max filing status length")
        void shouldHaveCorrectMaxFilingStatusLength() {
            assertEquals(50, PhaseOutRange.MAX_FILING_STATUS_LENGTH);
        }

        @Test
        @DisplayName("should have correct max account type length")
        void shouldHaveCorrectMaxAccountTypeLength() {
            assertEquals(50, PhaseOutRange.MAX_ACCOUNT_TYPE_LENGTH);
        }
    }
}
