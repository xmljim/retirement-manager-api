package com.xmljim.retirement.api.dto.marriage;

import com.xmljim.retirement.domain.marriage.Marriage;
import com.xmljim.retirement.domain.marriage.MarriageStatus;
import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link MarriageDto}.
 *
 * @since 1.0
 */
@DisplayName("MarriageDto")
class MarriageDtoTest {

    @Test
    @DisplayName("should create DTO from entity")
    void shouldCreateDtoFromEntity() throws Exception {
        var person1 = createPerson("John", "Doe", UUID.randomUUID());
        var person2 = createPerson("Jane", "Doe", UUID.randomUUID());

        var marriage = new Marriage(
                person1,
                person2,
                LocalDate.of(2010, 6, 15),
                MarriageStatus.MARRIED,
                "First marriage"
        );

        var id = UUID.randomUUID();
        setPrivateField(marriage, "id", id);

        var now = Instant.now();
        setPrivateField(marriage, "createdAt", now);
        setPrivateField(marriage, "updatedAt", now);

        var dto = MarriageDto.fromEntity(marriage);

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals(person1.getId(), dto.person1Id());
        assertEquals(person2.getId(), dto.person2Id());
        assertEquals(LocalDate.of(2010, 6, 15), dto.marriageDate());
        assertNull(dto.divorceDate());
        assertEquals(MarriageStatus.MARRIED, dto.status());
        assertEquals("First marriage", dto.notes());
        assertNotNull(dto.createdAt());
        assertNotNull(dto.updatedAt());
    }

    @Test
    @DisplayName("should include marriage duration years")
    void shouldIncludeMarriageDurationYears() throws Exception {
        var person1 = createPerson("John", "Doe", UUID.randomUUID());
        var person2 = createPerson("Jane", "Doe", UUID.randomUUID());

        var marriage = new Marriage(
                person1,
                person2,
                LocalDate.of(2010, 6, 15),
                MarriageStatus.DIVORCED,
                null
        );
        marriage.setDivorceDate(LocalDate.of(2020, 6, 15));

        setPrivateField(marriage, "id", UUID.randomUUID());
        setPrivateField(marriage, "createdAt", Instant.now());
        setPrivateField(marriage, "updatedAt", Instant.now());

        var dto = MarriageDto.fromEntity(marriage);

        assertEquals(10, dto.marriageDurationYears());
    }

    @Test
    @DisplayName("should indicate spousal benefit eligibility when eligible")
    void shouldIndicateSpousalBenefitEligibilityWhenEligible() throws Exception {
        var person1 = createPerson("John", "Doe", UUID.randomUUID());
        var person2 = createPerson("Jane", "Doe", UUID.randomUUID());

        var marriage = new Marriage(
                person1,
                person2,
                LocalDate.of(2010, 6, 15),
                MarriageStatus.DIVORCED,
                null
        );
        marriage.setDivorceDate(LocalDate.of(2020, 6, 15));

        setPrivateField(marriage, "id", UUID.randomUUID());
        setPrivateField(marriage, "createdAt", Instant.now());
        setPrivateField(marriage, "updatedAt", Instant.now());

        var dto = MarriageDto.fromEntity(marriage);

        assertTrue(dto.eligibleForSpousalBenefits());
    }

    @Test
    @DisplayName("should indicate not eligible for spousal benefits when less than 10 years")
    void shouldIndicateNotEligibleWhenLessThanTenYears() throws Exception {
        var person1 = createPerson("John", "Doe", UUID.randomUUID());
        var person2 = createPerson("Jane", "Doe", UUID.randomUUID());

        var marriage = new Marriage(
                person1,
                person2,
                LocalDate.of(2015, 6, 15),
                MarriageStatus.DIVORCED,
                null
        );
        marriage.setDivorceDate(LocalDate.of(2020, 6, 14));

        setPrivateField(marriage, "id", UUID.randomUUID());
        setPrivateField(marriage, "createdAt", Instant.now());
        setPrivateField(marriage, "updatedAt", Instant.now());

        var dto = MarriageDto.fromEntity(marriage);

        assertFalse(dto.eligibleForSpousalBenefits());
    }

    private Person createPerson(final String firstName,
                                 final String lastName,
                                 final UUID id) throws Exception {
        var person = new Person(
                firstName,
                lastName,
                LocalDate.of(1980, 5, 15),
                FilingStatus.MARRIED_FILING_JOINTLY,
                "CA"
        );
        setPrivateField(person, "id", id);
        setPrivateField(person, "createdAt", Instant.now());
        setPrivateField(person, "updatedAt", Instant.now());
        return person;
    }

    private void setPrivateField(final Object target,
                                  final String fieldName,
                                  final Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
