package com.xmljim.retirement.api.employer;

import com.xmljim.retirement.api.dto.employer.CreateEmployerRequest;
import com.xmljim.retirement.api.dto.employer.EmployerDto;
import com.xmljim.retirement.api.dto.employer.UpdateEmployerRequest;
import com.xmljim.retirement.api.exception.ResourceNotFoundException;
import com.xmljim.retirement.service.employer.EmployerService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller implementing Employer management operations.
 *
 * <p>Implementation of {@link EmployerOperations} providing CRUD endpoints
 * for managing employers in the retirement planning system.</p>
 *
 * @since 1.0
 */
@RestController
public final class EmployerController implements EmployerOperations {

    /** The employer service. */
    private final EmployerService employerService;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param service the employer service
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Spring DI - service is managed by Spring container")
    public EmployerController(final EmployerService service) {
        this.employerService = service;
    }

    @Override
    public ResponseEntity<EmployerDto> createEmployer(
            @Valid @RequestBody final CreateEmployerRequest request) {
        var created = employerService.createEmployer(request);
        var location = URI.create("/api/v1/employers/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<EmployerDto> getEmployerById(@PathVariable final UUID id) {
        return employerService.getEmployerById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Employer", id));
    }

    @Override
    public ResponseEntity<Page<EmployerDto>> getAllEmployers(final Pageable pageable) {
        return ResponseEntity.ok(employerService.getAllEmployers(pageable));
    }

    @Override
    public ResponseEntity<List<EmployerDto>> searchEmployersByName(
            @RequestParam final String name) {
        return ResponseEntity.ok(employerService.searchEmployersByName(name));
    }

    @Override
    public ResponseEntity<EmployerDto> updateEmployer(
            @PathVariable final UUID id,
            @Valid @RequestBody final UpdateEmployerRequest request) {
        return employerService.updateEmployer(id, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Employer", id));
    }

    @Override
    public ResponseEntity<Void> deleteEmployer(@PathVariable final UUID id) {
        if (!employerService.deleteEmployer(id)) {
            throw new ResourceNotFoundException("Employer", id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
