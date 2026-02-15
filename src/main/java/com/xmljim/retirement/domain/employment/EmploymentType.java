package com.xmljim.retirement.domain.employment;

/**
 * Type of employment relationship.
 *
 * <p>This enum represents the different types of employment arrangements
 * that affect retirement plan eligibility and contribution options.</p>
 *
 * @since 1.0
 */
public enum EmploymentType {

    /** Full-time employee (typically 30+ hours/week). */
    FULL_TIME("Full-Time"),

    /** Part-time employee (typically less than 30 hours/week). */
    PART_TIME("Part-Time"),

    /** Independent contractor or 1099 worker. */
    CONTRACT("Contract"),

    /** Self-employed or business owner. */
    SELF_EMPLOYED("Self-Employed");

    /** Human-readable display name. */
    private final String displayName;

    /**
     * Constructs an employment type with the given display name.
     *
     * @param name the human-readable name
     */
    EmploymentType(final String name) {
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
