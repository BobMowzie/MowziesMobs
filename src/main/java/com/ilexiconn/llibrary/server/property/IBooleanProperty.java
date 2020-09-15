package com.ilexiconn.llibrary.server.property;

/**
 * A boolean property which has a set of valid values.
 */
public interface IBooleanProperty {
    /**
     * A simple implementation that holds its own state.
     */
    class WithState implements IBooleanProperty {
        private boolean value;

        public WithState(boolean value) {
            this.value = value;
        }

        @Override
        public boolean getBoolean() {
            return this.value;
        }

        @Override
        public void setBoolean(boolean value) {
            this.value = value;
        }

        @Override
        public boolean isValidBoolean(boolean value) {
            return true;
        }
    }

    /**
     * Get the boolean value.
     * @return the boolean value
     */
    boolean getBoolean();

    /**
     * Set the boolean value, regardless of it being a valid value or not.
     * The validity of the boolean value should be evaluated with isValidBoolean.
     * @param value the boolean value
     */
    void setBoolean(boolean value);

    /**
     * Returns true if the boolean value is valid.
     * @param value the boolean value
     * @return true if the boolean value is valid
     */
    boolean isValidBoolean(boolean value);

    /**
     * Try to set the boolean value. Returns true if it succeeded.
     * @param value the boolean value
     * @return true if it succeeded
     */
    default boolean trySetBoolean(boolean value) {
        if (this.isValidBoolean(value)) {
            this.setBoolean(value);
            return true;
        } else {
            return false;
        }
    }
}
