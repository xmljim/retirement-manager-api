package com.xmljim.retirement.domain.contribution;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing IRS contribution limits by year and account type.
 *
 * <p>Contribution limits are set annually by the IRS and vary by account type.
 * Different limit types distinguish between base limits, catch-up provisions,
 * and total limits including employer contributions.</p>
 *
 * <p>This class is not designed for extension outside the JPA framework.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "contribution_limits")
public class ContributionLimit {

    /** Maximum length for account type field. */
    public static final int MAX_ACCOUNT_TYPE_LENGTH = 50;

    /** Maximum length for limit type field. */
    public static final int MAX_LIMIT_TYPE_LENGTH = 50;

    /** Precision for monetary amounts (total digits). */
    public static final int MONEY_PRECISION = 12;

    /** Scale for monetary amounts (decimal places). */
    public static final int MONEY_SCALE = 2;

    /** Unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** The tax year for these limits. */
    @Column(name = "year", nullable = false)
    private Integer year;

    /** The type of account these limits apply to. */
    @Convert(converter = AccountTypeConverter.class)
    @Column(name = "account_type", nullable = false, length = MAX_ACCOUNT_TYPE_LENGTH)
    private AccountType accountType;

    /** The type of limit (base, catch-up, total, etc.). */
    @Enumerated(EnumType.STRING)
    @Column(name = "limit_type", nullable = false, length = MAX_LIMIT_TYPE_LENGTH)
    private LimitType limitType;

    /** The contribution limit amount in dollars. */
    @Column(name = "amount", nullable = false, precision = MONEY_PRECISION, scale = MONEY_SCALE)
    private BigDecimal amount;

    /** Timestamp when the record was created. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Default constructor for JPA.
     */
    protected ContributionLimit() {
        // JPA requires default constructor
    }

    /**
     * Constructs a new ContributionLimit with the required fields.
     *
     * @param limitYear       the tax year
     * @param type            the account type
     * @param typeOfLimit     the limit type
     * @param limitAmount     the contribution limit amount
     */
    public ContributionLimit(final Integer limitYear,
                             final AccountType type,
                             final LimitType typeOfLimit,
                             final BigDecimal limitAmount) {
        this.year = limitYear;
        this.accountType = type;
        this.limitType = typeOfLimit;
        this.amount = limitAmount;
    }

    /**
     * Sets the creation timestamp before persisting a new entity.
     */
    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
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
     * Returns the tax year for these limits.
     *
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the tax year.
     *
     * @param limitYear the year
     */
    public void setYear(final Integer limitYear) {
        this.year = limitYear;
    }

    /**
     * Returns the account type these limits apply to.
     *
     * @return the account type
     */
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     * Sets the account type.
     *
     * @param type the account type
     */
    public void setAccountType(final AccountType type) {
        this.accountType = type;
    }

    /**
     * Returns the type of limit.
     *
     * @return the limit type
     */
    public LimitType getLimitType() {
        return limitType;
    }

    /**
     * Sets the limit type.
     *
     * @param typeOfLimit the limit type
     */
    public void setLimitType(final LimitType typeOfLimit) {
        this.limitType = typeOfLimit;
    }

    /**
     * Returns the contribution limit amount.
     *
     * @return the amount in dollars
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the contribution limit amount.
     *
     * @param limitAmount the amount in dollars
     */
    public void setAmount(final BigDecimal limitAmount) {
        this.amount = limitAmount;
    }

    /**
     * Returns the creation timestamp.
     *
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        var that = (ContributionLimit) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public final String toString() {
        return "ContributionLimit{"
                + "id=" + id
                + ", year=" + year
                + ", accountType=" + accountType
                + ", limitType=" + limitType
                + ", amount=" + amount
                + '}';
    }
}
