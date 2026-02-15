package com.xmljim.retirement.api.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested resource is not found.
 *
 * <p>This exception is mapped to HTTP 404 Not Found by the global exception handler.</p>
 *
 * @since 1.0
 */
public class ResourceNotFoundException extends RuntimeException {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The type of resource that was not found. */
    private final String resourceType;

    /** The identifier of the resource that was not found. */
    private final UUID resourceId;

    /**
     * Constructs a new ResourceNotFoundException.
     *
     * @param type the type of resource (e.g., "Person")
     * @param id   the ID of the resource that was not found
     */
    public ResourceNotFoundException(final String type, final UUID id) {
        super(type + " not found with id: " + id);
        this.resourceType = type;
        this.resourceId = id;
    }

    /**
     * Returns the type of resource that was not found.
     *
     * @return the resource type
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * Returns the ID of the resource that was not found.
     *
     * @return the resource ID
     */
    public UUID getResourceId() {
        return resourceId;
    }
}
