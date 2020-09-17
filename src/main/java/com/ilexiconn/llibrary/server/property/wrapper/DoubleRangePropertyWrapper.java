package com.ilexiconn.llibrary.server.property.wrapper;

import com.ilexiconn.llibrary.server.property.IDoubleRangeProperty;
import com.ilexiconn.llibrary.server.property.IFloatRangeProperty;
import com.ilexiconn.llibrary.server.property.IIntRangeProperty;

import java.text.DecimalFormat;

/**
 * Wraps an IDoubleRangeProperty as IDoubleRangeProperty, IFloatFloatProperty, IIntRangeProperty and IStringProperty.
 */
public class DoubleRangePropertyWrapper extends DoublePropertyWrapperBase<IDoubleRangeProperty> implements IDoubleRangeProperty, IFloatRangeProperty, IIntRangeProperty {
    public DoubleRangePropertyWrapper(double value, double minDoubleValue, double maxDoubleValue, DecimalFormat decimalFormat) {
        super(new IDoubleRangeProperty.WithState(value, minDoubleValue, maxDoubleValue), decimalFormat);
    }

    public DoubleRangePropertyWrapper(IDoubleRangeProperty delegateFor, DecimalFormat decimalFormat) {
        super(delegateFor, decimalFormat);
    }

    @Override
    public double getMinDoubleValue() {
        return this.delegateFor.getMinDoubleValue();
    }

    @Override
    public double getMaxDoubleValue() {
        return this.delegateFor.getMaxDoubleValue();
    }

    @Override
    public float getMinFloatValue() {
        return (float)this.getMinDoubleValue();
    }

    @Override
    public float getMaxFloatValue() {
        return (float)this.getMaxDoubleValue();
    }

    @Override
    public int getMinIntValue() {
        return (int)Math.ceil(this.getMinDoubleValue());
    }

    @Override
    public int getMaxIntValue() {
        return (int)Math.floor(this.getMaxDoubleValue());
    }
}
