package com.ilexiconn.llibrary.server.property;

/**
 * A string property which has a set of valid values.
 */
public interface IStringProperty {
    /**
     * A simple implementation that holds its own state.
     */
    class WithState implements IStringProperty {
        private String value;

        public WithState(String value) {
            this.value = value;
        }

        @Override
        public String getString() {
            return this.value;
        }

        @Override
        public void setString(String value) {
            this.value = value;
        }

        @Override
        public boolean isValidString(String value) {
            return true;
        }
    }

    /**
     * Get the string value.
     * @return the string value
     */
    String getString();

    /**
     * Set the string value, regardless of it being a valid value or not.
     * The validity of the string value should be evaluated with isValidString.
     * @param value the string value
     */
    void setString(String value);

    /**
     * Returns true if the string value is valid.
     * @param value the string value
     * @return true if the string value is valid
     */
    boolean isValidString(String value);

    /**
     * Try to set the string value. Returns true if it succeeded.
     * @param value the string value
     * @return true if it succeeded
     */
    default boolean trySetString(String value) {
        if (this.isValidString(value)) {
            this.setString(value);
            return true;
        } else {
            return false;
        }
    }
}
