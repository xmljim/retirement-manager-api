package com.xmljim.retirement.domain.contribution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link LimitType}.
 *
 * @since 1.0
 */
@DisplayName("LimitType")
class LimitTypeTest {

    @Nested
    @DisplayName("enum values")
    class EnumValueTests {

        @ParameterizedTest
        @EnumSource(LimitType.class)
        @DisplayName("all values should have display name")
        void allValuesShouldHaveDisplayName(final LimitType type) {
            assertNotNull(type.getDisplayName());
            assertFalse(type.getDisplayName().isBlank());
        }

        @Test
        @DisplayName("should have correct count of values")
        void shouldHaveCorrectCountOfValues() {
            assertEquals(6, LimitType.values().length);
        }
    }

    @Nested
    @DisplayName("age requirements")
    class AgeRequirementTests {

        @Test
        @DisplayName("BASE should not have age requirement")
        void baseShouldNotHaveAgeRequirement() {
            assertFalse(LimitType.BASE.hasAgeRequirement());
            assertNull(LimitType.BASE.getMinimumAge());
            assertNull(LimitType.BASE.getMaximumAge());
        }

        @Test
        @DisplayName("CATCHUP_50 should have minimum age of 50")
        void catchup50ShouldHaveMinimumAge50() {
            assertTrue(LimitType.CATCHUP_50.hasAgeRequirement());
            assertEquals(50, LimitType.CATCHUP_50.getMinimumAge());
            assertNull(LimitType.CATCHUP_50.getMaximumAge());
        }

        @Test
        @DisplayName("CATCHUP_60_63 should have age range 60-63")
        void catchup6063ShouldHaveAgeRange() {
            assertTrue(LimitType.CATCHUP_60_63.hasAgeRequirement());
            assertEquals(60, LimitType.CATCHUP_60_63.getMinimumAge());
            assertEquals(63, LimitType.CATCHUP_60_63.getMaximumAge());
        }
    }

    @Nested
    @DisplayName("eligibility")
    class EligibilityTests {

        @ParameterizedTest
        @CsvSource({
            "25, true",
            "49, true",
            "50, true",
            "65, true"
        })
        @DisplayName("BASE should be eligible for all ages")
        void baseShouldBeEligibleForAllAges(final int age, final boolean expected) {
            assertEquals(expected, LimitType.BASE.isEligible(age));
        }

        @ParameterizedTest
        @CsvSource({
            "25, false",
            "49, false",
            "50, true",
            "55, true",
            "65, true"
        })
        @DisplayName("CATCHUP_50 should be eligible for age 50+")
        void catchup50ShouldBeEligibleForAge50Plus(final int age, final boolean expected) {
            assertEquals(expected, LimitType.CATCHUP_50.isEligible(age));
        }
    }
}
