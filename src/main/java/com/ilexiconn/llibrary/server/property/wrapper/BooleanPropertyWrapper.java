package com.ilexiconn.llibrary.server.property.wrapper;

import net.ilexiconn.llibrary.server.property.IBooleanProperty;
import net.ilexiconn.llibrary.server.property.IStringSelectionProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * Wraps an IBooleanProperty as IBooleanProperty and IStringSelectionProperty.
 */
public class BooleanPropertyWrapper implements IBooleanProperty, IStringSelectionProperty {
    private final IBooleanProperty delegateFor;
    private final String trueName;
    private final String falseName;

    public BooleanPropertyWrapper(boolean value, String trueName, String falseName) {
        this(new IBooleanProperty.WithState(value), trueName, falseName);
    }

    public BooleanPropertyWrapper(IBooleanProperty delegateFor, String trueName, String falseName) {
        this.delegateFor = delegateFor;
        this.trueName = trueName;
        this.falseName = falseName;
    }

    @Override
    public boolean getBoolean() {
        return this.delegateFor.getBoolean();
    }

    @Override
    public void setBoolean(boolean value) {
        this.delegateFor.setBoolean(value);
    }

    @Override
    public boolean isValidBoolean(boolean value) {
        return true;
    }

    @Override
    public String getString() {
        return this.getBoolean() ? this.trueName : this.falseName;
    }

    @Override
    public void setString(String value) {
        if (value.equals(this.trueName)) {
            this.setBoolean(true);
        } else if (value.equals(this.falseName)) {
            this.setBoolean(false);
        }
    }

    @Override
    public boolean isValidString(String value) {
        if (value.equals(this.trueName)) {
            return this.isValidBoolean(true);
        } else if (value.equals(this.falseName)) {
            return this.isValidBoolean(false);
        }
        return this.isValidBoolean(Boolean.parseBoolean(value));
    }

    @Override
    public Set<String> getValidStringValues() {
        Set<String> result = new HashSet<>();
        if (this.isValidBoolean(true)) {
            result.add(this.trueName);
        }
        if (this.isValidBoolean(false)) {
            result.add(this.falseName);
        }
        return result;
    }
}
