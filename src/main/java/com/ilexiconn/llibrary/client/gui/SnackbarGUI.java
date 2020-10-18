package com.ilexiconn.llibrary.client.gui;

import com.ilexiconn.llibrary.client.ClientEventHandler;
import com.ilexiconn.llibrary.client.ClientProxy;
import com.ilexiconn.llibrary.client.util.ClientUtils;
import com.ilexiconn.llibrary.server.snackbar.Snackbar;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class SnackbarGUI extends AbstractGui {
    private Snackbar snackbar;
    private int maxAge;
    private int age;
    private float yOffset = 20;

    public SnackbarGUI(Snackbar snackbar) {
        this.snackbar = snackbar;
        this.maxAge = snackbar.getDuration() > 0 ? snackbar.getDuration() : ClientProxy.MINECRAFT.fontRenderer.getStringWidth(snackbar.getMessage()) * 3;
    }

    public void updateSnackbar() {
        this.age++;
        if (this.age > this.maxAge) {
            ClientEventHandler.INSTANCE.setOpenSnackbar(null);
        }
    }

    public void drawSnackbar() {
        GlStateManager.pushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 500.0F);
        MainWindow resolution = Minecraft.getInstance().mainWindow;
        switch (this.snackbar.getPosition()) {
            case UP:
                GlStateManager.translatef(0.0F, -this.yOffset, 0.0F);
                fill(0, 20, resolution.getScaledWidth(), 0, this.snackbar.getColor());
                ClientProxy.MINECRAFT.fontRenderer.drawString(this.snackbar.getMessage(), 10, 6, 0xFFFFFFFF);
                break;
            case DOWN:
                GlStateManager.translatef(0.0F, this.yOffset, 0.0F);
                fill(0, resolution.getScaledHeight() - 20, resolution.getScaledWidth(), resolution.getScaledHeight(), this.snackbar.getColor());
                ClientProxy.MINECRAFT.fontRenderer.drawString(this.snackbar.getMessage(), 10, resolution.getScaledHeight() - 14, 0xFFFFFFFF);
                break;
        }
        GlStateManager.popMatrix();
        this.yOffset = ClientUtils.updateValue(this.yOffset, this.age < this.maxAge - 61 ? 0.0F : 20.0F);
    }
}
