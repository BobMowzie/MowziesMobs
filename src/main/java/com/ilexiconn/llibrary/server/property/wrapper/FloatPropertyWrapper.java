package com.ilexiconn.llibrary.server.property.wrapper;

import com.ilexiconn.llibrary.server.property.IFloatProperty;

import java.text.DecimalFormat;

/**
 * Wraps an IFloatProperty as IFloatProperty, IDoubleProperty, IIntProperty and IStringProperty.
 */
public class FloatPropertyWrapper extends FloatPropertyWrapperBase<IFloatProperty> {
    public FloatPropertyWrapper(float value, DecimalFormat decimalFormat) {
        this(new IFloatProperty.WithState(value), decimalFormat);
    }

    public FloatPropertyWrapper(IFloatProperty delegateFor, DecimalFormat decimalFormat) {
        super(delegateFor, decimalFormat);
    }
}
