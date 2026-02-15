package com.xmljim.retirement.api.person;

import com.xmljim.retirement.api.dto.person.CreatePersonRequest;
import com.xmljim.retirement.api.dto.person.PersonDto;
import com.xmljim.retirement.api.dto.person.UpdatePersonRequest;
import com.xmljim.retirement.api.exception.ResourceNotFoundException;
import com.xmljim.retirement.service.person.PersonService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
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
@RequestMapping("/api/v1/persons")
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
    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody final CreatePersonRequest request) {
        var created = personService.createPerson(request);
        var location = URI.create("/api/v1/persons/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable final UUID id) {
        return personService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PersonDto>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> updatePerson(
            @PathVariable final UUID id,
            @RequestBody final UpdatePersonRequest request) {
        return personService.updatePerson(id, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable final UUID id) {
        if (!personService.deletePerson(id)) {
            throw new ResourceNotFoundException("Person", id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
