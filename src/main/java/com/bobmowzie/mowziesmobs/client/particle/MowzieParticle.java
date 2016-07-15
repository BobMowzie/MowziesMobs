package com.bobmowzie.mowziesmobs.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.MowziesMobs;

@SideOnly(Side.CLIENT)
public abstract class MowzieParticle extends EntityFX implements IIcon {
    private static final int TEX_SIZE = 64;
    private static final int TEX_TILE_SIZE = 8;

    public MowzieParticle(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.particleIcon = this;
    }

    public void setTextureIndex(int x, int y) {
        this.particleTextureIndexX = x;
        this.particleTextureIndexY = y;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public float getMinU() {
        return IconStitcher.INSTANCE.icon.getInterpolatedU(this.particleTextureIndexX * TEX_TILE_SIZE / TEX_SIZE * 16);
    }

    @Override
    public float getMinV() {
        return IconStitcher.INSTANCE.icon.getInterpolatedV(this.particleTextureIndexY * TEX_TILE_SIZE / TEX_SIZE * 16);
    }

    @Override
    public float getMaxU() {
        return IconStitcher.INSTANCE.icon.getInterpolatedU((this.particleTextureIndexX + 1) * TEX_TILE_SIZE / TEX_SIZE * 16);
    }

    @Override
    public float getMaxV() {
        return IconStitcher.INSTANCE.icon.getInterpolatedV((this.particleTextureIndexY + 1) * TEX_TILE_SIZE / TEX_SIZE * 16);
    }

    @Override
    public int getIconWidth() {
        throw new IllegalStateException("I'm breaking everything!");
    }

    @Override
    public int getIconHeight() {
        throw new IllegalStateException("I'm breaking everything!");
    }

    @Override
    public float getInterpolatedU(double t) {
        throw new IllegalStateException("I'm breaking everything!");
    }

    @Override
    public float getInterpolatedV(double t) {
        throw new IllegalStateException("I'm breaking everything!");
    }

    @Override
    public String getIconName() {
        throw new IllegalStateException("I'm breaking everything!");
    }

    public enum IconStitcher {
        INSTANCE;

        public String location = MowziesMobs.MODID + ":particles";
        public IIcon icon;

        @SubscribeEvent
        public void onTextureStitch(TextureStitchEvent.Pre event) {
            if (event.map == Minecraft.getMinecraft().getTextureMapBlocks()) {
                this.icon = event.map.registerIcon(this.location);
            }
        }
    }
}
