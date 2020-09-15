package com.ilexiconn.llibrary.server.property.wrapper;

import net.ilexiconn.llibrary.server.property.IDoubleRangeProperty;
import net.ilexiconn.llibrary.server.property.IFloatRangeProperty;
import net.ilexiconn.llibrary.server.property.IIntRangeProperty;

import java.text.DecimalFormat;

/**
 * Wraps an IFloatRangeProperty as IFloatRangeProperty, IDoubleRangeProperty, IIntRangeProperty and IStringProperty.
 */
public class FloatRangePropertyWrapper extends FloatPropertyWrapperBase<IFloatRangeProperty> implements IFloatRangeProperty, IDoubleRangeProperty, IIntRangeProperty {
    public FloatRangePropertyWrapper(float value, float minFloatValue, float maxFloatValue, DecimalFormat decimalFormat) {
        this(new IFloatRangeProperty.WithState(value, minFloatValue, maxFloatValue), decimalFormat);
    }

    public FloatRangePropertyWrapper(IFloatRangeProperty delegateFor, DecimalFormat decimalFormat) {
        super(delegateFor, decimalFormat);
    }

    @Override
    public float getMinFloatValue() {
        return this.delegateFor.getMinFloatValue();
    }

    @Override
    public float getMaxFloatValue() {
        return this.delegateFor.getMaxFloatValue();
    }

    @Override
    public double getMinDoubleValue() {
        return this.getMinFloatValue();
    }

    @Override
    public double getMaxDoubleValue() {
        return this.getMaxFloatValue();
    }

    @Override
    public int getMinIntValue() {
        return (int)Math.ceil(this.getMinFloatValue());
    }

    @Override
    public int getMaxIntValue() {
        return (int)Math.floor(this.getMaxFloatValue());
    }
}
