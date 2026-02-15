package com.xmljim.retirement.service.employment;

import com.xmljim.retirement.api.dto.employment.CreateEmploymentRequest;
import com.xmljim.retirement.api.dto.employment.EmploymentDto;
import com.xmljim.retirement.api.dto.employment.UpdateEmploymentRequest;
import com.xmljim.retirement.api.exception.ResourceNotFoundException;
import com.xmljim.retirement.domain.employment.Employment;
import com.xmljim.retirement.repository.employer.EmployerRepository;
import com.xmljim.retirement.repository.employment.EmploymentRepository;
import com.xmljim.retirement.repository.person.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link EmploymentService}.
 *
 * <p>Provides transactional business logic for employment management.</p>
 *
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class EmploymentServiceImpl implements EmploymentService {

    /** The employment repository. */
    private final EmploymentRepository employmentRepository;

    /** The person repository. */
    private final PersonRepository personRepository;

    /** The employer repository. */
    private final EmployerRepository employerRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param empRepository    the employment repository
     * @param persRepository   the person repository
     * @param emplrRepository  the employer repository
     */
    public EmploymentServiceImpl(final EmploymentRepository empRepository,
                                  final PersonRepository persRepository,
                                  final EmployerRepository emplrRepository) {
        this.employmentRepository = empRepository;
        this.personRepository = persRepository;
        this.employerRepository = emplrRepository;
    }

    @Override
    @Transactional
    public EmploymentDto createEmployment(final CreateEmploymentRequest request) {
        var person = personRepository.findById(request.personId())
                .orElseThrow(() -> new ResourceNotFoundException("Person", request.personId()));

        var employer = employerRepository.findById(request.employerId())
                .orElseThrow(() -> new ResourceNotFoundException("Employer", request.employerId()));

        var employment = new Employment(
                person,
                employer,
                request.startDate(),
                request.employmentType()
        );
        employment.setJobTitle(request.jobTitle());
        employment.setEndDate(request.endDate());
        employment.setRetirementPlanEligible(request.isRetirementPlanEligible());

        var saved = employmentRepository.save(employment);
        return EmploymentDto.fromEntity(saved);
    }

    @Override
    public Optional<EmploymentDto> getEmploymentById(final UUID id) {
        return employmentRepository.findById(id)
                .map(EmploymentDto::fromEntity);
    }

    @Override
    public List<EmploymentDto> getEmploymentByPersonId(final UUID personId) {
        return employmentRepository.findByPersonId(personId).stream()
                .map(EmploymentDto::fromEntity)
                .toList();
    }

    @Override
    public List<EmploymentDto> getCurrentEmploymentByPersonId(final UUID personId) {
        return employmentRepository.findCurrentByPersonId(personId).stream()
                .map(EmploymentDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public Optional<EmploymentDto> updateEmployment(final UUID id,
                                                     final UpdateEmploymentRequest request) {
        return employmentRepository.findById(id)
                .map(employment -> {
                    var employer = employerRepository.findById(request.employerId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Employer", request.employerId()));

                    employment.setEmployer(employer);
                    employment.setJobTitle(request.jobTitle());
                    employment.setStartDate(request.startDate());
                    employment.setEndDate(request.endDate());
                    employment.setEmploymentType(request.employmentType());
                    employment.setRetirementPlanEligible(request.retirementPlanEligible());

                    return EmploymentDto.fromEntity(employmentRepository.save(employment));
                });
    }

    @Override
    @Transactional
    public boolean deleteEmployment(final UUID id) {
        if (employmentRepository.existsById(id)) {
            employmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
