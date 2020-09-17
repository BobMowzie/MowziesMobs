package com.ilexiconn.llibrary.client.gui.update;

import com.ilexiconn.llibrary.server.update.UpdateContainer;
import com.ilexiconn.llibrary.server.update.UpdateHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModUpdateGUI extends GuiScreen {
    private GuiMainMenu parent;
    private ModUpdateListGUI modList;
    private ModUpdateEntryGUI modInfo;
    private int selected = -1;
    private GuiButton buttonUpdate;
    private GuiButton buttonDone;

    public ModUpdateGUI(GuiMainMenu parent) {
        this.parent = parent;
    }

    public ModUpdateListGUI getModList() {
        return this.modList;
    }

    public ModUpdateEntryGUI getModInfo() {
        return this.modInfo;
    }

    @Override
    public void initGui() {
        int width = 0;
        for (UpdateContainer mod : UpdateHandler.INSTANCE.getOutdatedModList()) {
            width = Math.max(width, this.fontRenderer.getStringWidth(mod.getModContainer().getName()) + 47);
            width = Math.max(width, this.fontRenderer.getStringWidth(mod.getModContainer().getVersion()) + 47);
        }
        width = Math.min(width, 150);
        this.modList = new ModUpdateListGUI(this, width);

        this.buttonList.add(this.buttonDone = new GuiButton(6, ((this.modList.getRight() + this.width) / 2) - 100, this.height - 38, I18n.format("gui.done")));
        this.buttonList.add(this.buttonUpdate = new GuiButton(20, 10, this.height - 38, this.modList.getWidth(), 20, I18n.format("gui.com.ilexiconn.llibrary.update")));

        this.updateModInfo();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            switch (button.id) {
                case 6: {
                    this.mc.displayGuiScreen(this.parent);
                    return;
                }
                case 20: {
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URI(UpdateHandler.INSTANCE.getOutdatedModList().get(this.selected).getUpdateURL()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (UpdateHandler.INSTANCE.getOutdatedModList().isEmpty()) {
            this.drawDefaultBackground();
            int i = this.width / 2;
            int j = this.height / 2;
            this.buttonDone.x = this.width / 2 - 100;
            this.buttonDone.y = this.height - 38;
            this.buttonList.clear();
            this.buttonList.add(this.buttonDone);
            this.drawScaledString(I18n.format("gui.com.ilexiconn.llibrary.updated.1"), i, j - 40, 0xFFFFFF, 2.0F);
            this.drawScaledString(I18n.format("gui.com.ilexiconn.llibrary.updated.2"), i, j - 15, 0xFFFFFF, 1.0F);
        } else {
            this.modList.drawScreen(mouseX, mouseY, partialTicks);
            if (this.modInfo != null) {
                this.modInfo.drawScreen(mouseX, mouseY, partialTicks);
            }

            int left = ((this.width - this.modList.getWidth() - 38) / 2) + this.modList.getWidth() + 30;
            this.drawCenteredString(this.fontRenderer, I18n.format("gui.com.ilexiconn.llibrary.update.title"), left, 16, 0xFFFFFF);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void selectModIndex(int index) {
        if (this.selected != index) {
            this.selected = index;
            this.updateModInfo();
        }
    }

    public boolean modIndexSelected(int index) {
        return this.selected == index;
    }

    private void updateModInfo() {
        this.buttonUpdate.visible = false;
        this.modInfo = null;

        if (this.selected == -1) {
            return;
        }

        List<String> textList = new ArrayList<>();

        this.buttonUpdate.visible = true;
        this.buttonUpdate.enabled = true;
        this.buttonUpdate.displayString = I18n.format("gui.com.ilexiconn.llibrary.update");

        UpdateContainer updateContainer = UpdateHandler.INSTANCE.getOutdatedModList().get(this.selected);
        textList.add(updateContainer.getModContainer().getName());
        textList.add(I18n.format("gui.com.ilexiconn.llibrary.currentVersion") + String.format(": %s", updateContainer.getModContainer().getVersion()));
        textList.add(I18n.format("gui.com.ilexiconn.llibrary.latestVersion") + String.format(": %s", updateContainer.getLatestVersion().getVersionString()));
        textList.add(null);
        Collections.addAll(textList, UpdateHandler.INSTANCE.getChangelog(updateContainer, updateContainer.getLatestVersion()));

        this.modInfo = new ModUpdateEntryGUI(this, this.width - this.modList.getWidth() - 30, textList);
    }

    public void drawScaledString(String text, int x, int y, int color, float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        this.drawCenteredString(this.fontRenderer, text, (int) (x / scale), (int) (y / scale), color);
        GL11.glPopMatrix();
    }

    @Override
    public boolean handleComponentClick(ITextComponent component) {
        return super.handleComponentClick(component);
    }
}