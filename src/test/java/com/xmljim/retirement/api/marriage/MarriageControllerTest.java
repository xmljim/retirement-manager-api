package com.xmljim.retirement.api.marriage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xmljim.retirement.api.dto.marriage.CreateMarriageRequest;
import com.xmljim.retirement.api.dto.marriage.MarriageDto;
import com.xmljim.retirement.api.dto.marriage.UpdateMarriageRequest;
import com.xmljim.retirement.api.dto.person.PersonDto;
import com.xmljim.retirement.domain.marriage.MarriageStatus;
import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.service.marriage.MarriageService;
import com.xmljim.retirement.service.person.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MockMvc integration tests for {@link MarriageController}.
 *
 * @since 1.0
 */
@WebMvcTest(MarriageController.class)
@DisplayName("MarriageController")
class MarriageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MarriageService marriageService;

    @MockitoBean
    private PersonService personService;

    private final ObjectMapper objectMapper = createObjectMapper();
    private UUID person1Id;
    private UUID person2Id;
    private UUID marriageId;
    private PersonDto person1Dto;
    private MarriageDto sampleMarriageDto;

    private static ObjectMapper createObjectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @BeforeEach
    void setUp() {
        person1Id = UUID.randomUUID();
        person2Id = UUID.randomUUID();
        marriageId = UUID.randomUUID();

        person1Dto = new PersonDto(
                person1Id,
                "John",
                "Doe",
                LocalDate.of(1980, 5, 15),
                FilingStatus.MARRIED_FILING_JOINTLY,
                "CA",
                Instant.now(),
                Instant.now()
        );

        sampleMarriageDto = new MarriageDto(
                marriageId,
                person1Id,
                person2Id,
                LocalDate.of(2010, 6, 15),
                null,
                MarriageStatus.MARRIED,
                "First marriage",
                14L,
                true,
                Instant.now(),
                Instant.now()
        );
    }

    @Nested
    @DisplayName("POST /api/v1/persons/{personId}/marriages")
    class CreateMarriageTests {

        @Test
        @DisplayName("should create marriage successfully")
        void shouldCreateMarriageSuccessfully() throws Exception {
            var request = new CreateMarriageRequest(
                    person1Id,
                    person2Id,
                    LocalDate.of(2010, 6, 15),
                    null,
                    MarriageStatus.MARRIED,
                    "First marriage"
            );

            when(personService.getPersonById(person1Id)).thenReturn(Optional.of(person1Dto));
            when(marriageService.createMarriage(any(CreateMarriageRequest.class)))
                    .thenReturn(sampleMarriageDto);

            mockMvc.perform(post("/api/v1/persons/{personId}/marriages", person1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/api/v1/marriages/" + marriageId))
                    .andExpect(jsonPath("$.id", is(marriageId.toString())))
                    .andExpect(jsonPath("$.person1Id", is(person1Id.toString())))
                    .andExpect(jsonPath("$.person2Id", is(person2Id.toString())))
                    .andExpect(jsonPath("$.marriageDate", is("2010-06-15")))
                    .andExpect(jsonPath("$.status", is("MARRIED")));
        }

        @Test
        @DisplayName("should return 404 when person not found")
        void shouldReturn404WhenPersonNotFound() throws Exception {
            var request = new CreateMarriageRequest(
                    person1Id,
                    person2Id,
                    LocalDate.of(2010, 6, 15),
                    null,
                    MarriageStatus.MARRIED,
                    null
            );

            when(personService.getPersonById(person1Id)).thenReturn(Optional.empty());

            mockMvc.perform(post("/api/v1/persons/{personId}/marriages", person1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return 400 when path personId does not match request")
        void shouldReturn400WhenPersonIdMismatch() throws Exception {
            var otherPersonId = UUID.randomUUID();
            var request = new CreateMarriageRequest(
                    person1Id,
                    person2Id,
                    LocalDate.of(2010, 6, 15),
                    null,
                    MarriageStatus.MARRIED,
                    null
            );

            when(personService.getPersonById(otherPersonId)).thenReturn(Optional.of(person1Dto));

            mockMvc.perform(post("/api/v1/persons/{personId}/marriages", otherPersonId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when required fields missing")
        void shouldReturn400WhenRequiredFieldsMissing() throws Exception {
            var request = """
                    {
                        "person1Id": null,
                        "marriageDate": "2010-06-15",
                        "status": "MARRIED"
                    }
                    """;

            when(personService.getPersonById(person1Id)).thenReturn(Optional.of(person1Dto));

            mockMvc.perform(post("/api/v1/persons/{personId}/marriages", person1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/persons/{personId}/marriages")
    class GetMarriagesByPersonIdTests {

        @Test
        @DisplayName("should return marriages for person")
        void shouldReturnMarriagesForPerson() throws Exception {
            when(personService.getPersonById(person1Id)).thenReturn(Optional.of(person1Dto));
            when(marriageService.getMarriagesByPersonId(person1Id))
                    .thenReturn(List.of(sampleMarriageDto));

            mockMvc.perform(get("/api/v1/persons/{personId}/marriages", person1Id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(marriageId.toString())))
                    .andExpect(jsonPath("$[0].person1Id", is(person1Id.toString())));
        }

        @Test
        @DisplayName("should return empty list when no marriages")
        void shouldReturnEmptyListWhenNoMarriages() throws Exception {
            when(personService.getPersonById(person1Id)).thenReturn(Optional.of(person1Dto));
            when(marriageService.getMarriagesByPersonId(person1Id)).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/persons/{personId}/marriages", person1Id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("should return 404 when person not found")
        void shouldReturn404WhenPersonNotFound() throws Exception {
            when(personService.getPersonById(person1Id)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/persons/{personId}/marriages", person1Id))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/marriages/{id}")
    class GetMarriageByIdTests {

        @Test
        @DisplayName("should return marriage when found")
        void shouldReturnMarriageWhenFound() throws Exception {
            when(marriageService.getMarriageById(marriageId))
                    .thenReturn(Optional.of(sampleMarriageDto));

            mockMvc.perform(get("/api/v1/marriages/{id}", marriageId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(marriageId.toString())))
                    .andExpect(jsonPath("$.person1Id", is(person1Id.toString())))
                    .andExpect(jsonPath("$.person2Id", is(person2Id.toString())))
                    .andExpect(jsonPath("$.status", is("MARRIED")))
                    .andExpect(jsonPath("$.marriageDurationYears", is(14)))
                    .andExpect(jsonPath("$.eligibleForSpousalBenefits", is(true)));
        }

        @Test
        @DisplayName("should return 404 when not found")
        void shouldReturn404WhenNotFound() throws Exception {
            when(marriageService.getMarriageById(marriageId)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/marriages/{id}", marriageId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/marriages/{id}")
    class UpdateMarriageTests {

        @Test
        @DisplayName("should update marriage successfully")
        void shouldUpdateMarriageSuccessfully() throws Exception {
            var updatedDto = new MarriageDto(
                    marriageId,
                    person1Id,
                    person2Id,
                    LocalDate.of(2010, 6, 15),
                    LocalDate.of(2020, 6, 15),
                    MarriageStatus.DIVORCED,
                    "Divorced after 10 years",
                    10L,
                    true,
                    sampleMarriageDto.createdAt(),
                    Instant.now()
            );

            var request = new UpdateMarriageRequest(
                    LocalDate.of(2010, 6, 15),
                    LocalDate.of(2020, 6, 15),
                    MarriageStatus.DIVORCED,
                    "Divorced after 10 years"
            );

            when(marriageService.updateMarriage(eq(marriageId), any(UpdateMarriageRequest.class)))
                    .thenReturn(Optional.of(updatedDto));

            mockMvc.perform(put("/api/v1/marriages/{id}", marriageId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(marriageId.toString())))
                    .andExpect(jsonPath("$.status", is("DIVORCED")))
                    .andExpect(jsonPath("$.divorceDate", is("2020-06-15")))
                    .andExpect(jsonPath("$.marriageDurationYears", is(10)));
        }

        @Test
        @DisplayName("should return 404 when marriage not found")
        void shouldReturn404WhenMarriageNotFound() throws Exception {
            var request = new UpdateMarriageRequest(
                    LocalDate.of(2010, 6, 15),
                    LocalDate.of(2020, 6, 15),
                    MarriageStatus.DIVORCED,
                    null
            );

            when(marriageService.updateMarriage(eq(marriageId), any(UpdateMarriageRequest.class)))
                    .thenReturn(Optional.empty());

            mockMvc.perform(put("/api/v1/marriages/{id}", marriageId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return 400 when required fields missing")
        void shouldReturn400WhenRequiredFieldsMissing() throws Exception {
            var request = """
                    {
                        "marriageDate": null,
                        "status": "DIVORCED"
                    }
                    """;

            mockMvc.perform(put("/api/v1/marriages/{id}", marriageId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest());
        }
    }
}
