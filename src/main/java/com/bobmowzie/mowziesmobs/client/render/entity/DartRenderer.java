package com.bobmowzie.mowziesmobs.client.render.entity;

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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;

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
        this.bindEntityTexture(dart);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(dart.prevRotationYaw + (dart.rotationYaw - dart.prevRotationYaw) * delta - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(dart.prevRotationPitch + (dart.rotationPitch - dart.prevRotationPitch) * delta, 0.0F, 0.0F, 1.0F);
        Tessellator tes = Tessellator.getInstance();
        VertexBuffer buf = tes.getBuffer();
        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (b0 * 10) / 32.0F;
        float f5 = (5 + b0 * 10) / 32.0F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (5 + b0 * 10) / 32.0F;
        float f9 = (10 + b0 * 10) / 32.0F;
        float f10 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        float f11 = dart.arrowShake - delta;

        if (f11 > 0.0F) {
            float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
            GlStateManager.rotate(f12, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(f10, f10, f10);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);
        GlStateManager.glNormal3f(f10, 0.0F, 0.0F);
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-7.0D, -2.0D, -2.0D).tex(f6, f8).endVertex();
        buf.pos(-7.0D, -2.0D, 2.0D).tex(f7, f8).endVertex();
        buf.pos(-7.0D, 2.0D, 2.0D).tex(f7, f9).endVertex();
        buf.pos(-7.0D, 2.0D, -2.0D).tex(f6, f9).endVertex();
        tes.draw();
        GlStateManager.glNormal3f(-f10, 0.0F, 0.0F);
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-7.0D, 2.0D, -2.0D).tex(f6, f8).endVertex();
        buf.pos(-7.0D, 2.0D, 2.0D).tex(f7, f8).endVertex();
        buf.pos(-7.0D, -2.0D, 2.0D).tex(f7, f9).endVertex();
        buf.pos(-7.0D, -2.0D, -2.0D).tex(f6, f9).endVertex();
        tes.draw();

        for (int i = 0; i < 4; ++i) {
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.glNormal3f(0.0F, 0.0F, f10);
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buf.pos(-8.0D, -2.0D, 0.0D).tex(f2, f4).endVertex();
            buf.pos(8.0D, -2.0D, 0.0D).tex(f3, f4).endVertex();
            buf.pos(8.0D, 2.0D, 0.0D).tex(f3, f5).endVertex();
            buf.pos(-8.0D, 2.0D, 0.0D).tex(f2, f5).endVertex();
            tes.draw();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }
}