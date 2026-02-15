package com.xmljim.retirement.domain.marriage;

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
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a marriage record between two persons.
 *
 * <p>Marriage records track the history of marriages for retirement planning
 * purposes, particularly for determining Social Security spousal benefit
 * eligibility. SS divorced spouse benefits require marriages that lasted
 * 10 years or more.</p>
 *
 * <p>This class is not designed for extension outside the JPA framework.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "marriages")
public class Marriage {

    /** Maximum length for status field. */
    public static final int MAX_STATUS_LENGTH = 20;

    /** Minimum years of marriage required for SS divorced spouse benefits. */
    public static final int SS_SPOUSAL_BENEFIT_MIN_YEARS = 10;

    /** Unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** First person in the marriage. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person1_id", nullable = false)
    private Person person1;

    /** Second person in the marriage. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person2_id", nullable = false)
    private Person person2;

    /** Date of marriage. */
    @Column(name = "marriage_date", nullable = false)
    private LocalDate marriageDate;

    /** Date of divorce (null if still married or widowed). */
    @Column(name = "divorce_date")
    private LocalDate divorceDate;

    /** Current status of the marriage. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = MAX_STATUS_LENGTH)
    private MarriageStatus status;

    /** Optional notes about the marriage. */
    @Column(name = "notes")
    private String notes;

    /** Timestamp when the record was created. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /** Timestamp when the record was last updated. */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Default constructor for JPA.
     */
    protected Marriage() {
        // JPA requires default constructor
    }

    /**
     * Constructs a new Marriage with the required fields.
     *
     * @param newPerson1      the first person in the marriage
     * @param newPerson2      the second person in the marriage
     * @param newMarriageDate the date of marriage
     * @param newStatus       the status of the marriage
     * @param newNotes        optional notes (may be null)
     */
    public Marriage(final Person newPerson1,
                    final Person newPerson2,
                    final LocalDate newMarriageDate,
                    final MarriageStatus newStatus,
                    final String newNotes) {
        this.person1 = newPerson1;
        this.person2 = newPerson2;
        this.marriageDate = newMarriageDate;
        this.status = newStatus;
        this.notes = newNotes;
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
     * Returns the first person in the marriage.
     *
     * @return the first person
     */
    public Person getPerson1() {
        return person1;
    }

    /**
     * Sets the first person in the marriage.
     *
     * @param newPerson1 the first person
     */
    public void setPerson1(final Person newPerson1) {
        this.person1 = newPerson1;
    }

    /**
     * Returns the second person in the marriage.
     *
     * @return the second person
     */
    public Person getPerson2() {
        return person2;
    }

    /**
     * Sets the second person in the marriage.
     *
     * @param newPerson2 the second person
     */
    public void setPerson2(final Person newPerson2) {
        this.person2 = newPerson2;
    }

    /**
     * Returns the date of marriage.
     *
     * @return the marriage date
     */
    public LocalDate getMarriageDate() {
        return marriageDate;
    }

    /**
     * Sets the date of marriage.
     *
     * @param newMarriageDate the marriage date
     */
    public void setMarriageDate(final LocalDate newMarriageDate) {
        this.marriageDate = newMarriageDate;
    }

    /**
     * Returns the date of divorce.
     *
     * @return the divorce date, or null if still married or widowed
     */
    public LocalDate getDivorceDate() {
        return divorceDate;
    }

    /**
     * Sets the date of divorce.
     *
     * @param newDivorceDate the divorce date
     */
    public void setDivorceDate(final LocalDate newDivorceDate) {
        this.divorceDate = newDivorceDate;
    }

    /**
     * Returns the status of the marriage.
     *
     * @return the marriage status
     */
    public MarriageStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the marriage.
     *
     * @param newStatus the marriage status
     */
    public void setStatus(final MarriageStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Returns the notes about the marriage.
     *
     * @return the notes, or null if not set
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes about the marriage.
     *
     * @param newNotes the notes
     */
    public void setNotes(final String newNotes) {
        this.notes = newNotes;
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

    /**
     * Calculates the duration of the marriage in years.
     *
     * <p>For ongoing marriages, calculates to the current date.
     * For divorced marriages, calculates to the divorce date.
     * For widowed marriages, calculates to the current date (actual end
     * date is tracked elsewhere if needed).</p>
     *
     * @return the number of full years the marriage has lasted
     */
    public long getMarriageDurationYears() {
        var endDate = (divorceDate != null) ? divorceDate : LocalDate.now();
        return ChronoUnit.YEARS.between(marriageDate, endDate);
    }

    /**
     * Checks if this marriage qualifies for Social Security spousal benefits.
     *
     * <p>SS divorced spouse benefits require the marriage to have lasted
     * at least 10 years. This method returns true if the marriage duration
     * meets or exceeds that requirement.</p>
     *
     * @return true if the marriage lasted 10 or more years
     */
    public boolean isEligibleForSpousalBenefits() {
        return getMarriageDurationYears() >= SS_SPOUSAL_BENEFIT_MIN_YEARS;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        var marriage = (Marriage) obj;
        return Objects.equals(id, marriage.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public final String toString() {
        return "Marriage{"
                + "id=" + id
                + ", person1=" + (person1 != null ? person1.getId() : null)
                + ", person2=" + (person2 != null ? person2.getId() : null)
                + ", marriageDate=" + marriageDate
                + ", divorceDate=" + divorceDate
                + ", status=" + status
                + '}';
    }
}
