package com.xmljim.retirement.domain.contribution;

/**
 * Types of contribution limits defined by the IRS.
 *
 * <p>Different limit types apply based on age and plan characteristics.
 * Catch-up contributions allow older workers to contribute additional
 * amounts beyond the base limit.</p>
 *
 * @since 1.0
 */
public enum LimitType {

    /** Base contribution limit available to all eligible participants. */
    BASE("Base Limit", null, null),

    /** Catch-up contribution for participants age 50 and older. */
    CATCHUP_50("Catch-up (50+)", 50, null),

    /** Catch-up contribution for HSA participants age 55 and older. */
    CATCHUP_55("Catch-up (55+)", 55, null),

    /** Enhanced catch-up for participants aged 60-63 under SECURE 2.0. */
    CATCHUP_60_63("Super Catch-up (60-63)", 60, 63),

    /** Maximum total including employer contributions under IRC 415(c). */
    EMPLOYER_TOTAL("Total 415(c) Limit", null, null),

    /** Maximum compensation considered for contribution calculations. */
    COMPENSATION_LIMIT("Compensation Limit", null, null);

    /** Human-readable display name. */
    private final String displayName;

    /** Minimum age for eligibility, or null if no minimum. */
    private final Integer minimumAge;

    /** Maximum age for eligibility, or null if no maximum. */
    private final Integer maximumAge;

    /**
     * Constructs a limit type with the given attributes.
     *
     * @param name   the human-readable display name
     * @param minAge minimum age for eligibility
     * @param maxAge maximum age for eligibility
     */
    LimitType(final String name, final Integer minAge, final Integer maxAge) {
        this.displayName = name;
        this.minimumAge = minAge;
        this.maximumAge = maxAge;
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
     * Returns the minimum age for eligibility.
     *
     * @return the minimum age, or null if no minimum applies
     */
    public Integer getMinimumAge() {
        return minimumAge;
    }

    /**
     * Returns the maximum age for eligibility.
     *
     * @return the maximum age, or null if no maximum applies
     */
    public Integer getMaximumAge() {
        return maximumAge;
    }

    /**
     * Checks if this limit type requires a minimum age for eligibility.
     *
     * @return true if an age requirement exists
     */
    public boolean hasAgeRequirement() {
        return minimumAge != null;
    }

    /**
     * Checks if the given age is eligible for this limit type.
     *
     * @param age the age to check (as of December 31 of the contribution year)
     * @return true if the age meets the requirements for this limit type
     */
    public boolean isEligible(final int age) {
        if (minimumAge == null) {
            return true;
        }
        if (maximumAge == null) {
            return age >= minimumAge;
        }
        return age >= minimumAge && age <= maximumAge;
    }
}
