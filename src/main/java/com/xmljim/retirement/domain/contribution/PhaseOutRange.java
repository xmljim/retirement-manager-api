package com.xmljim.retirement.domain.contribution;

import com.xmljim.retirement.domain.person.FilingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing MAGI phase-out ranges for IRA contributions.
 *
 * <p>Phase-out ranges define the income thresholds at which contribution
 * limits or tax deductions begin to reduce and fully phase out. The ranges
 * vary by filing status and account type.</p>
 *
 * <p>This class is not designed for extension outside the JPA framework.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "phase_out_ranges")
public class PhaseOutRange {

    /** Maximum length for filing status field. */
    public static final int MAX_FILING_STATUS_LENGTH = 50;

    /** Maximum length for account type field. */
    public static final int MAX_ACCOUNT_TYPE_LENGTH = 50;

    /** Precision for monetary amounts (total digits). */
    public static final int MONEY_PRECISION = 12;

    /** Scale for monetary amounts (decimal places). */
    public static final int MONEY_SCALE = 2;

    /** Scale for percentage calculations. */
    public static final int PERCENTAGE_SCALE = 4;

    /** Unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** The tax year for these phase-out ranges. */
    @Column(name = "year", nullable = false)
    private Integer year;

    /** The tax filing status. */
    @Enumerated(EnumType.STRING)
    @Column(name = "filing_status", nullable = false, length = MAX_FILING_STATUS_LENGTH)
    private FilingStatus filingStatus;

    /** The type of account these phase-outs apply to. */
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = MAX_ACCOUNT_TYPE_LENGTH)
    private PhaseOutAccountType accountType;

    /** MAGI amount where phase-out begins. */
    @Column(name = "magi_start", nullable = false, precision = MONEY_PRECISION, scale = MONEY_SCALE)
    private BigDecimal magiStart;

    /** MAGI amount where phase-out is complete (full phase-out). */
    @Column(name = "magi_end", nullable = false, precision = MONEY_PRECISION, scale = MONEY_SCALE)
    private BigDecimal magiEnd;

    /** Timestamp when the record was created. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Default constructor for JPA.
     */
    protected PhaseOutRange() {
        // JPA requires default constructor
    }

    /**
     * Constructs a new PhaseOutRange with the required fields.
     *
     * @param rangeYear       the tax year
     * @param status          the filing status
     * @param type            the account type
     * @param startMagi       the MAGI where phase-out begins
     * @param endMagi         the MAGI where phase-out is complete
     */
    public PhaseOutRange(final Integer rangeYear,
                         final FilingStatus status,
                         final PhaseOutAccountType type,
                         final BigDecimal startMagi,
                         final BigDecimal endMagi) {
        this.year = rangeYear;
        this.filingStatus = status;
        this.accountType = type;
        this.magiStart = startMagi;
        this.magiEnd = endMagi;
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
     * Returns the tax year.
     *
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the tax year.
     *
     * @param rangeYear the year
     */
    public void setYear(final Integer rangeYear) {
        this.year = rangeYear;
    }

    /**
     * Returns the filing status.
     *
     * @return the filing status
     */
    public FilingStatus getFilingStatus() {
        return filingStatus;
    }

    /**
     * Sets the filing status.
     *
     * @param status the filing status
     */
    public void setFilingStatus(final FilingStatus status) {
        this.filingStatus = status;
    }

    /**
     * Returns the account type.
     *
     * @return the account type
     */
    public PhaseOutAccountType getAccountType() {
        return accountType;
    }

    /**
     * Sets the account type.
     *
     * @param type the account type
     */
    public void setAccountType(final PhaseOutAccountType type) {
        this.accountType = type;
    }

    /**
     * Returns the MAGI where phase-out begins.
     *
     * @return the starting MAGI threshold
     */
    public BigDecimal getMagiStart() {
        return magiStart;
    }

    /**
     * Sets the MAGI where phase-out begins.
     *
     * @param startMagi the starting MAGI threshold
     */
    public void setMagiStart(final BigDecimal startMagi) {
        this.magiStart = startMagi;
    }

    /**
     * Returns the MAGI where phase-out is complete.
     *
     * @return the ending MAGI threshold
     */
    public BigDecimal getMagiEnd() {
        return magiEnd;
    }

    /**
     * Sets the MAGI where phase-out is complete.
     *
     * @param endMagi the ending MAGI threshold
     */
    public void setMagiEnd(final BigDecimal endMagi) {
        this.magiEnd = endMagi;
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
     * Calculates the phase-out percentage for a given MAGI.
     *
     * <p>Returns a value between 0 and 1 representing the portion of the
     * contribution limit that has been phased out:</p>
     * <ul>
     *   <li>0.0 = no phase-out (full contribution allowed)</li>
     *   <li>1.0 = fully phased out (no contribution allowed)</li>
     * </ul>
     *
     * @param magi the modified adjusted gross income
     * @return the phase-out percentage (0.0 to 1.0)
     */
    public BigDecimal calculatePhaseOutPercentage(final BigDecimal magi) {
        if (magi.compareTo(magiStart) <= 0) {
            return BigDecimal.ZERO;
        }
        if (magi.compareTo(magiEnd) >= 0) {
            return BigDecimal.ONE;
        }
        var range = magiEnd.subtract(magiStart);
        var amountOver = magi.subtract(magiStart);
        return amountOver.divide(range, PERCENTAGE_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the reduced contribution limit based on MAGI.
     *
     * @param baseLimit the full contribution limit before phase-out
     * @param magi      the modified adjusted gross income
     * @return the reduced contribution limit
     */
    public BigDecimal calculateReducedLimit(final BigDecimal baseLimit, final BigDecimal magi) {
        var phaseOutPct = calculatePhaseOutPercentage(magi);
        var reductionAmount = baseLimit.multiply(phaseOutPct);
        return baseLimit.subtract(reductionAmount).setScale(0, RoundingMode.UP);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        var that = (PhaseOutRange) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public final String toString() {
        return "PhaseOutRange{"
                + "id=" + id
                + ", year=" + year
                + ", filingStatus=" + filingStatus
                + ", accountType=" + accountType
                + ", magiStart=" + magiStart
                + ", magiEnd=" + magiEnd
                + '}';
    }
}
