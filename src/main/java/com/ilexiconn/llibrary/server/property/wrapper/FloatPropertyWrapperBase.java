package com.ilexiconn.llibrary.server.property.wrapper;

import net.ilexiconn.llibrary.server.property.IDoubleProperty;
import net.ilexiconn.llibrary.server.property.IFloatProperty;
import net.ilexiconn.llibrary.server.property.IIntProperty;
import net.ilexiconn.llibrary.server.property.IStringProperty;

import java.text.DecimalFormat;
import java.text.ParseException;

public abstract class FloatPropertyWrapperBase<T extends IFloatProperty> implements IFloatProperty, IDoubleProperty, IIntProperty, IStringProperty {
    protected final T delegateFor;
    protected final DecimalFormat decimalFormat;

    public FloatPropertyWrapperBase(T delegateFor, DecimalFormat decimalFormat) {
        this.delegateFor = delegateFor;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public float getFloat() {
        return this.delegateFor.getFloat();
    }

    @Override
    public void setFloat(float value) {
        this.delegateFor.setFloat(value);
    }

    @Override
    public boolean isValidFloat(float value) {
        return this.delegateFor.isValidFloat(value);
    }

    @Override
    public double getDouble() {
        return (double)this.getFloat();
    }

    @Override
    public void setDouble(double value) {
        this.setFloat((float)value);
    }

    @Override
    public boolean isValidDouble(double value) {
        return this.isValidFloat((float)value);
    }

    @Override
    public int getInt() {
        return Math.round(this.getFloat());
    }

    @Override
    public void setInt(int value) {
        this.setFloat(value);
    }

    @Override
    public boolean isValidInt(int value) {
        return this.isValidFloat(value);
    }

    @Override
    public String getString() {
        return this.decimalFormat.format(this.getFloat());
    }

    @Override
    public void setString(String value) {
        try {
            this.setFloat(this.decimalFormat.parse(value).floatValue());
        } catch (ParseException e) {}
    }

    @Override
    public boolean isValidString(String value) {
        try {
            return this.isValidFloat(this.decimalFormat.parse(value).floatValue());
        } catch (ParseException e) {
            return false;
        }
    }
}
