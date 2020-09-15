package com.ilexiconn.llibrary.server.property;

/**
 * A float property which has a set of valid values.
 */
public interface IFloatProperty {
    /**
     * A simple implementation that holds its own state.
     */
    class WithState implements IFloatProperty {
        private float value;

        public WithState(float value) {
            this.value = value;
        }

        @Override
        public float getFloat() {
            return this.value;
        }

        @Override
        public void setFloat(float value) {
            this.value = value;
        }

        @Override
        public boolean isValidFloat(float value) {
            return true;
        }
    }

    /**
     * Get the float value.
     * @return the float value
     */
    float getFloat();

    /**
     * Set the float value, regardless of it being a valid value or not.
     * The validity of the float value should be evaluated with isValidFloat.
     * @param value the float value
     */
    void setFloat(float value);

    /**
     * Returns true if the float value is valid.
     * @param value the float value
     * @return true if float int value is valid
     */
    boolean isValidFloat(float value);

    /**
     * Try to set the float value. Returns true if it succeeded.
     * @param value the float value
     * @return true if it succeeded
     */
    default boolean trySetFloat(float value) {
        if (this.isValidFloat(value)) {
            this.setFloat(value);
            return true;
        } else {
            return false;
        }
    }
}
