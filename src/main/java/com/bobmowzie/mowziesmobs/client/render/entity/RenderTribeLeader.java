package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeLeader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTribeLeader extends RenderLiving {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureTribeLeader.png");
    private static final ResourceLocation BURST_TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/textureSunstrike.png");
    private static final double BURST_RADIUS = 3.5;
    private static final int BURST_FRAME_COUNT = 10;
    private static final int BURST_START_FRAME = 12;

    public RenderTribeLeader(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Override
    public void preRenderCallback(EntityLivingBase entity, float side) {
        super.preRenderCallback(entity, side);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase entity) {
        return 0;
    }

    @Override
    public void doRender(EntityLiving entityLiving, double x, double y, double z, float yaw, float delta) {
        EntityTribeLeader barako = (EntityTribeLeader) entityLiving;
        if (barako.getAnimation() == EntityTribeLeader.ATTACK_ANIMATION && barako.getAnimationTick() > BURST_START_FRAME && barako.getAnimationTick() < BURST_START_FRAME + BURST_FRAME_COUNT - 1) {
            GL11.glPushMatrix();
            GL11.glTranslated(x, y + 1.1, z);
            setupGL();
            bindTexture(BURST_TEXTURE);
            GL11.glRotatef(-renderManager.playerViewY, 0, 1, 0);
            GL11.glRotatef(renderManager.playerViewX, 1, 0, 0);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            drawBurst(barako.getAnimationTick() - BURST_START_FRAME + delta);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            revertGL();
            GL11.glPopMatrix();
        }
        super.doRender(entityLiving, x, y, z, yaw, delta);
    }

    private void drawBurst(float tick) {
        int dissapateFrame = 6;
        float firstSpeed = 2f;
        float secondSpeed = 1f;
        int frame = ((int) (tick * firstSpeed) <= dissapateFrame) ? (int) (tick * firstSpeed) : (int) (dissapateFrame + (tick - dissapateFrame / firstSpeed) * secondSpeed);
        if (frame > BURST_FRAME_COUNT) {
            frame = BURST_FRAME_COUNT;
        }
        double minU = 0.0625 * frame;
        double maxU = minU + 0.0625;
        double minV = 0.5;
        double maxV = minV + 0.5;
        double offset = 0.219 * (frame % 2);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setBrightness(240);
        t.setColorRGBA_F(1, 1, 1, (tick < 8) ? 0.8f : 0.4f);
        t.addVertexWithUV(-BURST_RADIUS + offset, -BURST_RADIUS + offset, 0, minU, minV);
        t.addVertexWithUV(-BURST_RADIUS + offset, BURST_RADIUS + offset, 0, minU, maxV);
        t.addVertexWithUV(BURST_RADIUS + offset, BURST_RADIUS + offset, 0, maxU, maxV);
        t.addVertexWithUV(BURST_RADIUS + offset, -BURST_RADIUS + offset, 0, maxU, minV);
        GL11.glDepthMask(false);
        t.draw();
        GL11.glDepthMask(true);
    }

    private void setupGL() {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0);
    }

    private void revertGL() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
    }
}
