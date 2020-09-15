package com.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.server.property.IStringProperty;

public class PropertyInputElement<T extends IElementGUI> extends InputElementBase<T> {
    private final IStringProperty property;

    public PropertyInputElement(T gui, float posX, float posY, int width, IStringProperty property) {
        super(gui, posX, posY, width, true, property.getString());
        this.property = property;
    }

    public void readValue() {
        this.setText(this.property.getString());
    }

    public boolean tryWriteValue() {
        if (this.property.trySetString(this.text)) {
            this.readValue();
            return true;
        } else {
            this.readValue();
            return false;
        }
    }

    @Override
    protected void onSubmit() {
        this.tryWriteValue();
    }

    @Override
    protected boolean allowKey(int key) {
        return true;
    }
}
