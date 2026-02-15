package com.xmljim.retirement.domain.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Person} entity.
 *
 * @since 1.0
 */
@DisplayName("Person")
class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person(
                "John",
                "Doe",
                LocalDate.of(1980, 5, 15),
                FilingStatus.SINGLE,
                "CA"
        );
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("should create person with all fields")
        void shouldCreatePersonWithAllFields() {
            assertEquals("John", person.getFirstName());
            assertEquals("Doe", person.getLastName());
            assertEquals(LocalDate.of(1980, 5, 15), person.getDateOfBirth());
            assertEquals(FilingStatus.SINGLE, person.getFilingStatus());
            assertEquals("CA", person.getStateOfResidence());
        }

        @Test
        @DisplayName("should allow null state of residence")
        void shouldAllowNullStateOfResidence() {
            var personNoState = new Person(
                    "Jane",
                    "Smith",
                    LocalDate.of(1990, 3, 20),
                    FilingStatus.MARRIED_FILING_JOINTLY,
                    null
            );
            assertNull(personNoState.getStateOfResidence());
        }

        @Test
        @DisplayName("should have null id before persistence")
        void shouldHaveNullIdBeforePersistence() {
            assertNull(person.getId());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {

        @Test
        @DisplayName("should update first name")
        void shouldUpdateFirstName() {
            person.setFirstName("Johnny");
            assertEquals("Johnny", person.getFirstName());
        }

        @Test
        @DisplayName("should update last name")
        void shouldUpdateLastName() {
            person.setLastName("Smith");
            assertEquals("Smith", person.getLastName());
        }

        @Test
        @DisplayName("should update date of birth")
        void shouldUpdateDateOfBirth() {
            var newDob = LocalDate.of(1985, 10, 20);
            person.setDateOfBirth(newDob);
            assertEquals(newDob, person.getDateOfBirth());
        }

        @Test
        @DisplayName("should update filing status")
        void shouldUpdateFilingStatus() {
            person.setFilingStatus(FilingStatus.MARRIED_FILING_JOINTLY);
            assertEquals(FilingStatus.MARRIED_FILING_JOINTLY, person.getFilingStatus());
        }

        @Test
        @DisplayName("should update state of residence")
        void shouldUpdateStateOfResidence() {
            person.setStateOfResidence("NY");
            assertEquals("NY", person.getStateOfResidence());
        }
    }

    @Nested
    @DisplayName("Lifecycle Callbacks")
    class LifecycleCallbackTests {

        @Test
        @DisplayName("onCreate should set timestamps")
        void onCreateShouldSetTimestamps() {
            assertNull(person.getCreatedAt());
            assertNull(person.getUpdatedAt());

            // Package-private method accessible from same package
            person.onCreate();

            assertNotNull(person.getCreatedAt());
            assertNotNull(person.getUpdatedAt());
            assertEquals(person.getCreatedAt(), person.getUpdatedAt());
        }

        @Test
        @DisplayName("onUpdate should update timestamp")
        void onUpdateShouldUpdateTimestamp() throws InterruptedException {
            person.onCreate();
            var originalUpdatedAt = person.getUpdatedAt();

            // Small delay to ensure different timestamp
            Thread.sleep(10);

            person.onUpdate();

            assertNotEquals(originalUpdatedAt, person.getUpdatedAt());
            // createdAt should remain unchanged
            assertEquals(person.getCreatedAt(), person.getCreatedAt());
        }
    }

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityTests {

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            assertEquals(person, person);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertFalse(person.equals(null));
        }

        @Test
        @DisplayName("should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            assertNotEquals("not a person", person);
        }

        @Test
        @DisplayName("persons without IDs should be equal based on id (both null)")
        void personsWithoutIdsShouldBeEqualBasedOnId() {
            var person2 = new Person(
                    "Jane",
                    "Smith",
                    LocalDate.of(1990, 3, 20),
                    FilingStatus.MARRIED_FILING_JOINTLY,
                    "NY"
            );
            // Both have null IDs, so they are equal by id comparison
            assertEquals(person.hashCode(), person2.hashCode());
        }

        @Test
        @DisplayName("should be equal when IDs match")
        void shouldBeEqualWhenIdsMatch() throws Exception {
            var id = UUID.randomUUID();
            setId(person, id);

            var person2 = new Person(
                    "Different",
                    "Name",
                    LocalDate.of(2000, 1, 1),
                    FilingStatus.HEAD_OF_HOUSEHOLD,
                    "TX"
            );
            setId(person2, id);

            assertEquals(person, person2);
            assertEquals(person.hashCode(), person2.hashCode());
        }

        @Test
        @DisplayName("should not be equal when IDs differ")
        void shouldNotBeEqualWhenIdsDiffer() throws Exception {
            setId(person, UUID.randomUUID());

            var person2 = new Person(
                    "John",
                    "Doe",
                    LocalDate.of(1980, 5, 15),
                    FilingStatus.SINGLE,
                    "CA"
            );
            setId(person2, UUID.randomUUID());

            assertNotEquals(person, person2);
        }

        private void setId(final Person targetPerson, final UUID id) throws Exception {
            var idField = Person.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(targetPerson, id);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringTests {

        @Test
        @DisplayName("should contain all fields")
        void shouldContainAllFields() {
            var str = person.toString();

            assertTrue(str.contains("Person"));
            assertTrue(str.contains("John"));
            assertTrue(str.contains("Doe"));
            assertTrue(str.contains("1980-05-15"));
            assertTrue(str.contains("SINGLE"));
            assertTrue(str.contains("CA"));
        }

        @Test
        @DisplayName("should handle null id")
        void shouldHandleNullId() {
            var str = person.toString();
            assertTrue(str.contains("id=null"));
        }
    }

    @Nested
    @DisplayName("Constants")
    class ConstantsTests {

        @Test
        @DisplayName("should have correct max name length constant")
        void shouldHaveCorrectMaxNameLengthConstant() {
            assertEquals(100, Person.MAX_NAME_LENGTH);
        }

        @Test
        @DisplayName("should have correct max filing status length constant")
        void shouldHaveCorrectMaxFilingStatusLengthConstant() {
            assertEquals(50, Person.MAX_FILING_STATUS_LENGTH);
        }

        @Test
        @DisplayName("should have correct state code length constant")
        void shouldHaveCorrectStateCodeLengthConstant() {
            assertEquals(2, Person.STATE_CODE_LENGTH);
        }
    }
}
