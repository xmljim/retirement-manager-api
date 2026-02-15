package com.xmljim.retirement.service.person;

import com.xmljim.retirement.api.dto.person.CreatePersonRequest;
import com.xmljim.retirement.api.dto.person.UpdatePersonRequest;
import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import com.xmljim.retirement.repository.person.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PersonServiceImpl}.
 *
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PersonServiceImpl")
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person samplePerson;
    private UUID sampleId;

    @BeforeEach
    void setUp() throws Exception {
        sampleId = UUID.randomUUID();
        samplePerson = new Person(
                "John",
                "Doe",
                LocalDate.of(1980, 5, 15),
                FilingStatus.SINGLE,
                "CA"
        );
        setPrivateField(samplePerson, "id", sampleId);
    }

    @Nested
    @DisplayName("createPerson")
    class CreatePersonTests {

        @Test
        @DisplayName("should create person with valid request")
        void shouldCreatePersonWithValidRequest() {
            var request = new CreatePersonRequest(
                    "John",
                    "Doe",
                    LocalDate.of(1980, 5, 15),
                    FilingStatus.SINGLE,
                    "CA"
            );

            when(personRepository.save(any(Person.class))).thenReturn(samplePerson);

            var result = personService.createPerson(request);

            assertNotNull(result);
            assertEquals("John", result.firstName());
            assertEquals("Doe", result.lastName());
            verify(personRepository).save(any(Person.class));
        }
    }

    @Nested
    @DisplayName("getPersonById")
    class GetPersonByIdTests {

        @Test
        @DisplayName("should return person when found")
        void shouldReturnPersonWhenFound() {
            when(personRepository.findById(sampleId)).thenReturn(Optional.of(samplePerson));

            var result = personService.getPersonById(sampleId);

            assertTrue(result.isPresent());
            assertEquals(sampleId, result.get().id());
        }

        @Test
        @DisplayName("should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            var unknownId = UUID.randomUUID();
            when(personRepository.findById(unknownId)).thenReturn(Optional.empty());

            var result = personService.getPersonById(unknownId);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("getAllPersons")
    class GetAllPersonsTests {

        @Test
        @DisplayName("should return paginated persons")
        void shouldReturnPaginatedPersons() {
            var pageable = PageRequest.of(0, 10);
            var page = new PageImpl<>(List.of(samplePerson), pageable, 1);
            when(personRepository.findAll(any(Pageable.class))).thenReturn(page);

            var result = personService.getAllPersons(pageable);

            assertEquals(1, result.getTotalElements());
            assertEquals(1, result.getContent().size());
        }
    }

    @Nested
    @DisplayName("updatePerson")
    class UpdatePersonTests {

        @Test
        @DisplayName("should update person when found")
        void shouldUpdatePersonWhenFound() {
            var request = new UpdatePersonRequest(
                    "Johnny",
                    "Doe",
                    LocalDate.of(1980, 5, 15),
                    FilingStatus.MARRIED_FILING_JOINTLY,
                    "NY"
            );

            when(personRepository.findById(sampleId)).thenReturn(Optional.of(samplePerson));
            when(personRepository.save(any(Person.class))).thenReturn(samplePerson);

            var result = personService.updatePerson(sampleId, request);

            assertTrue(result.isPresent());
            verify(personRepository).save(samplePerson);
        }

        @Test
        @DisplayName("should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            var unknownId = UUID.randomUUID();
            var request = new UpdatePersonRequest(
                    "Johnny",
                    "Doe",
                    LocalDate.of(1980, 5, 15),
                    FilingStatus.SINGLE,
                    "CA"
            );

            when(personRepository.findById(unknownId)).thenReturn(Optional.empty());

            var result = personService.updatePerson(unknownId, request);

            assertFalse(result.isPresent());
            verify(personRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deletePerson")
    class DeletePersonTests {

        @Test
        @DisplayName("should delete person when found")
        void shouldDeletePersonWhenFound() {
            when(personRepository.existsById(sampleId)).thenReturn(true);

            var result = personService.deletePerson(sampleId);

            assertTrue(result);
            verify(personRepository).deleteById(sampleId);
        }

        @Test
        @DisplayName("should return false when not found")
        void shouldReturnFalseWhenNotFound() {
            var unknownId = UUID.randomUUID();
            when(personRepository.existsById(unknownId)).thenReturn(false);

            var result = personService.deletePerson(unknownId);

            assertFalse(result);
            verify(personRepository, never()).deleteById(any());
        }
    }

    private void setPrivateField(final Object target, final String fieldName, final Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
