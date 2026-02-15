package com.xmljim.retirement.domain.contribution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link AccountType}.
 *
 * @since 1.0
 */
@DisplayName("AccountType")
class AccountTypeTest {

    @Nested
    @DisplayName("enum values")
    class EnumValueTests {

        @ParameterizedTest
        @EnumSource(AccountType.class)
        @DisplayName("all values should have display name")
        void allValuesShouldHaveDisplayName(final AccountType type) {
            assertNotNull(type.getDisplayName());
            assertFalse(type.getDisplayName().isBlank());
        }

        @ParameterizedTest
        @EnumSource(AccountType.class)
        @DisplayName("all values should have tax treatment")
        void allValuesShouldHaveTaxTreatment(final AccountType type) {
            assertNotNull(type.getTaxTreatment());
        }

        @Test
        @DisplayName("should have correct count of values")
        void shouldHaveCorrectCountOfValues() {
            assertEquals(10, AccountType.values().length);
        }
    }

    @Nested
    @DisplayName("employer sponsored")
    class EmployerSponsoredTests {

        @Test
        @DisplayName("401k should be employer sponsored")
        void traditional401kShouldBeEmployerSponsored() {
            assertTrue(AccountType.TRADITIONAL_401K.isEmployerSponsored());
        }

        @Test
        @DisplayName("Traditional IRA should not be employer sponsored")
        void traditionalIraShouldNotBeEmployerSponsored() {
            assertFalse(AccountType.TRADITIONAL_IRA.isEmployerSponsored());
        }
    }

    @Nested
    @DisplayName("tax treatment")
    class TaxTreatmentTests {

        @Test
        @DisplayName("traditional accounts should be tax deferred")
        void traditionalAccountsShouldBeTaxDeferred() {
            assertEquals(TaxTreatment.TAX_DEFERRED, AccountType.TRADITIONAL_401K.getTaxTreatment());
            assertEquals(TaxTreatment.TAX_DEFERRED, AccountType.TRADITIONAL_IRA.getTaxTreatment());
        }

        @Test
        @DisplayName("Roth and HSA accounts should be tax free")
        void rothAndHsaShouldBeTaxFree() {
            assertEquals(TaxTreatment.TAX_FREE, AccountType.ROTH_401K.getTaxTreatment());
            assertEquals(TaxTreatment.TAX_FREE, AccountType.ROTH_IRA.getTaxTreatment());
        }
    }

    @Nested
    @DisplayName("database conversion")
    class DatabaseConversionTests {

        @Test
        @DisplayName("should convert 403B from database value")
        void shouldConvert403bFromDatabaseValue() {
            assertEquals(AccountType.ACCOUNT_403B, AccountType.fromDatabaseValue("403B"));
        }

        @Test
        @DisplayName("should convert 457B from database value")
        void shouldConvert457bFromDatabaseValue() {
            assertEquals(AccountType.ACCOUNT_457B, AccountType.fromDatabaseValue("457B"));
        }

        @Test
        @DisplayName("should convert 403B to database value")
        void shouldConvert403bToDatabaseValue() {
            assertEquals("403B", AccountType.ACCOUNT_403B.toDatabaseValue());
        }

        @Test
        @DisplayName("should throw for invalid database value")
        void shouldThrowForInvalidDatabaseValue() {
            assertThrows(IllegalArgumentException.class, () -> AccountType.fromDatabaseValue("INVALID"));
        }
    }
}
