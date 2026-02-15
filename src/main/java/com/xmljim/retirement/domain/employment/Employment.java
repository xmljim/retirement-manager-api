package com.xmljim.retirement.domain.employment;

import com.xmljim.retirement.domain.employer.Employer;
import com.xmljim.retirement.domain.person.Person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing an employment record.
 *
 * <p>An employment record links a person to an employer for a specific
 * time period. It tracks job details and retirement plan eligibility
 * that affect contribution calculations.</p>
 *
 * <p>This class is not designed for extension outside the JPA framework.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "employment")
public class Employment {

    /** Maximum length for job title field. */
    public static final int MAX_JOB_TITLE_LENGTH = 200;

    /** Maximum length for employment type field. */
    public static final int MAX_EMPLOYMENT_TYPE_LENGTH = 30;

    /** Unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** The person who is employed. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    /** The employer. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    /** Job title. */
    @Column(name = "job_title", length = MAX_JOB_TITLE_LENGTH)
    private String jobTitle;

    /** Employment start date. */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Employment end date (null if currently employed). */
    @Column(name = "end_date")
    private LocalDate endDate;

    /** Type of employment. */
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = MAX_EMPLOYMENT_TYPE_LENGTH)
    private EmploymentType employmentType;

    /** Whether person is eligible for employer's retirement plan. */
    @Column(name = "is_retirement_plan_eligible", nullable = false)
    private boolean retirementPlanEligible;

    /** Timestamp when the record was created. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /** Timestamp when the record was last updated. */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Default constructor for JPA.
     */
    protected Employment() {
        // JPA requires default constructor
    }

    /**
     * Constructs a new Employment with the required fields.
     *
     * @param newPerson         the person being employed
     * @param newEmployer       the employer
     * @param newStartDate      the employment start date
     * @param newEmploymentType the type of employment
     */
    public Employment(final Person newPerson,
                      final Employer newEmployer,
                      final LocalDate newStartDate,
                      final EmploymentType newEmploymentType) {
        this.person = newPerson;
        this.employer = newEmployer;
        this.startDate = newStartDate;
        this.employmentType = newEmploymentType;
        this.retirementPlanEligible = true;
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
     * Returns the person who is employed.
     *
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets the person who is employed.
     *
     * @param newPerson the person
     */
    public void setPerson(final Person newPerson) {
        this.person = newPerson;
    }

    /**
     * Returns the employer.
     *
     * @return the employer
     */
    public Employer getEmployer() {
        return employer;
    }

    /**
     * Sets the employer.
     *
     * @param newEmployer the employer
     */
    public void setEmployer(final Employer newEmployer) {
        this.employer = newEmployer;
    }

    /**
     * Returns the job title.
     *
     * @return the job title, or null if not set
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the job title.
     *
     * @param newJobTitle the job title
     */
    public void setJobTitle(final String newJobTitle) {
        this.jobTitle = newJobTitle;
    }

    /**
     * Returns the employment start date.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the employment start date.
     *
     * @param newStartDate the start date
     */
    public void setStartDate(final LocalDate newStartDate) {
        this.startDate = newStartDate;
    }

    /**
     * Returns the employment end date.
     *
     * @return the end date, or null if currently employed
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the employment end date.
     *
     * @param newEndDate the end date
     */
    public void setEndDate(final LocalDate newEndDate) {
        this.endDate = newEndDate;
    }

    /**
     * Returns the type of employment.
     *
     * @return the employment type
     */
    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    /**
     * Sets the type of employment.
     *
     * @param newEmploymentType the employment type
     */
    public void setEmploymentType(final EmploymentType newEmploymentType) {
        this.employmentType = newEmploymentType;
    }

    /**
     * Returns whether the person is eligible for the employer's retirement plan.
     *
     * @return true if retirement plan eligible
     */
    public boolean isRetirementPlanEligible() {
        return retirementPlanEligible;
    }

    /**
     * Sets whether the person is eligible for the employer's retirement plan.
     *
     * @param eligible true if retirement plan eligible
     */
    public void setRetirementPlanEligible(final boolean eligible) {
        this.retirementPlanEligible = eligible;
    }

    /**
     * Returns whether this is current employment (no end date).
     *
     * @return true if currently employed
     */
    public boolean isCurrent() {
        return endDate == null;
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
        var employment = (Employment) obj;
        return Objects.equals(id, employment.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public final String toString() {
        return "Employment{"
                + "id=" + id
                + ", jobTitle='" + jobTitle + '\''
                + ", startDate=" + startDate
                + ", endDate=" + endDate
                + ", employmentType=" + employmentType
                + ", retirementPlanEligible=" + retirementPlanEligible
                + '}';
    }
}
