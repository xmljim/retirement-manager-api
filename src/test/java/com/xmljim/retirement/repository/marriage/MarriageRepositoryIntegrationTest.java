package com.xmljim.retirement.repository.marriage;

import com.xmljim.retirement.domain.marriage.Marriage;
import com.xmljim.retirement.domain.marriage.MarriageStatus;
import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import com.xmljim.retirement.repository.person.PersonRepository;
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
 * Integration tests for {@link MarriageRepository}.
 *
 * <p>Uses Testcontainers to run against a real PostgreSQL database.</p>
 *
 * @since 1.0
 */
@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("test")
@DisplayName("MarriageRepository Integration Tests")
class MarriageRepositoryIntegrationTest {

    /** PostgreSQL test container. */
    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("retirement_test")
                    .withUsername("test")
                    .withPassword("test");

    /** The marriage repository under test. */
    @Autowired
    private MarriageRepository marriageRepository;

    /** The person repository for test data setup. */
    @Autowired
    private PersonRepository personRepository;

    private Person person1;
    private Person person2;
    private Person person3;

    @BeforeEach
    void setUp() {
        marriageRepository.deleteAll();
        personRepository.deleteAll();

        person1 = personRepository.save(new Person(
                "John",
                "Doe",
                LocalDate.of(1980, 5, 15),
                FilingStatus.MARRIED_FILING_JOINTLY,
                "CA"
        ));
        person2 = personRepository.save(new Person(
                "Jane",
                "Doe",
                LocalDate.of(1982, 3, 20),
                FilingStatus.MARRIED_FILING_JOINTLY,
                "CA"
        ));
        person3 = personRepository.save(new Person(
                "Bob",
                "Smith",
                LocalDate.of(1975, 8, 10),
                FilingStatus.SINGLE,
                "NY"
        ));
    }

    @Nested
    @DisplayName("CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("should save and retrieve a marriage")
        void shouldSaveAndRetrieveMarriage() {
            var marriage = new Marriage(
                    person1,
                    person2,
                    LocalDate.of(2010, 6, 15),
                    MarriageStatus.MARRIED,
                    "First marriage"
            );

            var saved = marriageRepository.save(marriage);

            assertNotNull(saved.getId());
            assertNotNull(saved.getCreatedAt());
            assertNotNull(saved.getUpdatedAt());

            var found = marriageRepository.findById(saved.getId());
            assertTrue(found.isPresent());
            assertEquals(person1.getId(), found.get().getPerson1().getId());
            assertEquals(person2.getId(), found.get().getPerson2().getId());
            assertEquals(LocalDate.of(2010, 6, 15), found.get().getMarriageDate());
            assertEquals(MarriageStatus.MARRIED, found.get().getStatus());
            assertEquals("First marriage", found.get().getNotes());
        }

        @Test
        @DisplayName("should delete a marriage")
        void shouldDeleteMarriage() {
            var marriage = new Marriage(
                    person1,
                    person2,
                    LocalDate.of(2010, 6, 15),
                    MarriageStatus.MARRIED,
                    null
            );
            var saved = marriageRepository.save(marriage);
            var id = saved.getId();

            marriageRepository.deleteById(id);
            marriageRepository.flush();

            var found = marriageRepository.findById(id);
            assertFalse(found.isPresent());
        }
    }

    @Nested
    @DisplayName("Custom Queries")
    class CustomQueries {

        @Test
        @DisplayName("should find marriages by person ID")
        void shouldFindMarriagesByPersonId() {
            marriageRepository.save(new Marriage(
                    person1,
                    person2,
                    LocalDate.of(2010, 6, 15),
                    MarriageStatus.DIVORCED,
                    null
            ));
            marriageRepository.save(new Marriage(
                    person1,
                    person3,
                    LocalDate.of(2022, 1, 1),
                    MarriageStatus.MARRIED,
                    null
            ));

            var person1Marriages = marriageRepository.findByPersonId(person1.getId());
            assertEquals(2, person1Marriages.size());

            var person2Marriages = marriageRepository.findByPersonId(person2.getId());
            assertEquals(1, person2Marriages.size());
        }

        @Test
        @DisplayName("should find marriages by status")
        void shouldFindMarriagesByStatus() {
            marriageRepository.save(new Marriage(
                    person1,
                    person2,
                    LocalDate.of(2010, 6, 15),
                    MarriageStatus.DIVORCED,
                    null
            ));
            marriageRepository.save(new Marriage(
                    person1,
                    person3,
                    LocalDate.of(2022, 1, 1),
                    MarriageStatus.MARRIED,
                    null
            ));

            var divorced = marriageRepository.findByStatus(MarriageStatus.DIVORCED);
            assertEquals(1, divorced.size());

            var married = marriageRepository.findByStatus(MarriageStatus.MARRIED);
            assertEquals(1, married.size());
        }

        @Test
        @DisplayName("should check for active marriage")
        void shouldCheckForActiveMarriage() {
            marriageRepository.save(new Marriage(
                    person1,
                    person2,
                    LocalDate.of(2010, 6, 15),
                    MarriageStatus.DIVORCED,
                    null
            ));
            marriageRepository.save(new Marriage(
                    person1,
                    person3,
                    LocalDate.of(2022, 1, 1),
                    MarriageStatus.MARRIED,
                    null
            ));

            assertTrue(marriageRepository.hasActiveMarriage(person1.getId()));
            assertFalse(marriageRepository.hasActiveMarriage(person2.getId()));
            assertTrue(marriageRepository.hasActiveMarriage(person3.getId()));
        }
    }
}
