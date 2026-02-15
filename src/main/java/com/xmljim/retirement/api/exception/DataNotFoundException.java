package com.xmljim.retirement.api.exception;

/**
 * Exception thrown when requested data is not found.
 *
 * <p>This exception is used for non-UUID based resource lookups,
 * such as retrieving contribution limits by year. It is mapped to
 * HTTP 404 Not Found by the global exception handler.</p>
 *
 * @since 1.0
 */
public class DataNotFoundException extends RuntimeException {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The type of data that was not found. */
    private final String dataType;

    /** A description of what was being searched for. */
    private final String criteria;

    /**
     * Constructs a new DataNotFoundException.
     *
     * @param type        the type of data (e.g., "Contribution limits")
     * @param searchCriteria description of the search criteria (e.g., "year: 2025")
     */
    public DataNotFoundException(final String type, final String searchCriteria) {
        super(type + " not found for " + searchCriteria);
        this.dataType = type;
        this.criteria = searchCriteria;
    }

    /**
     * Returns the type of data that was not found.
     *
     * @return the data type
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Returns the search criteria description.
     *
     * @return the search criteria
     */
    public String getCriteria() {
        return criteria;
    }
}
