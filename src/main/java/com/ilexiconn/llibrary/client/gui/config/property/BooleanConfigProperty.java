package com.ilexiconn.llibrary.client.gui.config.property;

import net.ilexiconn.llibrary.client.gui.config.ConfigGUI;
import net.ilexiconn.llibrary.client.gui.element.CheckboxElement;
import net.ilexiconn.llibrary.client.gui.element.Element;
import net.ilexiconn.llibrary.server.property.IBooleanProperty;
import net.minecraftforge.common.config.Property;

public class BooleanConfigProperty extends ForgeConfigProperty implements IBooleanProperty {
    public BooleanConfigProperty(Property configProperty) {
        super(configProperty);
    }

    @Override
    public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
        return new CheckboxElement<>(gui, x, y, this);
    }

    @Override
    public boolean getBoolean() {
        return this.property.getBoolean();
    }

    @Override
    public void setBoolean(boolean value) {
        this.property.set(value);
    }

    @Override
    public boolean isValidBoolean(boolean value) {
        return true;
    }
}
