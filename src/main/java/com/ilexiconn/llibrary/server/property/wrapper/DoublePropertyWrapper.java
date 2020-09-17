package com.ilexiconn.llibrary.server.property.wrapper;

import com.ilexiconn.llibrary.server.property.IDoubleProperty;

import java.text.DecimalFormat;

/**
 * Wraps an IDoubleProperty as IDoubleProperty, IFloatProperty, IIntProperty and IStringProperty.
 */
public class DoublePropertyWrapper extends DoublePropertyWrapperBase<IDoubleProperty> {
    public DoublePropertyWrapper(double value, DecimalFormat decimalFormat) {
        this(new IDoubleProperty.WithState(value), decimalFormat);
    }

    public DoublePropertyWrapper(IDoubleProperty delegateFor, DecimalFormat decimalFormat) {
        super(delegateFor, decimalFormat);
    }
}
