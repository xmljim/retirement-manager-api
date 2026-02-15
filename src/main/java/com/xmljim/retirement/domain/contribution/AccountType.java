package com.xmljim.retirement.domain.contribution;

/**
 * Types of retirement and savings accounts.
 *
 * <p>This enum represents the various account types supported by the
 * retirement planning system, each with different tax treatments,
 * contribution limits, and eligibility rules.</p>
 *
 * @since 1.0
 */
public enum AccountType {

    /** Traditional 401(k) - Tax-deferred workplace retirement plan. */
    TRADITIONAL_401K("Traditional 401(k)", TaxTreatment.TAX_DEFERRED, true),

    /** Roth 401(k) - After-tax workplace retirement plan with tax-free growth. */
    ROTH_401K("Roth 401(k)", TaxTreatment.TAX_FREE, true),

    /** Traditional IRA - Tax-deferred individual retirement account. */
    TRADITIONAL_IRA("Traditional IRA", TaxTreatment.TAX_DEFERRED, false),

    /** Roth IRA - After-tax IRA with tax-free growth and income limits. */
    ROTH_IRA("Roth IRA", TaxTreatment.TAX_FREE, false),

    /** SEP IRA - Simplified Employee Pension for self-employed. */
    SEP_IRA("SEP IRA", TaxTreatment.TAX_DEFERRED, false),

    /** SIMPLE IRA - Savings Incentive Match Plan for small businesses. */
    SIMPLE_IRA("SIMPLE IRA", TaxTreatment.TAX_DEFERRED, true),

    /** HSA Self-only - Health Savings Account for individual coverage. */
    HSA_SELF("HSA (Self-only)", TaxTreatment.TAX_FREE, false),

    /** HSA Family - Health Savings Account for family coverage. */
    HSA_FAMILY("HSA (Family)", TaxTreatment.TAX_FREE, false),

    /** 403(b) - Tax-deferred plan for non-profit employees. */
    ACCOUNT_403B("403(b)", TaxTreatment.TAX_DEFERRED, true),

    /** 457(b) - Deferred compensation plan for government employees. */
    ACCOUNT_457B("457(b)", TaxTreatment.TAX_DEFERRED, true);

    /** Human-readable display name. */
    private final String displayName;

    /** Tax treatment for contributions and growth. */
    private final TaxTreatment taxTreatment;

    /** Whether this is an employer-sponsored plan. */
    private final boolean employerSponsored;

    /**
     * Constructs an account type with the given attributes.
     *
     * @param name          the human-readable display name
     * @param treatment     the tax treatment
     * @param isEmployer    whether employer-sponsored
     */
    AccountType(final String name, final TaxTreatment treatment, final boolean isEmployer) {
        this.displayName = name;
        this.taxTreatment = treatment;
        this.employerSponsored = isEmployer;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the tax treatment for this account type.
     *
     * @return the tax treatment
     */
    public TaxTreatment getTaxTreatment() {
        return taxTreatment;
    }

    /**
     * Returns whether this is an employer-sponsored plan.
     *
     * @return true if employer-sponsored
     */
    public boolean isEmployerSponsored() {
        return employerSponsored;
    }

    /**
     * Converts a database value to an AccountType.
     *
     * <p>This method handles the mapping of database column values
     * (like '403B' and '457B') to their corresponding enum constants.</p>
     *
     * @param dbValue the database column value
     * @return the corresponding AccountType
     * @throws IllegalArgumentException if no matching type exists
     */
    public static AccountType fromDatabaseValue(final String dbValue) {
        return switch (dbValue) {
            case "403B" -> ACCOUNT_403B;
            case "457B" -> ACCOUNT_457B;
            default -> valueOf(dbValue);
        };
    }

    /**
     * Returns the database column value for this account type.
     *
     * <p>This method handles the mapping of enum constants to their
     * database column representation.</p>
     *
     * @return the database column value
     */
    public String toDatabaseValue() {
        return switch (this) {
            case ACCOUNT_403B -> "403B";
            case ACCOUNT_457B -> "457B";
            default -> name();
        };
    }
}
