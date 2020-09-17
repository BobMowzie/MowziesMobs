package com.ilexiconn.llibrary.client.gui.element;

import com.ilexiconn.llibrary.LLibrary;
import com.ilexiconn.llibrary.server.property.IBooleanProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class CheckboxElement<T extends IElementGUI> extends Element<T> {
    public final IBooleanProperty selected;

    public CheckboxElement(T gui, float posX, float posY, boolean selected) {
        this(gui, posX, posY, new IBooleanProperty.WithState(selected));
    }

    public CheckboxElement(T gui, float posX, float posY, IBooleanProperty selected) {
        super(gui, posX, posY, 12, 12);
        this.selected = selected;
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        if (this.isEnabled()) {
            this.drawRectangle(this.getPosX() + 1, this.getPosY() + 1, this.getWidth() - 1, this.getHeight() - 1, LLibrary.CONFIG.getSecondaryColor());
        } else {
            this.drawRectangle(this.getPosX() + 1, this.getPosY() + 1, this.getWidth() - 1, this.getHeight() - 1, LLibrary.CONFIG.getTertiaryColor());
        }
        if (this.selected.getBoolean()) {
            this.drawRectangle(this.getPosX() + 3, this.getPosY() + 3, this.getWidth() - 5, this.getHeight() - 5, LLibrary.CONFIG.getTextColor());
        }
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (button == 0 && super.isSelected(mouseX, mouseY)) {
            if (this.selected.trySetBoolean(!this.selected.getBoolean())) {
                this.gui.playClickSound();
            }
            return true;
        }
        return false;
    }

    public CheckboxElement<T> withSelection(boolean selected) {
        this.selected.trySetBoolean(selected);
        return this;
    }

    public boolean isSelected() {
        return this.selected.getBoolean();
    }
}

