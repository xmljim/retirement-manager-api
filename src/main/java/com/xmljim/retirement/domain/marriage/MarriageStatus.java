package com.xmljim.retirement.domain.marriage;

/**
 * Status of a marriage record.
 *
 * <p>This enum represents the current state of a marriage relationship,
 * used for tracking marriage history and determining Social Security
 * spousal benefit eligibility.</p>
 *
 * @since 1.0
 */
public enum MarriageStatus {

    /** Currently married. */
    MARRIED("Married"),

    /** Divorced (marriage ended by legal decree). */
    DIVORCED("Divorced"),

    /** Widowed (marriage ended by death of spouse). */
    WIDOWED("Widowed");

    /** Human-readable display name. */
    private final String displayName;

    /**
     * Constructs a marriage status with the given display name.
     *
     * @param name the human-readable name
     */
    MarriageStatus(final String name) {
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
