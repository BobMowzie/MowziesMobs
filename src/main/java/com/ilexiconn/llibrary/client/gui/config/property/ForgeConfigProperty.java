package com.ilexiconn.llibrary.client.gui.config.property;

import net.ilexiconn.llibrary.client.gui.config.ConfigProperty;
import net.minecraftforge.common.config.Property;

public abstract class ForgeConfigProperty extends ConfigProperty {
    protected final Property property;

    public ForgeConfigProperty(Property property) {
        super(property.getName(), property.getComment());
        this.property = property;
    }

    public static ForgeConfigProperty factory(Property property) {
        if (!property.isList()) {
            switch (property.getType()) {
                case BOOLEAN:
                    return new BooleanConfigProperty(property);
                case COLOR:
                    return new ColorConfigProperty(property);
                case STRING:
                    return StringConfigProperty.factory(property);
                case DOUBLE:
                    return new DoubleRangeConfigProperty(property);
                case INTEGER:
                    return new IntRangeConfigProperty(property);
            }
        }
        return null;
    }
}
