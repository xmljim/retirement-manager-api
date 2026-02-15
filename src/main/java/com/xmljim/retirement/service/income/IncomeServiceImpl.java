package com.xmljim.retirement.service.income;

import com.xmljim.retirement.api.dto.income.CreateIncomeRequest;
import com.xmljim.retirement.api.dto.income.IncomeDto;
import com.xmljim.retirement.api.dto.income.UpdateIncomeRequest;
import com.xmljim.retirement.api.exception.ResourceNotFoundException;
import com.xmljim.retirement.domain.income.Income;
import com.xmljim.retirement.repository.employment.EmploymentRepository;
import com.xmljim.retirement.repository.income.IncomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link IncomeService}.
 *
 * <p>Provides transactional business logic for income management.</p>
 *
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class IncomeServiceImpl implements IncomeService {

    /** The income repository. */
    private final IncomeRepository incomeRepository;

    /** The employment repository. */
    private final EmploymentRepository employmentRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param incRepository the income repository
     * @param empRepository the employment repository
     */
    public IncomeServiceImpl(final IncomeRepository incRepository,
                              final EmploymentRepository empRepository) {
        this.incomeRepository = incRepository;
        this.employmentRepository = empRepository;
    }

    @Override
    @Transactional
    public IncomeDto createIncome(final CreateIncomeRequest request) {
        var employment = employmentRepository.findById(request.employmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employment", request.employmentId()));

        var income = new Income(
                employment,
                request.year(),
                request.annualSalary()
        );
        income.setBonus(request.getBonus());
        income.setOtherCompensation(request.getOtherCompensation());
        income.setW2Wages(request.w2Wages());

        var saved = incomeRepository.save(income);
        return IncomeDto.fromEntity(saved);
    }

    @Override
    public Optional<IncomeDto> getIncomeById(final UUID id) {
        return incomeRepository.findById(id)
                .map(IncomeDto::fromEntity);
    }

    @Override
    public List<IncomeDto> getIncomeByEmploymentId(final UUID employmentId) {
        return incomeRepository.findByEmploymentId(employmentId).stream()
                .map(IncomeDto::fromEntity)
                .toList();
    }

    @Override
    public Optional<IncomeDto> getIncomeByEmploymentIdAndYear(final UUID employmentId, final int year) {
        return incomeRepository.findByEmploymentIdAndYear(employmentId, year)
                .map(IncomeDto::fromEntity);
    }

    @Override
    public List<IncomeDto> getIncomeByPersonId(final UUID personId) {
        return incomeRepository.findByPersonId(personId).stream()
                .map(IncomeDto::fromEntity)
                .toList();
    }

    @Override
    public List<IncomeDto> getIncomeByPersonIdAndYear(final UUID personId, final int year) {
        return incomeRepository.findByPersonIdAndYear(personId, year).stream()
                .map(IncomeDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public Optional<IncomeDto> updateIncome(final UUID id, final UpdateIncomeRequest request) {
        return incomeRepository.findById(id)
                .map(income -> {
                    income.setAnnualSalary(request.annualSalary());
                    income.setBonus(request.bonus());
                    income.setOtherCompensation(request.otherCompensation());
                    income.setW2Wages(request.w2Wages());
                    return IncomeDto.fromEntity(incomeRepository.save(income));
                });
    }

    @Override
    @Transactional
    public boolean deleteIncome(final UUID id) {
        if (incomeRepository.existsById(id)) {
            incomeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
