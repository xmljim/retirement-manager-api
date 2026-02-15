package com.xmljim.retirement.service.marriage;

import com.xmljim.retirement.api.dto.marriage.CreateMarriageRequest;
import com.xmljim.retirement.domain.marriage.Marriage;
import com.xmljim.retirement.domain.marriage.MarriageStatus;
import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import com.xmljim.retirement.repository.marriage.MarriageRepository;
import com.xmljim.retirement.repository.person.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link MarriageServiceImpl}.
 *
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MarriageServiceImpl")
class MarriageServiceImplTest {

    @Mock
    private MarriageRepository marriageRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private MarriageServiceImpl marriageService;

    private Person person1;
    private Person person2;
    private Marriage sampleMarriage;
    private UUID person1Id;
    private UUID person2Id;
    private UUID marriageId;

    @BeforeEach
    void setUp() throws Exception {
        person1Id = UUID.randomUUID();
        person2Id = UUID.randomUUID();
        marriageId = UUID.randomUUID();

        person1 = createPerson("John", "Doe", person1Id);
        person2 = createPerson("Jane", "Doe", person2Id);

        sampleMarriage = new Marriage(
                person1, person2,
                LocalDate.of(2010, 6, 15),
                MarriageStatus.MARRIED, "First marriage"
        );
        setPrivateField(sampleMarriage, "id", marriageId);
    }

    @Nested
    @DisplayName("createMarriage")
    class CreateMarriageTests {

        @Test
        @DisplayName("should create marriage with valid request")
        void shouldCreateMarriageWithValidRequest() {
            var request = new CreateMarriageRequest(
                    person1Id, person2Id,
                    LocalDate.of(2010, 6, 15), null,
                    MarriageStatus.MARRIED, "First marriage"
            );

            when(personRepository.findById(person1Id)).thenReturn(Optional.of(person1));
            when(personRepository.findById(person2Id)).thenReturn(Optional.of(person2));
            when(marriageRepository.save(any(Marriage.class))).thenReturn(sampleMarriage);

            var result = marriageService.createMarriage(request);

            assertNotNull(result);
            assertEquals(person1Id, result.person1Id());
            assertEquals(person2Id, result.person2Id());
            verify(marriageRepository).save(any(Marriage.class));
        }

        @Test
        @DisplayName("should throw when person1 not found")
        void shouldThrowWhenPerson1NotFound() {
            var request = new CreateMarriageRequest(
                    person1Id, person2Id,
                    LocalDate.of(2010, 6, 15), null,
                    MarriageStatus.MARRIED, null
            );

            when(personRepository.findById(person1Id)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class,
                    () -> marriageService.createMarriage(request));
        }

        @Test
        @DisplayName("should throw when same person for both")
        void shouldThrowWhenSamePersonForBoth() {
            var request = new CreateMarriageRequest(
                    person1Id, person1Id,
                    LocalDate.of(2010, 6, 15), null,
                    MarriageStatus.MARRIED, null
            );

            when(personRepository.findById(person1Id)).thenReturn(Optional.of(person1));

            assertThrows(IllegalArgumentException.class,
                    () -> marriageService.createMarriage(request));
        }
    }

    @Nested
    @DisplayName("getMarriageById")
    class GetMarriageByIdTests {

        @Test
        @DisplayName("should return marriage when found")
        void shouldReturnMarriageWhenFound() {
            when(marriageRepository.findById(marriageId))
                    .thenReturn(Optional.of(sampleMarriage));

            var result = marriageService.getMarriageById(marriageId);

            assertTrue(result.isPresent());
            assertEquals(marriageId, result.get().id());
        }

        @Test
        @DisplayName("should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            var unknownId = UUID.randomUUID();
            when(marriageRepository.findById(unknownId)).thenReturn(Optional.empty());

            var result = marriageService.getMarriageById(unknownId);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("deleteMarriage")
    class DeleteMarriageTests {

        @Test
        @DisplayName("should delete marriage when found")
        void shouldDeleteMarriageWhenFound() {
            when(marriageRepository.existsById(marriageId)).thenReturn(true);

            var result = marriageService.deleteMarriage(marriageId);

            assertTrue(result);
            verify(marriageRepository).deleteById(marriageId);
        }

        @Test
        @DisplayName("should return false when not found")
        void shouldReturnFalseWhenNotFound() {
            var unknownId = UUID.randomUUID();
            when(marriageRepository.existsById(unknownId)).thenReturn(false);

            var result = marriageService.deleteMarriage(unknownId);

            assertFalse(result);
            verify(marriageRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("hasActiveMarriage")
    class HasActiveMarriageTests {

        @Test
        @DisplayName("should return true when active marriage exists")
        void shouldReturnTrueWhenActiveMarriageExists() {
            when(marriageRepository.hasActiveMarriage(person1Id)).thenReturn(true);

            assertTrue(marriageService.hasActiveMarriage(person1Id));
        }

        @Test
        @DisplayName("should return false when no active marriage")
        void shouldReturnFalseWhenNoActiveMarriage() {
            when(marriageRepository.hasActiveMarriage(person1Id)).thenReturn(false);

            assertFalse(marriageService.hasActiveMarriage(person1Id));
        }
    }

    private Person createPerson(final String firstName, final String lastName, final UUID id) throws Exception {
        var person = new Person(firstName, lastName,
                LocalDate.of(1980, 5, 15), FilingStatus.MARRIED_FILING_JOINTLY, "CA");
        setPrivateField(person, "id", id);
        return person;
    }

    private void setPrivateField(final Object target, final String fieldName, final Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
