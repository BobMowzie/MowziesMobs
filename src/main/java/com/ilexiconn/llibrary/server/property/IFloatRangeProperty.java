package com.ilexiconn.llibrary.server.property;

/**
 * A float property which has a set of valid values in a range.
 */
public interface IFloatRangeProperty extends IFloatProperty {
    /**
     * A simple implementation that holds its own state.
     */
    class WithState extends IFloatProperty.WithState implements IFloatRangeProperty {
        private final float minFloatValue;
        private final float maxFloatValue;

        public WithState(float value, float minFloatValue, float maxFloatValue) {
            super(value);
            this.minFloatValue = minFloatValue;
            this.maxFloatValue = maxFloatValue;
        }

        @Override
        public float getMinFloatValue() {
            return this.minFloatValue;
        }

        @Override
        public float getMaxFloatValue() {
            return this.maxFloatValue;
        }
    }

    /**
     * Returns true if the float value is valid.
     * @param value the float value
     * @return true if the float value is valid
     */
    @Override
    default boolean isValidFloat(float value) {
        return value >= this.getMinFloatValue() && value <= this.getMaxFloatValue();
    }

    /**
     * Get the inclusive min float value.
     * @return the inclusive min float value
     */
    float getMinFloatValue();

    /**
     * Get the inclusive max float value.
     * @return the inclusive max float value
     */
    float getMaxFloatValue();

    /**
     * Get the float value range.
     * @return the float value range
     */
    default float getFloatValueRange() {
        return this.getMaxFloatValue() - this.getMinFloatValue();
    }
}
