package com.ilexiconn.llibrary.server.property.wrapper;

import com.ilexiconn.llibrary.server.property.IDoubleProperty;
import com.ilexiconn.llibrary.server.property.IFloatProperty;
import com.ilexiconn.llibrary.server.property.IIntProperty;
import com.ilexiconn.llibrary.server.property.IStringProperty;

import java.text.DecimalFormat;
import java.text.ParseException;

public abstract class DoublePropertyWrapperBase<T extends IDoubleProperty> implements IDoubleProperty, IFloatProperty, IIntProperty, IStringProperty {
    protected final T delegateFor;
    protected final DecimalFormat decimalFormat;

    public DoublePropertyWrapperBase(T delegateFor, DecimalFormat decimalFormat) {
        this.delegateFor = delegateFor;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public double getDouble() {
        return this.delegateFor.getDouble();
    }

    @Override
    public void setDouble(double value) {
        this.delegateFor.setDouble(value);
    }

    @Override
    public boolean isValidDouble(double value) {
        return this.delegateFor.isValidDouble(value);
    }

    @Override
    public float getFloat() {
        return (float)this.getDouble();
    }

    @Override
    public void setFloat(float value) {
        this.setDouble(value);
    }

    @Override
    public boolean isValidFloat(float value) {
        return this.isValidDouble(value);
    }

    @Override
    public int getInt() {
        return (int)Math.round(this.getDouble());
    }

    @Override
    public void setInt(int value) {
        this.setDouble(value);
    }

    @Override
    public boolean isValidInt(int value) {
        return this.isValidDouble(value);
    }

    @Override
    public String getString() {
        return this.decimalFormat.format(this.getDouble());
    }

    @Override
    public void setString(String value) {
        try {
            this.setDouble(this.decimalFormat.parse(value).doubleValue());
        } catch (ParseException e) {}
    }

    @Override
    public boolean isValidString(String value) {
        try {
            return this.isValidDouble(this.decimalFormat.parse(value).doubleValue());
        } catch (ParseException e) {
            return false;
        }
    }
}
