package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;

@SideOnly(Side.CLIENT)
public abstract class EntityMMFX extends EntityFX implements IIcon {
    private static final float TEX_SIZE = 64;

    private static final int TEX_TILE_SIZE = 8;

    public EntityMMFX(World world, double x, double y, double z) {
        super(world, x, y, z);
        particleIcon = this;
    }

    protected final void setTextureIndex(int x, int y) {
        particleTextureIndexX = x;
        particleTextureIndexY = y;
    }

    @Override
    public final int getFXLayer() {
        return 1;
    }

    @Override
    public final float getMinU() {
        return Stitcher.icon.getInterpolatedU(particleTextureIndexX * TEX_TILE_SIZE / TEX_SIZE * 16);
    }

    @Override
    public final float getMinV() {
        return Stitcher.icon.getInterpolatedV(particleTextureIndexY * TEX_TILE_SIZE / TEX_SIZE * 16);
    }

    @Override
    public final float getMaxU() {
        return Stitcher.icon.getInterpolatedU((particleTextureIndexX + 1) * TEX_TILE_SIZE / TEX_SIZE * 16);
    }

    @Override
    public final float getMaxV() {
        return Stitcher.icon.getInterpolatedV((particleTextureIndexY + 1) * TEX_TILE_SIZE / TEX_SIZE * 16);
    }

    @Override
    public final int getIconWidth() {
        throw fail();
    }

    @Override
    public final int getIconHeight() {
        throw fail();
    }

    @Override
    public final float getInterpolatedU(double t) {
        throw fail();
    }

    @Override
    public final float getInterpolatedV(double t) {
        throw fail();
    }

    @Override
    public final String getIconName() {
        throw fail();
    }

    private static IllegalStateException fail() {
        return new IllegalStateException("I'm breaking everything!");
    }

    public static class Stitcher {
        public static final Stitcher INSTANCE = new Stitcher();

        private static final String LOCATION = MowziesMobs.MODID + ":particles";

        private static IIcon icon;

        private Stitcher() {
        }

        @SubscribeEvent
        public void onTextureStitch(TextureStitchEvent.Pre event) {
            if (event.map == Minecraft.getMinecraft().getTextureMapBlocks()) {
                icon = event.map.registerIcon(LOCATION);
            }
        }
    }
}
