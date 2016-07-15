package com.bobmowzie.mowziesmobs.server.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.MowziesMobs;

public class MowziePotion extends Potion {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/gui/container/potions.png");
    private static final float TEXTURE_SIZE = 64;
    private static final int ICON_SIZE = 18;
    private static final int ICON_ROW_LENGTH = 3;

    public MowziePotion(int id, boolean isBadEffect, int liquidColor) {
        super(id, isBadEffect, liquidColor);
    }

    @Override
    public boolean isReady(int id, int amplifier) {
        return true;
    }

    @Override
    public final boolean hasStatusIcon() {
        return false;
    }

    @Override
    public final MowziePotion setIconIndex(int x, int y) {
        super.setIconIndex(x + y * ICON_ROW_LENGTH, 0);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        mc.getTextureManager().bindTexture(TEXTURE);
        int index = getStatusIconIndex();
        drawTexturedRect(x + 6, y + 7, index % ICON_ROW_LENGTH * ICON_SIZE, index / ICON_ROW_LENGTH * ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }

    public void drawTexturedRect(int x, int y, int u, int v, int width, int height) {
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x, y + height, 0, u / TEXTURE_SIZE, (v + height) / TEXTURE_SIZE);
        t.addVertexWithUV(x + width, y + height, 0, (u + width) / TEXTURE_SIZE, (v + height) / TEXTURE_SIZE);
        t.addVertexWithUV(x + width, y, 0, (u + width) / TEXTURE_SIZE, v / TEXTURE_SIZE);
        t.addVertexWithUV(x, y, 0, u / TEXTURE_SIZE, v / TEXTURE_SIZE);
        t.draw();
    }
}
