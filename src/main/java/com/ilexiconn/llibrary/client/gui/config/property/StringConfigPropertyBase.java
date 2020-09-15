package com.ilexiconn.llibrary.client.gui.config.property;

import net.ilexiconn.llibrary.server.property.IStringProperty;
import net.minecraftforge.common.config.Property;

public abstract class StringConfigPropertyBase extends ForgeConfigProperty implements IStringProperty {
    public StringConfigPropertyBase(Property property) {
        super(property);
    }

    @Override
    public String getString() {
        return this.property.getString();
    }

    @Override
    public void setString(String value) {
        this.property.set(value);
    }

    public static StringConfigPropertyBase factory(Property property) {
        if (property.getValidValues().length > 0) {
            return new StringSelectionConfigProperty(property);
        } else {
            return new StringConfigProperty(property);
        }
    }
}
