package com.xmljim.retirement.service.person;

import com.xmljim.retirement.api.dto.person.CreatePersonRequest;
import com.xmljim.retirement.api.dto.person.PersonDto;
import com.xmljim.retirement.api.dto.person.UpdatePersonRequest;
import com.xmljim.retirement.domain.person.FilingStatus;
import com.xmljim.retirement.domain.person.Person;
import com.xmljim.retirement.repository.person.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link PersonService}.
 *
 * <p>Provides transactional business logic for person management.</p>
 *
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    /** The person repository. */
    private final PersonRepository personRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param repository the person repository
     */
    public PersonServiceImpl(final PersonRepository repository) {
        this.personRepository = repository;
    }

    @Override
    @Transactional
    public PersonDto createPerson(final CreatePersonRequest request) {
        var person = new Person(
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                request.filingStatus(),
                request.stateOfResidence()
        );
        var saved = personRepository.save(person);
        return PersonDto.fromEntity(saved);
    }

    @Override
    public Optional<PersonDto> getPersonById(final UUID id) {
        return personRepository.findById(id)
                .map(PersonDto::fromEntity);
    }

    @Override
    public Page<PersonDto> getAllPersons(final Pageable pageable) {
        return personRepository.findAll(pageable)
                .map(PersonDto::fromEntity);
    }

    @Override
    public List<PersonDto> getPersonsByLastName(final String lastName) {
        return personRepository.findByLastName(lastName).stream()
                .map(PersonDto::fromEntity)
                .toList();
    }

    @Override
    public List<PersonDto> getPersonsByFilingStatus(final FilingStatus filingStatus) {
        return personRepository.findByFilingStatus(filingStatus).stream()
                .map(PersonDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public Optional<PersonDto> updatePerson(final UUID id, final UpdatePersonRequest request) {
        return personRepository.findById(id)
                .map(person -> {
                    person.setFirstName(request.firstName());
                    person.setLastName(request.lastName());
                    person.setDateOfBirth(request.dateOfBirth());
                    person.setFilingStatus(request.filingStatus());
                    person.setStateOfResidence(request.stateOfResidence());
                    return PersonDto.fromEntity(personRepository.save(person));
                });
    }

    @Override
    @Transactional
    public boolean deletePerson(final UUID id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByNameAndDateOfBirth(final String firstName,
                                               final String lastName,
                                               final LocalDate dateOfBirth) {
        return personRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                firstName, lastName, dateOfBirth);
    }
}
