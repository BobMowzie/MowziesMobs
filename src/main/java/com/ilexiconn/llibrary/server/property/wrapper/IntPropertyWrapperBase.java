package com.ilexiconn.llibrary.server.property.wrapper;

import net.ilexiconn.llibrary.server.property.IDoubleProperty;
import net.ilexiconn.llibrary.server.property.IFloatProperty;
import net.ilexiconn.llibrary.server.property.IIntProperty;
import net.ilexiconn.llibrary.server.property.IStringProperty;

import java.text.NumberFormat;
import java.text.ParseException;

public abstract class IntPropertyWrapperBase<T extends IIntProperty> implements IIntProperty, IDoubleProperty, IFloatProperty, IStringProperty {
    protected final T delegateFor;
    protected final NumberFormat numberFormat;

    public IntPropertyWrapperBase(T delegateFor, NumberFormat numberFormat) {
        this.delegateFor = delegateFor;
        this.numberFormat = numberFormat;
    }

    @Override
    public int getInt() {
        return this.delegateFor.getInt();
    }

    @Override
    public void setInt(int value) {
        this.delegateFor.setInt(value);
    }

    @Override
    public boolean isValidInt(int value) {
        return this.delegateFor.isValidInt(value);
    }

    @Override
    public double getDouble() {
        return (double)this.getInt();
    }

    @Override
    public void setDouble(double value) {
        this.setInt((int)Math.round(value));
    }

    @Override
    public boolean isValidDouble(double value) {
        return this.isValidInt((int)Math.round(value));
    }

    @Override
    public float getFloat() {
        return this.getInt();
    }

    @Override
    public void setFloat(float value) {
        this.setInt(Math.round(value));
    }

    @Override
    public boolean isValidFloat(float value) {
        return this.isValidInt(Math.round(value));
    }

    @Override
    public String getString() {
        return this.numberFormat.format(this.getInt());
    }

    @Override
    public void setString(String value) {
        try {
            this.setInt(this.numberFormat.parse(value).intValue());
        } catch (ParseException e) {}
    }

    @Override
    public boolean isValidString(String value) {
        try {
            return this.isValidInt(this.numberFormat.parse(value).intValue());
        } catch (ParseException e) {
            return false;
        }
    }
}
