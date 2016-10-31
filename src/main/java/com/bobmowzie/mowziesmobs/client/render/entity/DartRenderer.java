package com.bobmowzie.mowziesmobs.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DartRenderer extends Render<EntityDart> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/textureDart.png");

    public DartRenderer(RenderManager mgr) {
        super(mgr);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDart entity) {
        return DartRenderer.TEXTURE;
    }

    @Override
    public void doRender(EntityDart dart, double x, double y, double z, float yaw, float delta) {
        bindEntityTexture(dart);
        GlStateManager.color(1, 1, 1);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(dart.prevRotationYaw + (dart.rotationYaw - dart.prevRotationYaw) * delta - 90, 0, 1, 0);
        GlStateManager.rotate(dart.prevRotationPitch + (dart.rotationPitch - dart.prevRotationPitch) * delta, 0, 0, 1);
        Tessellator tes = Tessellator.getInstance();
        VertexBuffer buf = tes.getBuffer();
        GlStateManager.enableRescaleNormal();
        float shake = dart.arrowShake - delta;
        if (shake > 0) {
            GlStateManager.rotate(-MathHelper.sin(shake * 3) * shake, 0, 0, 1);
        }
        GlStateManager.rotate(45, 1, 0, 0);
        GlStateManager.scale(0.05625F, 0.05625F, 0.05625F);
        GlStateManager.translate(-4, 0, 0);
        if (renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(dart));
        }
        GlStateManager.glNormal3f(0.05625F, 0, 0);
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-7, -2, -2).tex(0, 0.15625D).endVertex();
        buf.pos(-7, -2, 2).tex(0.15625D, 0.15625D).endVertex();
        buf.pos(-7, 2, 2).tex(0.15625D, 0.3125D).endVertex();
        buf.pos(-7, 2, -2).tex(0, 0.3125D).endVertex();
        tes.draw();
        GlStateManager.glNormal3f(-0.05625F, 0, 0);
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-7, 2, -2).tex(0, 0.15625D).endVertex();
        buf.pos(-7, 2, 2).tex(0.15625D, 0.15625D).endVertex();
        buf.pos(-7, -2, 2).tex(0.15625D, 0.3125D).endVertex();
        buf.pos(-7, -2, -2).tex(0, 0.3125D).endVertex();
        tes.draw();
        for (int i = 0; i < 4; ++i) {
            GlStateManager.rotate(90, 1, 0, 0);
            GlStateManager.glNormal3f(0, 0, 0.05625F);
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buf.pos(-8, -2, 0).tex(0, 0).endVertex();
            buf.pos(8, -2, 0).tex(0.5D, 0).endVertex();
            buf.pos(8, 2, 0).tex(0.5D, 0.15625D).endVertex();
            buf.pos(-8, 2, 0).tex(0, 0.15625D).endVertex();
            tes.draw();
        }
        if (renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(dart, x, y, z, yaw, delta);
    }
}