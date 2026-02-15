package com.xmljim.retirement.api.marriage;

import com.xmljim.retirement.api.dto.marriage.CreateMarriageRequest;
import com.xmljim.retirement.api.dto.marriage.MarriageDto;
import com.xmljim.retirement.api.dto.marriage.UpdateMarriageRequest;
import com.xmljim.retirement.api.exception.ResourceNotFoundException;
import com.xmljim.retirement.service.marriage.MarriageService;
import com.xmljim.retirement.service.person.PersonService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller implementing Marriage management operations.
 *
 * <p>Implementation of {@link MarriageOperations} providing endpoints
 * for managing marriage records in the retirement planning system.</p>
 *
 * @since 1.0
 */
@RestController
public final class MarriageController implements MarriageOperations {

    /** The marriage service. */
    private final MarriageService marriageService;

    /** The person service for validation. */
    private final PersonService personService;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param marriageSvc the marriage service
     * @param personSvc   the person service
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Spring DI - services are managed by Spring container")
    public MarriageController(final MarriageService marriageSvc,
                              final PersonService personSvc) {
        this.marriageService = marriageSvc;
        this.personService = personSvc;
    }

    @Override
    public ResponseEntity<MarriageDto> createMarriage(
            @PathVariable final UUID personId,
            @Valid @RequestBody final CreateMarriageRequest request) {
        // Verify person exists
        personService.getPersonById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person", personId));

        // Validate that personId matches one of the persons in the request
        if (!personId.equals(request.person1Id()) && !personId.equals(request.person2Id())) {
            throw new IllegalArgumentException(
                    "Person ID in path must match either person1Id or person2Id in request");
        }

        var created = marriageService.createMarriage(request);
        var location = URI.create("/api/v1/marriages/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<List<MarriageDto>> getMarriagesByPersonId(
            @PathVariable final UUID personId) {
        // Verify person exists
        personService.getPersonById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person", personId));

        return ResponseEntity.ok(marriageService.getMarriagesByPersonId(personId));
    }

    @Override
    public ResponseEntity<MarriageDto> updateMarriage(
            @PathVariable final UUID id,
            @Valid @RequestBody final UpdateMarriageRequest request) {
        return marriageService.updateMarriage(id, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage", id));
    }

    @Override
    public ResponseEntity<MarriageDto> getMarriageById(@PathVariable final UUID id) {
        return marriageService.getMarriageById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Marriage", id));
    }
}
