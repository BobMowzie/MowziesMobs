package com.ilexiconn.llibrary.server.property;

import java.util.Set;

/**
 * A string property which has a known set of valid values.
 */
public interface IStringSelectionProperty extends IStringProperty {
    /**
     * A simple implementation that holds its own state.
     */
    class WithState extends IStringProperty.WithState implements IStringSelectionProperty {
        private final Set<String> validValues;

        public WithState(String value) {
            this(value, null);
        }

        public WithState(String value, Set<String> validValues) {
            super(value);
            this.validValues = validValues;
        }

        @Override
        public Set<String> getValidStringValues() {
            return this.validValues;
        }
    }

    /**
     * Returns true if the string value is valid.
     * @param value the string value
     * @return true if the string value is valid
     */
    @Override
    default boolean isValidString(String value) {
        Set<String> validStringValues = this.getValidStringValues();
        return validStringValues == null || validStringValues.contains(value);
    }

    /**
     * Get the set of valid string values or null. If this set is null, any value is valid.
     * @return the set of valid string values or null
     */
    Set<String> getValidStringValues();
}
