package com.xmljim.retirement.domain.income;

import com.xmljim.retirement.domain.employment.Employment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing annual income for an employment.
 *
 * <p>An income record tracks annual compensation for a specific employment
 * and year. W-2 wages are tracked separately to support SECURE 2.0
 * high earner catch-up contribution rules.</p>
 *
 * <p>This class is not designed for extension outside the JPA framework.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "income")
public class Income {

    /** Precision for decimal currency fields. */
    public static final int CURRENCY_PRECISION = 12;

    /** Scale for decimal currency fields. */
    public static final int CURRENCY_SCALE = 2;

    /** Unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** The employment this income is associated with. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employment_id", nullable = false)
    private Employment employment;

    /** The tax year for this income. */
    @Column(name = "year", nullable = false)
    private int year;

    /** Annual base salary. */
    @Column(name = "annual_salary", nullable = false, precision = CURRENCY_PRECISION, scale = CURRENCY_SCALE)
    private BigDecimal annualSalary;

    /** Annual bonus compensation. */
    @Column(name = "bonus", nullable = false, precision = CURRENCY_PRECISION, scale = CURRENCY_SCALE)
    private BigDecimal bonus;

    /** Other compensation (stock, commissions, etc.). */
    @Column(name = "other_compensation", nullable = false, precision = CURRENCY_PRECISION, scale = CURRENCY_SCALE)
    private BigDecimal otherCompensation;

    /** W-2 wages for SECURE 2.0 high earner calculations. */
    @Column(name = "w2_wages", precision = CURRENCY_PRECISION, scale = CURRENCY_SCALE)
    private BigDecimal w2Wages;

    /** Timestamp when the record was created. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /** Timestamp when the record was last updated. */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Default constructor for JPA.
     */
    protected Income() {
        // JPA requires default constructor
    }

    /**
     * Constructs a new Income with the required fields.
     *
     * @param newEmployment   the associated employment
     * @param newYear         the tax year
     * @param newAnnualSalary the annual base salary
     */
    public Income(final Employment newEmployment,
                  final int newYear,
                  final BigDecimal newAnnualSalary) {
        this.employment = newEmployment;
        this.year = newYear;
        this.annualSalary = newAnnualSalary;
        this.bonus = BigDecimal.ZERO;
        this.otherCompensation = BigDecimal.ZERO;
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
     * Returns the associated employment.
     *
     * @return the employment
     */
    public Employment getEmployment() {
        return employment;
    }

    /**
     * Sets the associated employment.
     *
     * @param newEmployment the employment
     */
    public void setEmployment(final Employment newEmployment) {
        this.employment = newEmployment;
    }

    /**
     * Returns the tax year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the tax year.
     *
     * @param newYear the year
     */
    public void setYear(final int newYear) {
        this.year = newYear;
    }

    /**
     * Returns the annual base salary.
     *
     * @return the annual salary
     */
    public BigDecimal getAnnualSalary() {
        return annualSalary;
    }

    /**
     * Sets the annual base salary.
     *
     * @param newAnnualSalary the annual salary
     */
    public void setAnnualSalary(final BigDecimal newAnnualSalary) {
        this.annualSalary = newAnnualSalary;
    }

    /**
     * Returns the annual bonus compensation.
     *
     * @return the bonus
     */
    public BigDecimal getBonus() {
        return bonus;
    }

    /**
     * Sets the annual bonus compensation.
     *
     * @param newBonus the bonus
     */
    public void setBonus(final BigDecimal newBonus) {
        this.bonus = newBonus;
    }

    /**
     * Returns other compensation (stock, commissions, etc.).
     *
     * @return other compensation
     */
    public BigDecimal getOtherCompensation() {
        return otherCompensation;
    }

    /**
     * Sets other compensation.
     *
     * @param newOtherCompensation other compensation
     */
    public void setOtherCompensation(final BigDecimal newOtherCompensation) {
        this.otherCompensation = newOtherCompensation;
    }

    /**
     * Returns W-2 wages for SECURE 2.0 calculations.
     *
     * @return W-2 wages, or null if not set
     */
    public BigDecimal getW2Wages() {
        return w2Wages;
    }

    /**
     * Sets W-2 wages for SECURE 2.0 calculations.
     *
     * @param newW2Wages the W-2 wages
     */
    public void setW2Wages(final BigDecimal newW2Wages) {
        this.w2Wages = newW2Wages;
    }

    /**
     * Calculates total compensation (salary + bonus + other).
     *
     * @return total compensation
     */
    public BigDecimal getTotalCompensation() {
        return annualSalary.add(bonus).add(otherCompensation);
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
        var income = (Income) obj;
        return Objects.equals(id, income.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public final String toString() {
        return "Income{"
                + "id=" + id
                + ", year=" + year
                + ", annualSalary=" + annualSalary
                + ", bonus=" + bonus
                + ", otherCompensation=" + otherCompensation
                + ", w2Wages=" + w2Wages
                + '}';
    }
}
