package com.xmljim.retirement.repository.person;

import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for {@link PersonRepository}.
 *
 * <p>Uses Testcontainers to run against a real PostgreSQL database.</p>
 *
 * @since 1.0
 */
@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("test")
@DisplayName("PersonRepository Integration Tests")
class PersonRepositoryIntegrationTest {

    /** PostgreSQL test container. */
    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("retirement_test")
                    .withUsername("test")
                    .withPassword("test");

    /** The person repository under test. */
    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
    }

    @Nested
    @DisplayName("CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("should save and retrieve a person")
        void shouldSaveAndRetrievePerson() {
            var person = new Person(
                    "John",
                    "Doe",
                    LocalDate.of(1980, 5, 15),
                    FilingStatus.SINGLE,
                    "CA"
            );

            var saved = personRepository.save(person);

            assertNotNull(saved.getId());
            assertNotNull(saved.getCreatedAt());
            assertNotNull(saved.getUpdatedAt());

            var found = personRepository.findById(saved.getId());
            assertTrue(found.isPresent());
            assertEquals("John", found.get().getFirstName());
            assertEquals("Doe", found.get().getLastName());
            assertEquals(LocalDate.of(1980, 5, 15), found.get().getDateOfBirth());
            assertEquals(FilingStatus.SINGLE, found.get().getFilingStatus());
            assertEquals("CA", found.get().getStateOfResidence());
        }

        @Test
        @DisplayName("should save person without state of residence")
        void shouldSavePersonWithoutStateOfResidence() {
            var person = new Person(
                    "Jane",
                    "Smith",
                    LocalDate.of(1990, 3, 20),
                    FilingStatus.MARRIED_FILING_JOINTLY,
                    null
            );

            var saved = personRepository.save(person);

            assertNotNull(saved.getId());
            var found = personRepository.findById(saved.getId());
            assertTrue(found.isPresent());
            assertEquals("Jane", found.get().getFirstName());
            assertEquals(FilingStatus.MARRIED_FILING_JOINTLY, found.get().getFilingStatus());
        }

        @Test
        @DisplayName("should delete a person")
        void shouldDeletePerson() {
            var person = new Person(
                    "John",
                    "Doe",
                    LocalDate.of(1980, 5, 15),
                    FilingStatus.SINGLE,
                    "CA"
            );
            var saved = personRepository.save(person);
            var id = saved.getId();

            personRepository.deleteById(id);
            personRepository.flush();

            var found = personRepository.findById(id);
            assertFalse(found.isPresent());
        }
    }

    @Nested
    @DisplayName("Custom Queries")
    class CustomQueries {

        @Test
        @DisplayName("should find persons by last name")
        void shouldFindPersonsByLastName() {
            var person1 = new Person("John", "Doe", LocalDate.of(1980, 5, 15),
                    FilingStatus.SINGLE, "CA");
            var person2 = new Person("Jane", "Doe", LocalDate.of(1985, 8, 20),
                    FilingStatus.MARRIED_FILING_JOINTLY, "CA");
            var person3 = new Person("Bob", "Smith", LocalDate.of(1975, 3, 10),
                    FilingStatus.SINGLE, "NY");

            personRepository.save(person1);
            personRepository.save(person2);
            personRepository.save(person3);

            var does = personRepository.findByLastName("Doe");

            assertEquals(2, does.size());
            assertTrue(does.stream().allMatch(p -> "Doe".equals(p.getLastName())));
        }

        @Test
        @DisplayName("should find persons by filing status")
        void shouldFindPersonsByFilingStatus() {
            var person1 = new Person("John", "Doe", LocalDate.of(1980, 5, 15),
                    FilingStatus.SINGLE, "CA");
            var person2 = new Person("Jane", "Doe", LocalDate.of(1985, 8, 20),
                    FilingStatus.MARRIED_FILING_JOINTLY, "CA");
            var person3 = new Person("Bob", "Smith", LocalDate.of(1975, 3, 10),
                    FilingStatus.SINGLE, "NY");

            personRepository.save(person1);
            personRepository.save(person2);
            personRepository.save(person3);

            var singles = personRepository.findByFilingStatus(FilingStatus.SINGLE);

            assertEquals(2, singles.size());
            assertTrue(singles.stream()
                    .allMatch(p -> p.getFilingStatus() == FilingStatus.SINGLE));
        }

        @Test
        @DisplayName("should check existence by name and date of birth")
        void shouldCheckExistenceByNameAndDateOfBirth() {
            var person = new Person("John", "Doe", LocalDate.of(1980, 5, 15),
                    FilingStatus.SINGLE, "CA");
            personRepository.save(person);

            var exists = personRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                    "John", "Doe", LocalDate.of(1980, 5, 15));
            assertTrue(exists);

            var notExists = personRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                    "Unknown", "Person", LocalDate.of(2000, 1, 1));
            assertFalse(notExists);
        }
    }
}
