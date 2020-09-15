package com.ilexiconn.llibrary.client.gui.element;

import net.ilexiconn.llibrary.LLibrary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class ScrollbarElement<T extends IElementGUI> extends Element<T> {
    private int maxScroll;
    private float scrollPerEntry;
    private Supplier<Integer> entryCount;
    private float entryHeight;
    private Supplier<Float> offsetX;
    private Supplier<Float> offsetY;
    private Supplier<Float> displayHeight;

    private float scroll;
    private float scrollYOffset;
    private boolean scrolling;
    private float scrollVelocity;

    public ScrollbarElement(Element<T> parent, Supplier<Float> posX, Supplier<Float> posY, Supplier<Float> displayHeight, int entryHeight, Supplier<Integer> entryCount) {
        super(parent.gui, posX.get(), posY.get(), 4, 0);
        this.withParent(parent);
        this.offsetX = posX;
        this.offsetY = posY;
        this.entryHeight = entryHeight;
        this.entryCount = entryCount;
        this.displayHeight = displayHeight;
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        if (this.maxScroll > 0) {
            this.drawRectangle(this.getPosX(), this.getPosY(), this.getWidth(), this.getHeight() - 3, this.scrolling ? LLibrary.CONFIG.getAccentColor() : LLibrary.CONFIG.getSecondaryColor());
        }
        this.scroll -= this.scrollVelocity;
        this.scrollVelocity *= 0.6F;
        if (this.scroll > this.maxScroll / this.scrollPerEntry) {
            this.scroll = (int) (this.maxScroll / this.scrollPerEntry);
        } else if (this.scroll < 0) {
            this.scroll = 0;
        }
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        if (this.maxScroll > 0 && button == 0) {
            if (this.isSelected(mouseX, mouseY)) {
                this.scrolling = true;
                this.scrollYOffset = (int) (mouseY - this.getPosY());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(float mouseX, float mouseY, int button, long timeSinceClick) {
        if (this.scrolling) {
            this.scroll = (int) Math.max(0, Math.min(this.maxScroll / this.scrollPerEntry, mouseY - this.scrollYOffset - (this.offsetY.get() + this.getParent().getPosY())));
        }
        return this.scrolling;
    }

    @Override
    public boolean mouseReleased(float mouseX, float mouseY, int button) {
        this.scrolling = false;
        return false;
    }

    @Override
    public void update() {
        this.setPosX(this.offsetX.get());
        this.setPosY(this.offsetY.get() + this.scroll);
        float parentHeight = this.getParent().getHeight();
        int maxDisplayEntries = (int) (parentHeight / this.entryHeight);
        int entryCount = this.entryCount.get();
        this.maxScroll = Math.max(0, entryCount - maxDisplayEntries);
        this.scrollPerEntry = (float) entryCount / parentHeight;
        this.setHeight((int) (parentHeight / ((float) entryCount / (float) maxDisplayEntries)));
    }

    public float getScrollOffset() {
        return this.scroll * (this.entryCount.get() / this.displayHeight.get()) * this.entryHeight;
    }

    public boolean isScrolling() {
        return this.scrolling;
    }

    public int getMaxScroll() {
        return this.maxScroll;
    }

    public float getScrollVelocity() {
        return this.scrollVelocity;
    }

    public void setScrollVelocity(float scrollVelocity) {
        this.scrollVelocity = scrollVelocity;
    }
}
