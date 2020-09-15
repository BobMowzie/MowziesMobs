package com.ilexiconn.llibrary.server.property;

/**
 * A double property which has a set of valid values.
 */
public interface IDoubleProperty {
    /**
     * A simple implementation that holds its own state.
     */
    class WithState implements IDoubleProperty {
        private double value;

        public WithState(double value) {
            this.value = value;
        }

        @Override
        public double getDouble() {
            return this.value;
        }

        @Override
        public void setDouble(double value) {
            this.value = value;
        }

        @Override
        public boolean isValidDouble(double value) {
            return true;
        }
    }

    /**
     * Get the double value.
     * @return the double value
     */
    double getDouble();

    /**
     * Set the double value, regardless of it being a valid value or not.
     * The validity of the double value should be evaluated with isValidDouble.
     * @param value the double value
     */
    void setDouble(double value);

    /**
     * Returns true if the double value is valid.
     * @param value the double value
     * @return true if double int value is valid
     */
    boolean isValidDouble(double value);

    /**
     * Try to set the double value. Returns true if it succeeded.
     * @param value the double value
     * @return true if it succeeded
     */
    default boolean trySetDouble(double value) {
        if (this.isValidDouble(value)) {
            this.setDouble(value);
            return true;
        } else {
            return false;
        }
    }
}
