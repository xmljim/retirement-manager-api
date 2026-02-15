package com.xmljim.retirement.domain.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a person in the retirement planning system.
 *
 * <p>A person is the core entity around which retirement planning revolves.
 * They have personal information, accounts, incomes, and relationships that
 * affect their retirement planning strategy.</p>
 *
 * <p>This class is not designed for extension outside the JPA framework.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "persons")
public class Person {

    /** Maximum length for name fields. */
    public static final int MAX_NAME_LENGTH = 100;

    /** Maximum length for filing status field. */
    public static final int MAX_FILING_STATUS_LENGTH = 50;

    /** Maximum length for state code. */
    public static final int STATE_CODE_LENGTH = 2;

    /** Unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** First name. */
    @Column(name = "first_name", nullable = false, length = MAX_NAME_LENGTH)
    private String firstName;

    /** Last name. */
    @Column(name = "last_name", nullable = false, length = MAX_NAME_LENGTH)
    private String lastName;

    /** Date of birth. */
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    /** Tax filing status. */
    @Enumerated(EnumType.STRING)
    @Column(name = "filing_status", nullable = false, length = MAX_FILING_STATUS_LENGTH)
    private FilingStatus filingStatus;

    /** Two-letter US state code of residence. */
    @Column(name = "state_of_residence", length = STATE_CODE_LENGTH)
    private String stateOfResidence;

    /** Timestamp when the record was created. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /** Timestamp when the record was last updated. */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Default constructor for JPA.
     */
    protected Person() {
        // JPA requires default constructor
    }

    /**
     * Constructs a new Person with the required fields.
     *
     * @param newFirstName       the first name
     * @param newLastName        the last name
     * @param newDateOfBirth     the date of birth
     * @param newFilingStatus    the tax filing status
     * @param newStateOfResidence the two-letter state code (may be null)
     */
    public Person(final String newFirstName,
                  final String newLastName,
                  final LocalDate newDateOfBirth,
                  final FilingStatus newFilingStatus,
                  final String newStateOfResidence) {
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.dateOfBirth = newDateOfBirth;
        this.filingStatus = newFilingStatus;
        this.stateOfResidence = newStateOfResidence;
    }

    /**
     * Sets timestamps before persisting a new entity.
     */
    @PrePersist
    void onCreate() {
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Updates the timestamp before updating an existing entity.
     */
    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Returns the unique identifier.
     *
     * @return the ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param newFirstName the first name
     */
    public void setFirstName(final String newFirstName) {
        this.firstName = newFirstName;
    }

    /**
     * Returns the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param newLastName the last name
     */
    public void setLastName(final String newLastName) {
        this.lastName = newLastName;
    }

    /**
     * Returns the date of birth.
     *
     * @return the date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth.
     *
     * @param newDateOfBirth the date of birth
     */
    public void setDateOfBirth(final LocalDate newDateOfBirth) {
        this.dateOfBirth = newDateOfBirth;
    }

    /**
     * Returns the tax filing status.
     *
     * @return the filing status
     */
    public FilingStatus getFilingStatus() {
        return filingStatus;
    }

    /**
     * Sets the tax filing status.
     *
     * @param newFilingStatus the filing status
     */
    public void setFilingStatus(final FilingStatus newFilingStatus) {
        this.filingStatus = newFilingStatus;
    }

    /**
     * Returns the two-letter state code of residence.
     *
     * @return the state code, or null if not set
     */
    public String getStateOfResidence() {
        return stateOfResidence;
    }

    /**
     * Sets the two-letter state code of residence.
     *
     * @param newStateOfResidence the state code
     */
    public void setStateOfResidence(final String newStateOfResidence) {
        this.stateOfResidence = newStateOfResidence;
    }

    /**
     * Returns the creation timestamp.
     *
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the last update timestamp.
     *
     * @return the update timestamp
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        var person = (Person) obj;
        return Objects.equals(id, person.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public final String toString() {
        return "Person{"
                + "id=" + id
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", dateOfBirth=" + dateOfBirth
                + ", filingStatus=" + filingStatus
                + ", stateOfResidence='" + stateOfResidence + '\''
                + '}';
    }
}
