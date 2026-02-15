package com.xmljim.retirement.domain.employer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing an employer (company or organization).
 *
 * <p>An employer is an organization where a person works. Employers are
 * referenced by employment records and may offer retirement benefits
 * like 401(k) matching.</p>
 *
 * <p>This class is not designed for extension outside the JPA framework.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "employers")
public class Employer {

    /** Maximum length for name field. */
    public static final int MAX_NAME_LENGTH = 200;

    /** Maximum length for EIN field (XX-XXXXXXX format). */
    public static final int MAX_EIN_LENGTH = 10;

    /** Maximum length for address fields. */
    public static final int MAX_ADDRESS_LENGTH = 200;

    /** Maximum length for city field. */
    public static final int MAX_CITY_LENGTH = 100;

    /** Length for state code. */
    public static final int STATE_CODE_LENGTH = 2;

    /** Maximum length for zip code field. */
    public static final int MAX_ZIP_LENGTH = 10;

    /** Unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Employer name. */
    @Column(name = "name", nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    /** Employer Identification Number (EIN) in XX-XXXXXXX format. */
    @Column(name = "ein", length = MAX_EIN_LENGTH)
    private String ein;

    /** Address line 1. */
    @Column(name = "address_line1", length = MAX_ADDRESS_LENGTH)
    private String addressLine1;

    /** Address line 2. */
    @Column(name = "address_line2", length = MAX_ADDRESS_LENGTH)
    private String addressLine2;

    /** City. */
    @Column(name = "city", length = MAX_CITY_LENGTH)
    private String city;

    /** Two-letter US state code. */
    @Column(name = "state", length = STATE_CODE_LENGTH)
    private String state;

    /** ZIP code. */
    @Column(name = "zip_code", length = MAX_ZIP_LENGTH)
    private String zipCode;

    /** Timestamp when the record was created. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /** Timestamp when the record was last updated. */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Default constructor for JPA.
     */
    protected Employer() {
        // JPA requires default constructor
    }

    /**
     * Constructs a new Employer with the required name.
     *
     * @param newName the employer name
     */
    public Employer(final String newName) {
        this.name = newName;
    }

    /**
     * Sets timestamps before persisting a new entity.
     */
    @PrePersist
    void onCreate() {
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Updates the timestamp before updating an existing entity.
     */
    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
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
     * Returns the employer name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employer name.
     *
     * @param newName the name
     */
    public void setName(final String newName) {
        this.name = newName;
    }

    /**
     * Returns the Employer Identification Number (EIN).
     *
     * @return the EIN, or null if not set
     */
    public String getEin() {
        return ein;
    }

    /**
     * Sets the Employer Identification Number (EIN).
     *
     * @param newEin the EIN in XX-XXXXXXX format
     */
    public void setEin(final String newEin) {
        this.ein = newEin;
    }

    /**
     * Returns address line 1.
     *
     * @return address line 1, or null if not set
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * Sets address line 1.
     *
     * @param newAddressLine1 the address
     */
    public void setAddressLine1(final String newAddressLine1) {
        this.addressLine1 = newAddressLine1;
    }

    /**
     * Returns address line 2.
     *
     * @return address line 2, or null if not set
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * Sets address line 2.
     *
     * @param newAddressLine2 the address
     */
    public void setAddressLine2(final String newAddressLine2) {
        this.addressLine2 = newAddressLine2;
    }

    /**
     * Returns the city.
     *
     * @return the city, or null if not set
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param newCity the city
     */
    public void setCity(final String newCity) {
        this.city = newCity;
    }

    /**
     * Returns the two-letter state code.
     *
     * @return the state code, or null if not set
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the two-letter state code.
     *
     * @param newState the state code
     */
    public void setState(final String newState) {
        this.state = newState;
    }

    /**
     * Returns the ZIP code.
     *
     * @return the ZIP code, or null if not set
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the ZIP code.
     *
     * @param newZipCode the ZIP code
     */
    public void setZipCode(final String newZipCode) {
        this.zipCode = newZipCode;
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
     * Returns the last update timestamp.
     *
     * @return the update timestamp
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        var employer = (Employer) obj;
        return Objects.equals(id, employer.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public final String toString() {
        return "Employer{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", ein='" + ein + '\''
                + ", city='" + city + '\''
                + ", state='" + state + '\''
                + '}';
    }
}
