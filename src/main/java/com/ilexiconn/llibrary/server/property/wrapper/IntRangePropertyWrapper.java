package com.ilexiconn.llibrary.server.property.wrapper;

import net.ilexiconn.llibrary.server.property.IDoubleRangeProperty;
import net.ilexiconn.llibrary.server.property.IFloatRangeProperty;
import net.ilexiconn.llibrary.server.property.IIntRangeProperty;

import java.text.NumberFormat;

/**
 * Wraps an IIntRangeProperty as IIntRangeProperty, IDoubleRangeProperty, IFloatRangeProperty and IStringProperty.
 */
public class IntRangePropertyWrapper extends IntPropertyWrapperBase<IIntRangeProperty> implements IIntRangeProperty, IDoubleRangeProperty, IFloatRangeProperty {
    public IntRangePropertyWrapper(int value, int minFloatValue, int maxFloatValue, NumberFormat numberFormat) {
        this(new IIntRangeProperty.WithState(value, minFloatValue, maxFloatValue), numberFormat);
    }

    public IntRangePropertyWrapper(IIntRangeProperty delegateFor, NumberFormat numberFormat) {
        super(delegateFor, numberFormat);
    }

    @Override
    public int getMinIntValue() {
        return this.delegateFor.getMinIntValue();
    }

    @Override
    public int getMaxIntValue() {
        return this.delegateFor.getMaxIntValue();
    }

    @Override
    public double getMinDoubleValue() {
        return this.getMinIntValue();
    }

    @Override
    public double getMaxDoubleValue() {
        return this.getMaxIntValue();
    }

    @Override
    public float getMinFloatValue() {
        return this.getMinIntValue();
    }

    @Override
    public float getMaxFloatValue() {
        return this.getMaxIntValue();
    }
}
