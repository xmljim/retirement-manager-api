package com.xmljim.retirement.api.contribution;

import com.xmljim.retirement.api.dto.contribution.AccountTypeLimitsDto;
import com.xmljim.retirement.api.dto.contribution.YearlyLimitsDto;
import com.xmljim.retirement.api.exception.DataNotFoundException;
import com.xmljim.retirement.domain.contribution.AccountType;
import com.xmljim.retirement.service.contribution.ContributionLimitsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller implementing contribution limits operations.
 *
 * <p>Implementation of {@link LimitsOperations} providing endpoints
 * for retrieving IRS contribution limits and phase-out ranges
 * for various retirement account types.</p>
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/limits")
public final class LimitsController implements LimitsOperations {

    /** The contribution limits service. */
    private final ContributionLimitsService limitsService;

    /**
     * Constructs the controller with required dependencies.
     *
     * @param service the contribution limits service
     */
    public LimitsController(final ContributionLimitsService service) {
        this.limitsService = service;
    }

    @Override
    @GetMapping("/{year}")
    public ResponseEntity<YearlyLimitsDto> getLimitsByYear(@PathVariable final Integer year) {
        return limitsService.getLimitsByYear(year)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new DataNotFoundException(
                        "Contribution limits", "year: " + year));
    }

    @Override
    @GetMapping("/{year}/{accountType}")
    public ResponseEntity<AccountTypeLimitsDto> getLimitsByYearAndAccountType(
            @PathVariable final Integer year,
            @PathVariable final AccountType accountType) {
        return limitsService.getLimitsByYearAndAccountType(year, accountType)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new DataNotFoundException(
                        "Contribution limits",
                        "year: " + year + ", account type: " + accountType));
    }

    @Override
    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getAvailableYears() {
        return ResponseEntity.ok(limitsService.getAvailableYears());
    }
}
