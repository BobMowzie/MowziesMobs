package com.ilexiconn.llibrary.client.gui.element;

import com.ilexiconn.llibrary.LLibrary;
import com.ilexiconn.llibrary.server.property.IIntProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class ColorElement<T extends IElementGUI> extends Element<T> {
    public static final int[] COLORS = {0xFFFEBA01, 0xFFFF8B00, 0xFFF7620E, 0xFFCA500F, 0xFFDA3B01, 0xFFEF6950, 0xFFD03438, 0xFFFF4244, 0xFFE64856, 0xFFE81123, 0xFFEA005F, 0xFFC40052, 0xFFE3008B, 0xFFBE0177, 0xFFC339B3, 0xFF9B008A, 0xFF0177D7, 0xFF0063B1, 0xFF928FD6, 0xFF6B69D6, 0xFF8764B8, 0xFF744DA8, 0xFFB046C2, 0xFF871797, 0xFF0099BB, 0xFF2D7C9A, 0xFF01B7C4, 0xFF038288, 0xFF00B294, 0xFF018675, 0xFF00CE70, 0xFF10883E, 0xFF7A7474, 0xFF5E5A57, 0xFF677689, 0xFF505C6A, 0xFF577C74, 0xFF496860, 0xFF4A8205, 0xFF107C0F, 0xFF767676, 0xFF4B4A48, 0xFF6A797E, 0xFF4C535B, 0xFF647C64, 0xFF535D54, 0xFF837544, 0xFF7E735F};
    private final IIntProperty selectedColor;
    private final int[] colors;
    private int horizontalRows;

    public ColorElement(T gui, float posX, float posY, int width, int height, int color) {
        this(gui, posX, posY, width, height, new IIntProperty.WithState(color));
    }

    public ColorElement(T gui, float posX, float posY, int width, int height, IIntProperty selectedColor) {
        super(gui, posX, posY, width, height);
        this.selectedColor = selectedColor;
        this.colors = COLORS;
        this.horizontalRows = (int) Math.ceil(width / 23);
    }

    @Override
    public void render(float mouseX, float mouseY, float partialTicks) {
        this.drawRectangle(this.getPosX() + 1, this.getPosY() + 1, this.getWidth() - 1, this.getHeight() - 1, LLibrary.CONFIG.getSecondaryColor());
        float offsetX = this.getWidth() / 2 - (this.horizontalRows * 23 + 4) / 2;
        float offsetY = this.getHeight() / 2 - (this.colors.length / this.horizontalRows * 23 + 4) / 2;
        for (int i = 0; i < this.colors.length; i++) {
            int color = this.colors[i];
            int x = i % this.horizontalRows * 23 + 4;
            int y = i / this.horizontalRows * 23 + 4;
            this.drawRectangle(this.getPosX() + x + offsetX, this.getPosY() + y + offsetY, 20, 20, color);
            if (color == LLibrary.CONFIG.getAccentColor()) {
                this.drawOutline(this.getPosX() + x + offsetX, this.getPosY() + y + offsetY, 20, 20, LLibrary.CONFIG.getTextColor(), 2);
            }
        }
    }

    @Override
    public boolean mouseClicked(float mouseX, float mouseY, int button) {
        float offsetX = this.getWidth() / 2 - (this.horizontalRows * 23 + 4) / 2;
        float offsetY = this.getHeight() / 2 - (this.colors.length / this.horizontalRows * 23 + 4) / 2;
        for (int i = 0; i < this.colors.length; i++) {
            float x = this.getPosX() + i % this.horizontalRows * 23 + 4 + offsetX;
            float y = this.getPosY() + i / this.horizontalRows * 23 + 4 + offsetY;
            if (button == 0 && mouseX >= x && mouseX <= x + 20 && mouseY >= y && mouseY <= y + 20) {
                if (this.selectedColor.trySetInt(this.colors[i])) {
                    this.gui.playClickSound();
                }
                return true;
            }
        }
        return false;
    }

    public int getColor() {
        return this.selectedColor.getInt();
    }
}
