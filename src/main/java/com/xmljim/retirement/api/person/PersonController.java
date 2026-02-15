package com.xmljim.retirement.api.person;

import com.xmljim.retirement.api.dto.person.CreatePersonRequest;
import com.xmljim.retirement.api.dto.person.PersonDto;
import com.xmljim.retirement.api.dto.person.UpdatePersonRequest;
import com.xmljim.retirement.api.exception.ResourceNotFoundException;
import com.xmljim.retirement.service.person.PersonService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

/**
 * REST controller implementing Person management operations.
 *
 * <p>Implementation of {@link PersonOperations} providing CRUD endpoints
 * for managing persons in the retirement planning system.</p>
 *
 * @since 1.0
 */
@RestController
public final class PersonController implements PersonOperations {

    /** The person service. */
    private final PersonService personService;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param service the person service
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Spring DI - service is managed by Spring container")
    public PersonController(final PersonService service) {
        this.personService = service;
    }

    @Override
    public ResponseEntity<PersonDto> createPerson(
            @Valid @RequestBody final CreatePersonRequest request) {
        var created = personService.createPerson(request);
        var location = URI.create("/api/v1/persons/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<PersonDto> getPersonById(@PathVariable final UUID id) {
        return personService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    }

    @Override
    public ResponseEntity<Page<PersonDto>> getAllPersons(final Pageable pageable) {
        return ResponseEntity.ok(personService.getAllPersons(pageable));
    }

    @Override
    public ResponseEntity<PersonDto> updatePerson(
            @PathVariable final UUID id,
            @Valid @RequestBody final UpdatePersonRequest request) {
        return personService.updatePerson(id, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    }

    @Override
    public ResponseEntity<Void> deletePerson(@PathVariable final UUID id) {
        if (!personService.deletePerson(id)) {
            throw new ResourceNotFoundException("Person", id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
