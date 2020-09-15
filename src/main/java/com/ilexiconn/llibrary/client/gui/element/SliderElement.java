package com.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.ClientProxy;
import net.ilexiconn.llibrary.server.property.IFloatRangeProperty;
import net.ilexiconn.llibrary.server.property.IStringProperty;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SliderElement<T extends IElementGUI, P extends IFloatRangeProperty & IStringProperty> extends Element<T> {
    private final P value;
    private float step;
    private boolean hasSlider;
    private float sliderWidth;
    private boolean editable = true;
    private boolean dragging;
    private PropertyInputElement<T> inputElement;

    public SliderElement(T gui, float posX, float posY, P value, float step) {
        this(gui, posX, posY, 0.0F, value, step);
    }

    public SliderElement(T gui, float posX, float posY, float sliderWidth, P value, float step) {
        super(gui, posX, posY, (int) (38 + sliderWidth), 12);
        this.value = value;
        this.step = step;
        this.hasSlider = sliderWidth > 0.0F;
        this.sliderWidth = sliderWidth;
    }

    @Override
    public void init() {
        this.inputElement = (PropertyInputElement<T>)new PropertyInputElement<>(this.gui, -1.0F, 0.0F, 28, this.value).withParent(this);
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        float posX = this.getPosX();
        float posY = this.getPosY();
        int width = this.getWidth();
        int height = this.getHeight();
        this.drawRectangle(posX, posY, width, height, this.editable ? LLibrary.CONFIG.getSecondaryColor() : LLibrary.CONFIG.getSecondarySubcolor());
        boolean selected = this.isSelected(mouseX, mouseY);
        boolean upperSelected = this.editable && selected && mouseX >= posX + width - this.sliderWidth - 11 && mouseY < posY + 6 && mouseX < posX + width - this.sliderWidth;
        boolean lowerSelected = this.editable && selected && mouseX >= posX + width - this.sliderWidth - 11 && mouseY > posY + 6 && mouseX < posX + width - this.sliderWidth;
        this.drawRectangle(posX + width - 11 - this.sliderWidth, posY, 11, 6, this.editable ? upperSelected ? LLibrary.CONFIG.getDarkAccentColor() : LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getTertiaryColor());
        this.drawRectangle(posX + width - 11 - this.sliderWidth, posY + 6, 11, 6, this.editable ? lowerSelected ? LLibrary.CONFIG.getDarkAccentColor() : LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getTertiaryColor());
        int textColor = LLibrary.CONFIG.getTextColor();
        this.drawRectangle(posX + width - 8 - this.sliderWidth, posY + 4, 5, 1, textColor);
        this.drawRectangle(posX + width - 7 - this.sliderWidth, posY + 3, 3, 1, textColor);
        this.drawRectangle(posX + width - 6 - this.sliderWidth, posY + 2, 1, 1, textColor);
        this.drawRectangle(posX + width - 8 - this.sliderWidth, posY + 7, 5, 1, textColor);
        this.drawRectangle(posX + width - 7 - this.sliderWidth, posY + 8, 3, 1, textColor);
        this.drawRectangle(posX + width - 6 - this.sliderWidth, posY + 9, 1, 1, textColor);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        float scaleFactor = new ScaledResolution(ClientProxy.MINECRAFT).getScaleFactor();
        GL11.glScissor((int) (posX * scaleFactor), (int) ((this.gui.getHeight() - (posY + height)) * scaleFactor), (int) ((width - 11) * scaleFactor), (int) (height * scaleFactor));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        if (this.hasSlider) {
            float offsetX = ((this.sliderWidth - 4) * (this.getValue() - this.value.getMinFloatValue()) / this.value.getFloatValueRange());
            boolean indicatorSelected = this.editable && selected && mouseX >= this.getPosX() + 38 + offsetX && mouseX <= this.getPosX() + 38 + offsetX + 4;
            this.drawRectangle(this.getPosX() + 38 + offsetX, this.getPosY(), 4, this.getHeight(), this.editable ? indicatorSelected ? LLibrary.CONFIG.getDarkAccentColor() : LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getTertiaryColor());
        }
    }

    private boolean trySetValue(float value) {
        if (this.value.trySetFloat(value)) {
            this.inputElement.readValue();
            return true;
        } else {
            return false;
        }
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        this.inputElement.setEditable(editable);
    }

    /**
     * @deprecated Property should be changed through property object
     */
    @Deprecated
    public SliderElement<T, P> withValue(float value) {
        this.trySetValue(value);
        return this;
    }

    @Override
    public boolean mouseScrolled(float mouseX, float mouseY, int amount) {
        if (this.editable && (this.isSelected(mouseX, mouseY) || this.inputElement.isSelected(mouseX, mouseY))) {
            float delta = (amount / 120.0F) * this.step;
            if (GuiScreen.isShiftKeyDown()) {
                delta *= 10.0F;
            }
            return this.trySetValue(MathHelper.clamp(this.value.getFloat() + delta, this.value.getMinFloatValue(), this.value.getMaxFloatValue()));
        }
        return false;
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (!this.editable) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        float offsetX = ((this.sliderWidth - 4) * (this.getValue() - this.value.getMinFloatValue()) / this.value.getFloatValueRange());
        boolean selected = this.isSelected(mouseX, mouseY);
        boolean indicatorSelected = selected && mouseX >= this.getPosX() + 38 + offsetX && mouseX <= this.getPosX() + 38 + offsetX + 4;
        boolean upperSelected = selected && mouseX >= this.getPosX() + this.getWidth() - this.sliderWidth - 11 && mouseY < this.getPosY() + 6 && mouseX < this.getPosX() + this.getWidth() - this.sliderWidth;
        boolean lowerSelected = selected && mouseX >= this.getPosX() + this.getWidth() - this.sliderWidth - 11 && mouseY > this.getPosY() + 6 && mouseX < this.getPosX() + this.getWidth() - this.sliderWidth;
        if (!indicatorSelected) {
            if (upperSelected || lowerSelected) {
                float delta = 0.0F;
                if (upperSelected) delta += this.step;
                if (lowerSelected) delta -= this.step;
                if (GuiScreen.isShiftKeyDown()) delta *= 10.0F;

                if (this.trySetValue(this.value.getFloat() + delta)) {
                    this.gui.playClickSound();
                    return true;
                }
            }
        } else {
            this.dragging = true;
            this.gui.playClickSound();
            return true;
        }
        return false;
    }

    private float getValue() {
        return this.value.getFloat();
    }

    @Override
    public boolean mouseDragged(float mouseX, float mouseY, int button, long timeSinceClick) {
        if (this.dragging) {
            float newValue = (((mouseX - (this.getPosX() + 38.0F)) / ((this.getWidth() - 38.0F) - 4.0F)) * this.value.getFloatValueRange()) + this.value.getMinFloatValue();
            if (newValue > this.value.getMaxFloatValue()) {
                newValue = this.value.getMaxFloatValue();
            }
            if (newValue < this.value.getMinFloatValue()) {
                newValue = this.value.getMinFloatValue();
            }
            this.trySetValue(newValue);
        }
        return this.dragging;
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        if (this.dragging) {
            this.gui.playClickSound();
        }
        this.dragging = false;
        return false;
    }

    public InputElementBase getValueInput() {
        return this.inputElement;
    }
}
