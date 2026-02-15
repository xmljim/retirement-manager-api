package com.xmljim.retirement.service.marriage;

import com.xmljim.retirement.api.dto.marriage.CreateMarriageRequest;
import com.xmljim.retirement.api.dto.marriage.MarriageDto;
import com.xmljim.retirement.api.dto.marriage.UpdateMarriageRequest;
import com.xmljim.retirement.domain.marriage.Marriage;
import com.xmljim.retirement.domain.marriage.MarriageStatus;
import com.xmljim.retirement.repository.marriage.MarriageRepository;
import com.xmljim.retirement.repository.person.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link MarriageService}.
 *
 * <p>Provides transactional business logic for marriage management.</p>
 *
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class MarriageServiceImpl implements MarriageService {

    /** The marriage repository. */
    private final MarriageRepository marriageRepository;

    /** The person repository. */
    private final PersonRepository personRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param marriageRepo the marriage repository
     * @param personRepo   the person repository
     */
    public MarriageServiceImpl(final MarriageRepository marriageRepo,
                               final PersonRepository personRepo) {
        this.marriageRepository = marriageRepo;
        this.personRepository = personRepo;
    }

    @Override
    @Transactional
    public MarriageDto createMarriage(final CreateMarriageRequest request) {
        var person1 = personRepository.findById(request.person1Id())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person 1 not found: " + request.person1Id()));

        var person2 = personRepository.findById(request.person2Id())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person 2 not found: " + request.person2Id()));

        if (request.person1Id().equals(request.person2Id())) {
            throw new IllegalArgumentException(
                    "Person 1 and Person 2 must be different people");
        }

        if (request.divorceDate() != null
                && request.divorceDate().isBefore(request.marriageDate())) {
            throw new IllegalArgumentException(
                    "Divorce date must be after marriage date");
        }

        var marriage = new Marriage(
                person1,
                person2,
                request.marriageDate(),
                request.status(),
                request.notes()
        );
        marriage.setDivorceDate(request.divorceDate());

        var saved = marriageRepository.save(marriage);
        return MarriageDto.fromEntity(saved);
    }

    @Override
    public Optional<MarriageDto> getMarriageById(final UUID id) {
        return marriageRepository.findById(id)
                .map(MarriageDto::fromEntity);
    }

    @Override
    public List<MarriageDto> getAllMarriages() {
        return marriageRepository.findAll().stream()
                .map(MarriageDto::fromEntity)
                .toList();
    }

    @Override
    public List<MarriageDto> getMarriagesByPersonId(final UUID personId) {
        return marriageRepository.findByPersonId(personId).stream()
                .map(MarriageDto::fromEntity)
                .toList();
    }

    @Override
    public List<MarriageDto> getMarriagesByStatus(final MarriageStatus status) {
        return marriageRepository.findByStatus(status).stream()
                .map(MarriageDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public Optional<MarriageDto> updateMarriage(final UUID id,
                                                 final UpdateMarriageRequest request) {
        return marriageRepository.findById(id)
                .map(marriage -> {
                    if (request.divorceDate() != null
                            && request.divorceDate().isBefore(request.marriageDate())) {
                        throw new IllegalArgumentException(
                                "Divorce date must be after marriage date");
                    }

                    marriage.setMarriageDate(request.marriageDate());
                    marriage.setDivorceDate(request.divorceDate());
                    marriage.setStatus(request.status());
                    marriage.setNotes(request.notes());
                    return MarriageDto.fromEntity(marriageRepository.save(marriage));
                });
    }

    @Override
    @Transactional
    public boolean deleteMarriage(final UUID id) {
        if (marriageRepository.existsById(id)) {
            marriageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasActiveMarriage(final UUID personId) {
        return marriageRepository.hasActiveMarriage(personId);
    }

    @Override
    public Optional<Boolean> checkSpousalBenefitEligibility(final UUID marriageId) {
        return marriageRepository.findById(marriageId)
                .map(Marriage::isEligibleForSpousalBenefits);
    }

    @Override
    public List<MarriageDto> getSpousalBenefitEligibleMarriages(final UUID personId) {
        return marriageRepository.findByPersonId(personId).stream()
                .filter(Marriage::isEligibleForSpousalBenefits)
                .map(MarriageDto::fromEntity)
                .toList();
    }
}
