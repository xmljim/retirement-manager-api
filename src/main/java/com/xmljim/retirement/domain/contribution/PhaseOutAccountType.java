package com.xmljim.retirement.domain.contribution;

/**
 * Account types that have income-based phase-out ranges.
 *
 * <p>Only certain account types have contribution limits or deduction
 * eligibility that phases out based on modified adjusted gross income (MAGI).
 * This enum represents those specific account types and scenarios.</p>
 *
 * @since 1.0
 */
public enum PhaseOutAccountType {

    /** Roth IRA contribution phase-out based on MAGI. */
    ROTH_IRA("Roth IRA", "Contribution phase-out based on MAGI"),

    /** Traditional IRA deduction phase-out when contributor is covered by workplace plan. */
    TRADITIONAL_IRA("Traditional IRA", "Deduction phase-out when covered by workplace plan"),

    /** Traditional IRA deduction phase-out when spouse is covered by workplace plan. */
    TRADITIONAL_IRA_SPOUSE_COVERED("Traditional IRA (Spouse Covered)",
            "Deduction phase-out when spouse is covered by workplace plan");

    /** Human-readable display name. */
    private final String displayName;

    /** Description of what this phase-out applies to. */
    private final String description;

    /**
     * Constructs a phase-out account type with the given attributes.
     *
     * @param name the human-readable display name
     * @param desc description of the phase-out scenario
     */
    PhaseOutAccountType(final String name, final String desc) {
        this.displayName = name;
        this.description = desc;
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
     * Returns the description of this phase-out scenario.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
