package com.xmljim.retirement.api.employment;

import com.xmljim.retirement.api.dto.employment.CreateEmploymentRequest;
import com.xmljim.retirement.api.dto.employment.EmploymentDto;
import com.xmljim.retirement.api.dto.employment.UpdateEmploymentRequest;
import com.xmljim.retirement.api.exception.ResourceNotFoundException;
import com.xmljim.retirement.service.employment.EmploymentService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller implementing Employment management operations.
 *
 * <p>Implementation of {@link EmploymentOperations} providing CRUD endpoints
 * for managing employment records in the retirement planning system.</p>
 *
 * @since 1.0
 */
@RestController
public final class EmploymentController implements EmploymentOperations {

    /** The employment service. */
    private final EmploymentService employmentService;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param service the employment service
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Spring DI - service is managed by Spring container")
    public EmploymentController(final EmploymentService service) {
        this.employmentService = service;
    }

    @Override
    public ResponseEntity<EmploymentDto> createEmployment(
            @Valid @RequestBody final CreateEmploymentRequest request) {
        var created = employmentService.createEmployment(request);
        var location = URI.create("/api/v1/employment/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<EmploymentDto> getEmploymentById(@PathVariable final UUID id) {
        return employmentService.getEmploymentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Employment", id));
    }

    @Override
    public ResponseEntity<List<EmploymentDto>> getEmploymentByPersonId(
            @RequestParam final UUID personId) {
        return ResponseEntity.ok(employmentService.getEmploymentByPersonId(personId));
    }

    @Override
    public ResponseEntity<List<EmploymentDto>> getCurrentEmploymentByPersonId(
            @RequestParam final UUID personId) {
        return ResponseEntity.ok(employmentService.getCurrentEmploymentByPersonId(personId));
    }

    @Override
    public ResponseEntity<EmploymentDto> updateEmployment(
            @PathVariable final UUID id,
            @Valid @RequestBody final UpdateEmploymentRequest request) {
        return employmentService.updateEmployment(id, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Employment", id));
    }

    @Override
    public ResponseEntity<Void> deleteEmployment(@PathVariable final UUID id) {
        if (!employmentService.deleteEmployment(id)) {
            throw new ResourceNotFoundException("Employment", id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
