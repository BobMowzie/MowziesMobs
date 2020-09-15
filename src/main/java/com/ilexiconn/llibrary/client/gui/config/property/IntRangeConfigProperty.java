package com.ilexiconn.llibrary.client.gui.config.property;

import net.ilexiconn.llibrary.client.gui.config.ConfigGUI;
import net.ilexiconn.llibrary.client.gui.element.Element;
import net.ilexiconn.llibrary.client.gui.element.SliderElement;
import net.ilexiconn.llibrary.server.property.IIntRangeProperty;
import net.ilexiconn.llibrary.server.property.wrapper.IntRangePropertyWrapper;
import net.minecraftforge.common.config.Property;

import java.text.NumberFormat;

public class IntRangeConfigProperty extends ForgeConfigProperty implements IIntRangeProperty {
    private final int minIntValue;
    private final int maxIntValue;

    public IntRangeConfigProperty(Property property) {
        super(property);
        this.minIntValue = Integer.parseInt(property.getMinValue());
        this.maxIntValue = Integer.parseInt(property.getMaxValue());
    }

    @Override
    public Element<ConfigGUI> provideElement(ConfigGUI gui, float x, float y) {
        return new SliderElement<>(gui, x, y, new IntRangePropertyWrapper(this, NumberFormat.getIntegerInstance()), 1.0F);
    }

    @Override
    public int getInt() {
        return this.property.getInt();
    }

    @Override
    public void setInt(int value) {
        this.property.set(value);
    }

    @Override
    public int getMinIntValue() {
        return this.minIntValue;
    }

    @Override
    public int getMaxIntValue() {
        return this.maxIntValue;
    }
}
