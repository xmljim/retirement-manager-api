package com.xmljim.retirement.domain.contribution;

/**
 * Tax treatment categories for retirement accounts.
 *
 * <p>Defines how contributions and growth are taxed for different
 * account types.</p>
 *
 * @since 1.0
 */
public enum TaxTreatment {

    /** Contributions are pre-tax; withdrawals are taxed as income. */
    TAX_DEFERRED("Tax-Deferred"),

    /** Contributions are after-tax; qualified withdrawals are tax-free. */
    TAX_FREE("Tax-Free"),

    /** No special tax treatment; subject to capital gains tax. */
    TAXABLE("Taxable");

    /** Human-readable display name. */
    private final String displayName;

    /**
     * Constructs a tax treatment with the given display name.
     *
     * @param name the human-readable name
     */
    TaxTreatment(final String name) {
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
