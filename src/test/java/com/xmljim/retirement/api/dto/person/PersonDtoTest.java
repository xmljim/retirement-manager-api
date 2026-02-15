package com.xmljim.retirement.api.dto.person;

import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link PersonDto}.
 *
 * @since 1.0
 */
@DisplayName("PersonDto")
class PersonDtoTest {

    @Test
    @DisplayName("should create DTO from entity")
    void shouldCreateDtoFromEntity() throws Exception {
        var person = new Person(
                "John",
                "Doe",
                LocalDate.of(1980, 5, 15),
                FilingStatus.SINGLE,
                "CA"
        );

        // Set ID using reflection
        var id = UUID.randomUUID();
        var idField = Person.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(person, id);

        // Set timestamps using reflection
        var now = Instant.now();
        var createdAtField = Person.class.getDeclaredField("createdAt");
        createdAtField.setAccessible(true);
        createdAtField.set(person, now);

        var updatedAtField = Person.class.getDeclaredField("updatedAt");
        updatedAtField.setAccessible(true);
        updatedAtField.set(person, now);

        var dto = PersonDto.fromEntity(person);

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals("John", dto.firstName());
        assertEquals("Doe", dto.lastName());
        assertEquals(LocalDate.of(1980, 5, 15), dto.dateOfBirth());
        assertEquals(FilingStatus.SINGLE, dto.filingStatus());
        assertEquals("CA", dto.stateOfResidence());
        assertNotNull(dto.createdAt());
        assertNotNull(dto.updatedAt());
    }

    @Test
    @DisplayName("should handle null state of residence from entity")
    void shouldHandleNullStateOfResidenceFromEntity() throws Exception {
        var person = new Person(
                "Jane",
                "Smith",
                LocalDate.of(1990, 3, 20),
                FilingStatus.MARRIED_FILING_JOINTLY,
                null
        );

        var id = UUID.randomUUID();
        var idField = Person.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(person, id);

        var now = Instant.now();
        var createdAtField = Person.class.getDeclaredField("createdAt");
        createdAtField.setAccessible(true);
        createdAtField.set(person, now);

        var updatedAtField = Person.class.getDeclaredField("updatedAt");
        updatedAtField.setAccessible(true);
        updatedAtField.set(person, now);

        var dto = PersonDto.fromEntity(person);

        assertNull(dto.stateOfResidence());
    }

    @Test
    @DisplayName("should construct directly with all fields")
    void shouldConstructDirectlyWithAllFields() {
        var id = UUID.randomUUID();
        var now = Instant.now();

        var dto = new PersonDto(
                id,
                "John",
                "Doe",
                LocalDate.of(1980, 5, 15),
                FilingStatus.SINGLE,
                "CA",
                now,
                now
        );

        assertEquals(id, dto.id());
        assertEquals("John", dto.firstName());
        assertEquals("Doe", dto.lastName());
        assertEquals(LocalDate.of(1980, 5, 15), dto.dateOfBirth());
        assertEquals(FilingStatus.SINGLE, dto.filingStatus());
        assertEquals("CA", dto.stateOfResidence());
        assertEquals(now, dto.createdAt());
        assertEquals(now, dto.updatedAt());
    }
}
