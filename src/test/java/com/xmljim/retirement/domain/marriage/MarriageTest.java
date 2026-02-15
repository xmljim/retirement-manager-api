package com.xmljim.retirement.domain.marriage;

import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Marriage} entity.
 *
 * @since 1.0
 */
@DisplayName("Marriage")
class MarriageTest {

    private Marriage marriage;
    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        person1 = new Person(
                "John",
                "Doe",
                LocalDate.of(1980, 5, 15),
                FilingStatus.MARRIED_FILING_JOINTLY,
                "CA"
        );
        person2 = new Person(
                "Jane",
                "Doe",
                LocalDate.of(1982, 3, 20),
                FilingStatus.MARRIED_FILING_JOINTLY,
                "CA"
        );
        marriage = new Marriage(
                person1,
                person2,
                LocalDate.of(2010, 6, 15),
                MarriageStatus.MARRIED,
                "First marriage"
        );
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should create marriage with all fields")
        void shouldCreateMarriageWithAllFields() {
            assertEquals(person1, marriage.getPerson1());
            assertEquals(person2, marriage.getPerson2());
            assertEquals(LocalDate.of(2010, 6, 15), marriage.getMarriageDate());
            assertEquals(MarriageStatus.MARRIED, marriage.getStatus());
            assertEquals("First marriage", marriage.getNotes());
        }

        @Test
        @DisplayName("should allow null notes")
        void shouldAllowNullNotes() {
            var marriageNoNotes = new Marriage(
                    person1,
                    person2,
                    LocalDate.of(2010, 6, 15),
                    MarriageStatus.MARRIED,
                    null
            );
            assertNull(marriageNoNotes.getNotes());
        }

        @Test
        @DisplayName("should have null id before persistence")
        void shouldHaveNullIdBeforePersistence() {
            assertNull(marriage.getId());
        }

        @Test
        @DisplayName("should have null divorce date by default")
        void shouldHaveNullDivorceDateByDefault() {
            assertNull(marriage.getDivorceDate());
        }
    }

    @Nested
    @DisplayName("Marriage Duration")
    class MarriageDurationTests {

        @Test
        @DisplayName("should calculate duration for ongoing marriage")
        void shouldCalculateDurationForOngoingMarriage() {
            var yearsAgo = LocalDate.now().minusYears(15);
            marriage.setMarriageDate(yearsAgo);

            assertEquals(15, marriage.getMarriageDurationYears());
        }

        @Test
        @DisplayName("should calculate duration for divorced marriage")
        void shouldCalculateDurationForDivorcedMarriage() {
            marriage.setMarriageDate(LocalDate.of(2010, 6, 15));
            marriage.setDivorceDate(LocalDate.of(2020, 6, 15));
            marriage.setStatus(MarriageStatus.DIVORCED);

            assertEquals(10, marriage.getMarriageDurationYears());
        }
    }

    @Nested
    @DisplayName("Spousal Benefit Eligibility")
    class SpousalBenefitTests {

        @Test
        @DisplayName("should be eligible for benefits with 10+ years")
        void shouldBeEligibleWithTenOrMoreYears() {
            marriage.setMarriageDate(LocalDate.of(2010, 6, 15));
            marriage.setDivorceDate(LocalDate.of(2020, 6, 15));

            assertTrue(marriage.isEligibleForSpousalBenefits());
        }

        @Test
        @DisplayName("should not be eligible with less than 10 years")
        void shouldNotBeEligibleWithLessThanTenYears() {
            marriage.setMarriageDate(LocalDate.of(2015, 6, 15));
            marriage.setDivorceDate(LocalDate.of(2020, 6, 14));

            assertFalse(marriage.isEligibleForSpousalBenefits());
        }

        @Test
        @DisplayName("constant should be 10 years for SS benefits")
        void constantShouldBeTenYears() {
            assertEquals(10, Marriage.SS_SPOUSAL_BENEFIT_MIN_YEARS);
        }
    }

    @Nested
    @DisplayName("Lifecycle Callbacks")
    class LifecycleCallbackTests {

        @Test
        @DisplayName("onCreate should set timestamps")
        void onCreateShouldSetTimestamps() {
            assertNull(marriage.getCreatedAt());
            assertNull(marriage.getUpdatedAt());

            marriage.onCreate();

            assertNotNull(marriage.getCreatedAt());
            assertNotNull(marriage.getUpdatedAt());
            assertEquals(marriage.getCreatedAt(), marriage.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityTests {

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(marriage, marriage);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertFalse(marriage.equals(null));
        }

        @Test
        @DisplayName("should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            assertNotEquals("not a marriage", marriage);
        }
    }

    @Nested
    @DisplayName("Constants")
    class ConstantsTests {

        @Test
        @DisplayName("should have correct max status length constant")
        void shouldHaveCorrectMaxStatusLengthConstant() {
            assertEquals(20, Marriage.MAX_STATUS_LENGTH);
        }

        @Test
        @DisplayName("should have correct spousal benefit min years constant")
        void shouldHaveCorrectSpousalBenefitMinYearsConstant() {
            assertEquals(10, Marriage.SS_SPOUSAL_BENEFIT_MIN_YEARS);
        }
    }
}
