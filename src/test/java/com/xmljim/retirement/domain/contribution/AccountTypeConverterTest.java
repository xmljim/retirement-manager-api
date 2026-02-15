package com.xmljim.retirement.domain.contribution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link AccountTypeConverter}.
 *
 * @since 1.0
 */
@DisplayName("AccountTypeConverter")
class AccountTypeConverterTest {

    private AccountTypeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AccountTypeConverter();
    }

    @Nested
    @DisplayName("convertToDatabaseColumn")
    class ConvertToDatabaseColumnTests {

        @Test
        @DisplayName("should return null for null input")
        void shouldReturnNullForNullInput() {
            assertNull(converter.convertToDatabaseColumn(null));
        }

        @Test
        @DisplayName("should convert 403B correctly")
        void shouldConvert403bCorrectly() {
            assertEquals("403B", converter.convertToDatabaseColumn(AccountType.ACCOUNT_403B));
        }

        @Test
        @DisplayName("should convert 457B correctly")
        void shouldConvert457bCorrectly() {
            assertEquals("457B", converter.convertToDatabaseColumn(AccountType.ACCOUNT_457B));
        }
    }

    @Nested
    @DisplayName("convertToEntityAttribute")
    class ConvertToEntityAttributeTests {

        @Test
        @DisplayName("should return null for null input")
        void shouldReturnNullForNullInput() {
            assertNull(converter.convertToEntityAttribute(null));
        }

        @Test
        @DisplayName("should convert 403B correctly")
        void shouldConvert403bCorrectly() {
            assertEquals(AccountType.ACCOUNT_403B, converter.convertToEntityAttribute("403B"));
        }

        @Test
        @DisplayName("should convert 457B correctly")
        void shouldConvert457bCorrectly() {
            assertEquals(AccountType.ACCOUNT_457B, converter.convertToEntityAttribute("457B"));
        }
    }

    @Nested
    @DisplayName("round-trip conversion")
    class RoundTripTests {

        @ParameterizedTest
        @EnumSource(AccountType.class)
        @DisplayName("should survive round-trip conversion")
        void shouldSurviveRoundTripConversion(final AccountType type) {
            var dbValue = converter.convertToDatabaseColumn(type);
            var restored = converter.convertToEntityAttribute(dbValue);
            assertEquals(type, restored);
        }
    }
}
