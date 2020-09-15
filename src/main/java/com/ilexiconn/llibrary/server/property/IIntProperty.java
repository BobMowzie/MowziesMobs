package com.ilexiconn.llibrary.server.property;

/**
 * An int property which has a set of valid values.
 */
public interface IIntProperty {
    /**
     * A simple implementation that holds its own state.
     */
    class WithState implements IIntProperty {
        private int value;

        public WithState(int value) {
            this.value = value;
        }

        @Override
        public int getInt() {
            return this.value;
        }

        @Override
        public void setInt(int value) {
            this.value = value;
        }

        @Override
        public boolean isValidInt(int value) {
            return true;
        }
    }

    /**
     * Get the int value.
     * @return the int value
     */
    int getInt();

    /**
     * Set the int value, regardless of it being a valid value or not.
     * The validity of the int value should be evaluated with isValidInt.
     * @param value the int value
     */
    void setInt(int value);

    /**
     * Returns true if the int value is valid.
     * @param value the int value
     * @return true if the int value is valid
     */
    boolean isValidInt(int value);

    /**
     * Try to set the int value. Returns true if it succeeded.
     * @param value the int value
     * @return true if it succeeded
     */
    default boolean trySetInt(int value) {
        if (this.isValidInt(value)) {
            this.setInt(value);
            return true;
        } else {
            return false;
        }
    }
}
