package com.xmljim.retirement.domain.person;

/**
 * Tax filing status for a person.
 *
 * <p>This enum represents the IRS filing status options that affect
 * tax calculations, contribution limits, and phase-out ranges.</p>
 *
 * @since 1.0
 */
public enum FilingStatus {

    /** Single filer (unmarried). */
    SINGLE("Single"),

    /** Married filing jointly with spouse. */
    MARRIED_FILING_JOINTLY("Married Filing Jointly"),

    /** Married filing separately from spouse. */
    MARRIED_FILING_SEPARATELY("Married Filing Separately"),

    /** Head of household (unmarried with qualifying dependent). */
    HEAD_OF_HOUSEHOLD("Head of Household");

    /** Human-readable display name. */
    private final String displayName;

    /**
     * Constructs a filing status with the given display name.
     *
     * @param name the human-readable name
     */
    FilingStatus(final String name) {
        this.displayName = name;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
