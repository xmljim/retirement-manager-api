package com.xmljim.retirement.service.employer;

import com.xmljim.retirement.api.dto.employer.CreateEmployerRequest;
import com.xmljim.retirement.api.dto.employer.EmployerDto;
import com.xmljim.retirement.api.dto.employer.UpdateEmployerRequest;
import com.xmljim.retirement.domain.employer.Employer;
import com.xmljim.retirement.repository.employer.EmployerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link EmployerService}.
 *
 * <p>Provides transactional business logic for employer management.</p>
 *
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class EmployerServiceImpl implements EmployerService {

    /** The employer repository. */
    private final EmployerRepository employerRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param repository the employer repository
     */
    public EmployerServiceImpl(final EmployerRepository repository) {
        this.employerRepository = repository;
    }

    @Override
    @Transactional
    public EmployerDto createEmployer(final CreateEmployerRequest request) {
        var employer = new Employer(request.name());
        employer.setEin(request.ein());
        employer.setAddressLine1(request.addressLine1());
        employer.setAddressLine2(request.addressLine2());
        employer.setCity(request.city());
        employer.setState(request.state());
        employer.setZipCode(request.zipCode());

        var saved = employerRepository.save(employer);
        return EmployerDto.fromEntity(saved);
    }

    @Override
    public Optional<EmployerDto> getEmployerById(final UUID id) {
        return employerRepository.findById(id)
                .map(EmployerDto::fromEntity);
    }

    @Override
    public Page<EmployerDto> getAllEmployers(final Pageable pageable) {
        return employerRepository.findAll(pageable)
                .map(EmployerDto::fromEntity);
    }

    @Override
    public List<EmployerDto> searchEmployersByName(final String name) {
        return employerRepository.findByNameContainingIgnoreCase(name).stream()
                .map(EmployerDto::fromEntity)
                .toList();
    }

    @Override
    public Optional<EmployerDto> getEmployerByEin(final String ein) {
        return employerRepository.findByEin(ein)
                .map(EmployerDto::fromEntity);
    }

    @Override
    @Transactional
    public Optional<EmployerDto> updateEmployer(final UUID id, final UpdateEmployerRequest request) {
        return employerRepository.findById(id)
                .map(employer -> {
                    employer.setName(request.name());
                    employer.setEin(request.ein());
                    employer.setAddressLine1(request.addressLine1());
                    employer.setAddressLine2(request.addressLine2());
                    employer.setCity(request.city());
                    employer.setState(request.state());
                    employer.setZipCode(request.zipCode());
                    return EmployerDto.fromEntity(employerRepository.save(employer));
                });
    }

    @Override
    @Transactional
    public boolean deleteEmployer(final UUID id) {
        if (employerRepository.existsById(id)) {
            employerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
