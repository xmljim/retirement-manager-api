package com.xmljim.retirement.domain.contribution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ContributionLimit}.
 *
 * @since 1.0
 */
@DisplayName("ContributionLimit")
class ContributionLimitTest {

    private ContributionLimit limit;

    @BeforeEach
    void setUp() {
        limit = new ContributionLimit(
                2025,
                AccountType.TRADITIONAL_401K,
                LimitType.BASE,
                new BigDecimal("23500.00")
        );
    }

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should create with all required fields")
        void shouldCreateWithAllRequiredFields() {
            assertEquals(2025, limit.getYear());
            assertEquals(AccountType.TRADITIONAL_401K, limit.getAccountType());
            assertEquals(LimitType.BASE, limit.getLimitType());
            assertEquals(new BigDecimal("23500.00"), limit.getAmount());
        }

        @Test
        @DisplayName("should have null id before persistence")
        void shouldHaveNullIdBeforePersistence() {
            assertNull(limit.getId());
        }
    }

    @Nested
    @DisplayName("setters")
    class SetterTests {

        @Test
        @DisplayName("should update year")
        void shouldUpdateYear() {
            limit.setYear(2026);
            assertEquals(2026, limit.getYear());
        }

        @Test
        @DisplayName("should update account type")
        void shouldUpdateAccountType() {
            limit.setAccountType(AccountType.ROTH_401K);
            assertEquals(AccountType.ROTH_401K, limit.getAccountType());
        }

        @Test
        @DisplayName("should update limit type")
        void shouldUpdateLimitType() {
            limit.setLimitType(LimitType.CATCHUP_50);
            assertEquals(LimitType.CATCHUP_50, limit.getLimitType());
        }

        @Test
        @DisplayName("should update amount")
        void shouldUpdateAmount() {
            limit.setAmount(new BigDecimal("7500.00"));
            assertEquals(new BigDecimal("7500.00"), limit.getAmount());
        }
    }

    @Nested
    @DisplayName("equals and hashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(limit, limit);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertNotEquals(null, limit);
        }

        @Test
        @DisplayName("should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            assertNotEquals("string", limit);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringTests {

        @Test
        @DisplayName("should contain relevant fields")
        void shouldContainRelevantFields() {
            var str = limit.toString();
            assertTrue(str.contains("ContributionLimit"));
            assertTrue(str.contains("2025"));
            assertTrue(str.contains("TRADITIONAL_401K"));
            assertTrue(str.contains("BASE"));
            assertTrue(str.contains("23500"));
        }
    }

    @Nested
    @DisplayName("onCreate")
    class OnCreateTests {

        @Test
        @DisplayName("should set createdAt timestamp")
        void shouldSetCreatedAtTimestamp() {
            limit.onCreate();
            assertNotNull(limit.getCreatedAt());
        }
    }

    @Nested
    @DisplayName("constants")
    class ConstantsTests {

        @Test
        @DisplayName("should have correct max account type length")
        void shouldHaveCorrectMaxAccountTypeLength() {
            assertEquals(50, ContributionLimit.MAX_ACCOUNT_TYPE_LENGTH);
        }

        @Test
        @DisplayName("should have correct max limit type length")
        void shouldHaveCorrectMaxLimitTypeLength() {
            assertEquals(50, ContributionLimit.MAX_LIMIT_TYPE_LENGTH);
        }
    }
}
