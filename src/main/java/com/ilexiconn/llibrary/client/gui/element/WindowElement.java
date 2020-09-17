package com.ilexiconn.llibrary.client.gui.element;

import com.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class WindowElement<T extends IElementGUI> extends Element<T> {
    private String name;
    private float dragOffsetX;
    private float dragOffsetY;
    private boolean isDragging;
    private boolean hasCloseButton;

    private List<Element<T>> elementList = new ArrayList<>();

    public WindowElement(T gui, String name, int width, int height) {
        this(gui, name, width, height, gui.getWidth() / 2 - width / 2, gui.getHeight() / 2 - height / 2, true);
    }

    public WindowElement(T gui, String name, int width, int height, boolean hasCloseButton) {
        this(gui, name, width, height, gui.getWidth() / 2 - width / 2, gui.getHeight() / 2 - height / 2, hasCloseButton);
    }

    public WindowElement(T handler, String name, int width, int height, int posX, int posY, boolean hasCloseButton) {
        super(handler, posX, posY, width, height);
        this.name = name;
        this.hasCloseButton = hasCloseButton;
        if (hasCloseButton) {
            this.addElement(new ButtonElement<>(this.gui, "x", this.getWidth() - 14, 0, 14, 14, (v) -> {
                this.gui.removeElement(this);
                return true;
            }).withColorScheme(ButtonElement.CLOSE));
        }
    }

    public void addElement(Element<T> element) {
        element.withParent(this);
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        this.startScissor();
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight(), LLibrary.CONFIG.getPrimaryColor());
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getWidth(), 14, LLibrary.CONFIG.getAccentColor());
        FontRenderer fontRenderer = this.gui.getFontRenderer();
        fontRenderer.drawString(this.name, this.getPosX() + 2.0F, this.getPosY() + 3.0F, LLibrary.CONFIG.getTextColor(), false);
        for (Element<T> element : this.elementList) {
            element.render(mouseX, mouseY, partialTicks);
        }
        GlStateManager.popMatrix();
        this.endScissor();
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (button != 0 || !this.isSelected(mouseX, mouseY)) {
            return false;
        }
        if (mouseY < this.getPosY() + 14) {
            this.dragOffsetX = mouseX - this.getPosX();
            this.dragOffsetY = mouseY - this.getPosY();
            this.isDragging = true;
            this.gui.sendElementToFront(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(float mouseX, float mouseY, int button, long timeSinceClick) {
        if (this.isDragging) {
            this.setPosX(Math.min(Math.max(mouseX - this.dragOffsetX, 0), this.gui.getWidth() - this.getWidth()));
            this.setPosY(Math.min(Math.max(mouseY - this.dragOffsetY, 0), this.gui.getHeight() - this.getHeight()));
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        this.isDragging = false;
        return false;
    }

    @Override
    public boolean keyPressed(char character, int keyCode) {
        return false;
    }
}

