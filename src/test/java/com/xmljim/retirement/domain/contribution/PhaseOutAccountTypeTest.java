package com.xmljim.retirement.domain.contribution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link PhaseOutAccountType}.
 *
 * @since 1.0
 */
@DisplayName("PhaseOutAccountType")
class PhaseOutAccountTypeTest {

    @ParameterizedTest
    @EnumSource(PhaseOutAccountType.class)
    @DisplayName("all values should have display name")
    void allValuesShouldHaveDisplayName(final PhaseOutAccountType type) {
        assertNotNull(type.getDisplayName());
        assertFalse(type.getDisplayName().isBlank());
    }

    @ParameterizedTest
    @EnumSource(PhaseOutAccountType.class)
    @DisplayName("all values should have description")
    void allValuesShouldHaveDescription(final PhaseOutAccountType type) {
        assertNotNull(type.getDescription());
        assertFalse(type.getDescription().isBlank());
    }

    @Test
    @DisplayName("should have correct count of values")
    void shouldHaveCorrectCountOfValues() {
        assertEquals(3, PhaseOutAccountType.values().length);
    }

    @Test
    @DisplayName("should have correct display names")
    void shouldHaveCorrectDisplayNames() {
        assertEquals("Roth IRA", PhaseOutAccountType.ROTH_IRA.getDisplayName());
        assertEquals("Traditional IRA", PhaseOutAccountType.TRADITIONAL_IRA.getDisplayName());
        assertEquals("Traditional IRA (Spouse Covered)",
                PhaseOutAccountType.TRADITIONAL_IRA_SPOUSE_COVERED.getDisplayName());
    }
}
