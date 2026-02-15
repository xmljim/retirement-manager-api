package com.xmljim.retirement.domain.contribution;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter for {@link AccountType} enum.
 *
 * <p>Handles the mapping between Java enum constants and database column
 * values, particularly for account types like 403B and 457B which cannot
 * be valid Java identifiers starting with a digit.</p>
 *
 * @since 1.0
 */
@Converter(autoApply = false)
public class AccountTypeConverter implements AttributeConverter<AccountType, String> {

    /**
     * Converts the AccountType enum to its database column representation.
     *
     * @param attribute the AccountType to convert
     * @return the database column value
     */
    @Override
    public String convertToDatabaseColumn(final AccountType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toDatabaseValue();
    }

    /**
     * Converts the database column value to an AccountType enum.
     *
     * @param dbData the database column value
     * @return the corresponding AccountType
     */
    @Override
    public AccountType convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }
        return AccountType.fromDatabaseValue(dbData);
    }
}
