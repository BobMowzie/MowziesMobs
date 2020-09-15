package com.ilexiconn.llibrary.server.property;

/**
 * An int property which has a set of valid values in a range.
 */
public interface IIntRangeProperty extends IIntProperty {
    /**
     * A simple implementation that holds its own state.
     */
    class WithState extends IIntProperty.WithState implements IIntRangeProperty {
        private final int minIntValue;
        private final int maxIntValue;

        public WithState(int value, int minIntValue, int maxIntValue) {
            super(value);
            this.minIntValue = minIntValue;
            this.maxIntValue = maxIntValue;
        }

        @Override
        public int getMinIntValue() {
            return this.minIntValue;
        }

        @Override
        public int getMaxIntValue() {
            return this.maxIntValue;
        }
    }

    /**
     * Returns true if the int value is valid.
     * @param value the int value
     * @return true if the int value is valid
     */
    @Override
    default boolean isValidInt(int value) {
        return value >= this.getMinIntValue() && value <= this.getMaxIntValue();
    }

    /**
     * Get the inclusive min int value.
     * @return the inclusive min int value
     */
    int getMinIntValue();

    /**
     * Get the inclusive max int value.
     * @return the inclusive max int value
     */
    int getMaxIntValue();

    /**
     * Get the int value range.
     * @return the int value range
     */
    default int getIntValueRange() {
        return this.getMaxIntValue() - this.getMinIntValue();
    }
}
