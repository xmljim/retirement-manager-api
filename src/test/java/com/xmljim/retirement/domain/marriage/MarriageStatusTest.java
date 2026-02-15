package com.xmljim.retirement.domain.marriage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link MarriageStatus} enum.
 *
 * @since 1.0
 */
@DisplayName("MarriageStatus")
class MarriageStatusTest {

    @Test
    @DisplayName("should have correct display name for MARRIED")
    void shouldHaveCorrectDisplayNameForMarried() {
        assertEquals("Married", MarriageStatus.MARRIED.getDisplayName());
    }

    @Test
    @DisplayName("should have correct display name for DIVORCED")
    void shouldHaveCorrectDisplayNameForDivorced() {
        assertEquals("Divorced", MarriageStatus.DIVORCED.getDisplayName());
    }

    @Test
    @DisplayName("should have correct display name for WIDOWED")
    void shouldHaveCorrectDisplayNameForWidowed() {
        assertEquals("Widowed", MarriageStatus.WIDOWED.getDisplayName());
    }

    @Test
    @DisplayName("should have three values")
    void shouldHaveThreeValues() {
        assertEquals(3, MarriageStatus.values().length);
    }

    @Test
    @DisplayName("should be retrievable by name")
    void shouldBeRetrievableByName() {
        assertNotNull(MarriageStatus.valueOf("MARRIED"));
        assertNotNull(MarriageStatus.valueOf("DIVORCED"));
        assertNotNull(MarriageStatus.valueOf("WIDOWED"));
    }
}
