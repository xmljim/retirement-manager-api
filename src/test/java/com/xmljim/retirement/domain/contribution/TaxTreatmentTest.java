package com.xmljim.retirement.domain.contribution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link TaxTreatment}.
 *
 * @since 1.0
 */
@DisplayName("TaxTreatment")
class TaxTreatmentTest {

    @ParameterizedTest
    @EnumSource(TaxTreatment.class)
    @DisplayName("all values should have display name")
    void allValuesShouldHaveDisplayName(final TaxTreatment treatment) {
        assertNotNull(treatment.getDisplayName());
        assertFalse(treatment.getDisplayName().isBlank());
    }

    @Test
    @DisplayName("should have correct count of values")
    void shouldHaveCorrectCountOfValues() {
        assertEquals(3, TaxTreatment.values().length);
    }

    @Test
    @DisplayName("should have correct display names")
    void shouldHaveCorrectDisplayNames() {
        assertEquals("Tax-Deferred", TaxTreatment.TAX_DEFERRED.getDisplayName());
        assertEquals("Tax-Free", TaxTreatment.TAX_FREE.getDisplayName());
        assertEquals("Taxable", TaxTreatment.TAXABLE.getDisplayName());
    }
}
