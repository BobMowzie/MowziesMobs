package com.ilexiconn.llibrary.client.gui.update;

import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
public class ModUpdateEntryGUI extends GuiScrollingList {
    private ModUpdateGUI parent;
    private List<ITextComponent> textList = null;

    public ModUpdateEntryGUI(ModUpdateGUI parent, int width, List<String> textList) {
        super(parent.mc, width, parent.height, 32, parent.height - 55, parent.getModList().getWidth() + 20, 60, parent.width, parent.height);
        this.parent = parent;
        this.textList = this.resizeContent(textList);
        this.setHeaderInfo(true, this.getHeaderHeight());
    }

    @Override
    protected int getSize() {
        return 0;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tessellator) {

    }

    private List<ITextComponent> resizeContent(List<String> textList) {
        List<ITextComponent> list = new ArrayList<>();
        for (String text : textList) {
            if (text == null) {
                list.add(null);
                continue;
            }

            ITextComponent chat = ForgeHooks.newChatWithLinks(text, false);
            list.addAll(GuiUtilRenderComponents.splitText(chat, this.listWidth - 8, this.parent.mc.fontRenderer, false, true));
        }
        return list;
    }

    private int getHeaderHeight() {
        int height = (this.textList.size() * 10);
        if (height < this.bottom - this.top - 8) {
            height = this.bottom - this.top - 8;
        }
        return height;
    }

    @Override
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
        int top = relativeY;

        for (ITextComponent text : this.textList) {
            if (text != null) {
                GlStateManager.enableBlend();
                this.parent.mc.fontRenderer.drawStringWithShadow(text.getFormattedText(), this.left + 4, top, 0xFFFFFF);
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
            }
            top += 10;
        }
    }

    @Override
    protected void clickHeader(int x, int y) {
        if (y <= 0) {
            return;
        }

        int lineIdx = y / 10;
        if (lineIdx >= this.textList.size()) {
            return;
        }

        ITextComponent text = this.textList.get(lineIdx);
        if (text != null) {
            int k = -4;
            for (ITextComponent part : text) {
                if (!(part instanceof TextComponentString)) {
                    continue;
                }
                k += this.parent.mc.fontRenderer.getStringWidth(((TextComponentString) part).getText());
                if (k >= x) {
                    this.parent.handleComponentClick(part);
                    break;
                }
            }
        }
    }
}
