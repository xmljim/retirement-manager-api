package com.xmljim.retirement.domain.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link FilingStatus} enum.
 *
 * @since 1.0
 */
@DisplayName("FilingStatus")
class FilingStatusTest {

    @Test
    @DisplayName("should have correct display name for SINGLE")
    void shouldHaveCorrectDisplayNameForSingle() {
        assertEquals("Single", FilingStatus.SINGLE.getDisplayName());
    }

    @Test
    @DisplayName("should have correct display name for MARRIED_FILING_JOINTLY")
    void shouldHaveCorrectDisplayNameForMarriedFilingJointly() {
        assertEquals("Married Filing Jointly",
                FilingStatus.MARRIED_FILING_JOINTLY.getDisplayName());
    }

    @Test
    @DisplayName("should have correct display name for MARRIED_FILING_SEPARATELY")
    void shouldHaveCorrectDisplayNameForMarriedFilingSeparately() {
        assertEquals("Married Filing Separately",
                FilingStatus.MARRIED_FILING_SEPARATELY.getDisplayName());
    }

    @Test
    @DisplayName("should have correct display name for HEAD_OF_HOUSEHOLD")
    void shouldHaveCorrectDisplayNameForHeadOfHousehold() {
        assertEquals("Head of Household",
                FilingStatus.HEAD_OF_HOUSEHOLD.getDisplayName());
    }

    @Test
    @DisplayName("should have four values")
    void shouldHaveFourValues() {
        assertEquals(4, FilingStatus.values().length);
    }

    @Test
    @DisplayName("should be retrievable by name")
    void shouldBeRetrievableByName() {
        assertNotNull(FilingStatus.valueOf("SINGLE"));
        assertNotNull(FilingStatus.valueOf("MARRIED_FILING_JOINTLY"));
        assertNotNull(FilingStatus.valueOf("MARRIED_FILING_SEPARATELY"));
        assertNotNull(FilingStatus.valueOf("HEAD_OF_HOUSEHOLD"));
    }
}
