package com.ilexiconn.llibrary.client.gui.survivaltab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class PageButtonGUI extends GuiButton {
    private GuiScreen screen;

    public PageButtonGUI(int id, int x, int y, GuiScreen screen) {
        super(id, x, y, 20, 20, id == -1 ? "<" : ">");
        this.screen = screen;
        this.updateState();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        if (this.id == -1) {
            mc.fontRenderer.drawString((SurvivalTabHandler.INSTANCE.getCurrentPage() + 1) + "/" + SurvivalTabHandler.INSTANCE.getSurvivalTabList().size() / 8 + 1, this.x + 31, this.y + 6, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            if (this.id == -1) {
                int currentPage = SurvivalTabHandler.INSTANCE.getCurrentPage();
                if (currentPage > 0) {
                    SurvivalTabHandler.INSTANCE.setCurrentPage(currentPage - 1);
                    this.initGui();
                    this.updateState();
                }
            } else if (this.id == -2) {
                int currentPage = SurvivalTabHandler.INSTANCE.getCurrentPage();
                if (currentPage < SurvivalTabHandler.INSTANCE.getSurvivalTabList().size() / 8) {
                    SurvivalTabHandler.INSTANCE.setCurrentPage(currentPage + 1);
                    this.initGui();
                    this.updateState();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void updateState() {
        for (GuiButton button : this.screen.buttonList) {
            if (button.id == -1) {
                button.enabled = SurvivalTabHandler.INSTANCE.getCurrentPage() >= SurvivalTabHandler.INSTANCE.getSurvivalTabList().size() / 8;
            } else if (button.id == -2) {
                button.enabled = SurvivalTabHandler.INSTANCE.getCurrentPage() <= 0;
            }
        }
    }

    public void initGui() {
        this.screen.buttonList.removeIf(guiButton -> guiButton instanceof SurvivalTabGUI);
        this.screen.initGui();
        MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Post(this.screen, this.screen.buttonList));
    }
}

