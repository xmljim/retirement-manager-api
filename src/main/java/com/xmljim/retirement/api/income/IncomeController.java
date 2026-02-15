package com.xmljim.retirement.api.income;

import com.xmljim.retirement.api.dto.income.CreateIncomeRequest;
import com.xmljim.retirement.api.dto.income.IncomeDto;
import com.xmljim.retirement.api.dto.income.UpdateIncomeRequest;
import com.xmljim.retirement.api.exception.ResourceNotFoundException;
import com.xmljim.retirement.service.income.IncomeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST controller implementing Income management operations.
 *
 * <p>Implementation of {@link IncomeOperations} providing CRUD endpoints
 * for managing income records in the retirement planning system.</p>
 *
 * @since 1.0
 */
@RestController
public final class IncomeController implements IncomeOperations {

    /** The income service. */
    private final IncomeService incomeService;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param service the income service
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Spring DI - service is managed by Spring container")
    public IncomeController(final IncomeService service) {
        this.incomeService = service;
    }

    @Override
    public ResponseEntity<IncomeDto> createIncome(
            @Valid @RequestBody final CreateIncomeRequest request) {
        var created = incomeService.createIncome(request);
        var location = URI.create("/api/v1/income/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public ResponseEntity<IncomeDto> getIncomeById(@PathVariable final UUID id) {
        return incomeService.getIncomeById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Income", id));
    }

    @Override
    public ResponseEntity<List<IncomeDto>> getIncomeByEmploymentId(
            @PathVariable final UUID employmentId) {
        return ResponseEntity.ok(incomeService.getIncomeByEmploymentId(employmentId));
    }

    @Override
    public ResponseEntity<List<IncomeDto>> getIncomeByPersonId(
            @PathVariable final UUID personId) {
        return ResponseEntity.ok(incomeService.getIncomeByPersonId(personId));
    }

    @Override
    public ResponseEntity<List<IncomeDto>> getIncomeByPersonIdAndYear(
            @PathVariable final UUID personId,
            @PathVariable final int year) {
        return ResponseEntity.ok(incomeService.getIncomeByPersonIdAndYear(personId, year));
    }

    @Override
    public ResponseEntity<IncomeDto> updateIncome(
            @PathVariable final UUID id,
            @Valid @RequestBody final UpdateIncomeRequest request) {
        return incomeService.updateIncome(id, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Income", id));
    }

    @Override
    public ResponseEntity<Void> deleteIncome(@PathVariable final UUID id) {
        if (!incomeService.deleteIncome(id)) {
            throw new ResourceNotFoundException("Income", id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
