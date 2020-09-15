package com.ilexiconn.llibrary.server.property;

/**
 * A double property which has a set of valid values in a range.
 */
public interface IDoubleRangeProperty extends IDoubleProperty {
    /**
     * A simple implementation that holds its own state.
     */
    class WithState extends IDoubleProperty.WithState implements IDoubleRangeProperty {
        private final double minDoubleValue;
        private final double maxDoubleValue;

        public WithState(double value, double minDoubleValue, double maxDoubleValue) {
            super(value);
            this.minDoubleValue = minDoubleValue;
            this.maxDoubleValue = maxDoubleValue;
        }

        @Override
        public double getMinDoubleValue() {
            return this.minDoubleValue;
        }

        @Override
        public double getMaxDoubleValue() {
            return this.maxDoubleValue;
        }
    }

    /**
     * Returns true if the int value is valid.
     * @param value the int value
     * @return true if the int value is valid
     */
    @Override
    default boolean isValidDouble(double value) {
        return value >= this.getMinDoubleValue() && value <= this.getMaxDoubleValue();
    }

    /**
     * Get the inclusive min double value.
     * @return the inclusive min double value
     */
    double getMinDoubleValue();

    /**
     * Get the inclusive max double value.
     * @return the inclusive max double value
     */
    double getMaxDoubleValue();

    /**
     * Get the double value range.
     * @return the double value range
     */
    default double getDoubleValueRange() {
        return this.getMaxDoubleValue() - this.getMinDoubleValue();
    }
}
