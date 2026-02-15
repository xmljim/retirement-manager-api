package com.xmljim.retirement.api.person;

import com.xmljim.retirement.api.dto.person.CreatePersonRequest;
import com.xmljim.retirement.api.dto.person.PersonDto;
import com.xmljim.retirement.api.dto.person.UpdatePersonRequest;
import com.xmljim.retirement.api.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

/**
 * API contract for Person management operations.
 *
 * <p>Defines the REST API endpoints for managing persons in the retirement planning system.
 * This interface contains all OpenAPI/Swagger documentation annotations, separating the
 * API contract from the implementation.</p>
 *
 * @since 1.0
 */
@Tag(name = "Persons", description = "Person management operations")
public interface PersonOperations {

    /**
     * Creates a new person.
     *
     * @param request the creation request containing person details
     * @return the created person with HTTP 201 status and Location header
     */
    @Operation(
        summary = "Create a person",
        description = "Creates a new person in the retirement planning system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Person created successfully",
            content = @Content(schema = @Schema(implementation = PersonDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<PersonDto> createPerson(
            @Valid @RequestBody CreatePersonRequest request);

    /**
     * Retrieves a person by their unique identifier.
     *
     * @param id the person's UUID
     * @return the person if found
     */
    @Operation(
        summary = "Get a person by ID",
        description = "Retrieves a person by their unique identifier"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Person found",
            content = @Content(schema = @Schema(implementation = PersonDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<PersonDto> getPersonById(
            @Parameter(description = "Person ID", required = true)
            @PathVariable UUID id);

    /**
     * Retrieves all persons in the system.
     *
     * @return list of all persons
     */
    @Operation(
        summary = "List all persons",
        description = "Retrieves all persons in the retirement planning system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Persons retrieved successfully"
        )
    })
    ResponseEntity<List<PersonDto>> getAllPersons();

    /**
     * Updates an existing person's information.
     *
     * @param id      the person's UUID
     * @param request the update request containing updated person details
     * @return the updated person
     */
    @Operation(
        summary = "Update a person",
        description = "Updates an existing person's information"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Person updated successfully",
            content = @Content(schema = @Schema(implementation = PersonDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<PersonDto> updatePerson(
            @Parameter(description = "Person ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePersonRequest request);

    /**
     * Deletes a person from the system.
     *
     * @param id the person's UUID
     * @return HTTP 204 No Content on success
     */
    @Operation(
        summary = "Delete a person",
        description = "Deletes a person from the retirement planning system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Person deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Person not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<Void> deletePerson(
            @Parameter(description = "Person ID", required = true)
            @PathVariable UUID id);
}
