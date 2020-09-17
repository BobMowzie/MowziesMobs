package com.ilexiconn.llibrary.client.model.tools;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @author pau101
 * @since 1.4.0
 */
@OnlyIn(Dist.CLIENT)
public class Model3DTexture extends ModelBox {
    private int width;
    private int height;

    private float u1;
    private float v1;
    private float u2;
    private float v2;

    public Model3DTexture(ModelRenderer model, int textureOffsetX, int textureOffsetY, float posX, float posY, float posZ, int width, int height) {
        super(model, 0, 0, posX, posY, posZ, 0, 0, 0, 0);
        this.width = width;
        this.height = height;
        this.u1 = textureOffsetX / model.textureWidth;
        this.v1 = textureOffsetY / model.textureHeight;
        this.u2 = (textureOffsetX + width) / model.textureWidth;
        this.v2 = (textureOffsetY + height) / model.textureHeight;
    }

    public Model3DTexture(ModelRenderer model, int textureOffsetX, int textureOffsetY, int width, int height) {
        this(model, textureOffsetX, textureOffsetY, 0, 0, 0, width, height);
    }

    @Override
    public void render(BufferBuilder BufferBuilder, float scale) {
        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(this.posX1 * scale, this.posY1 * scale, this.posZ1 * scale);
        GlStateManager.scale(this.width / 16F, this.height / 16F, 1);
        float depth = 0.0625F;
        BufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        BufferBuilder.pos(0.0D, 0.0D, 0.0D).tex(this.u1, this.v2).normal(0.0F, 0.0F, 1.0F).endVertex();
        BufferBuilder.pos(1.0D, 0.0D, 0.0D).tex(this.u2, this.v2).normal(0.0F, 0.0F, 1.0F).endVertex();
        BufferBuilder.pos(1.0D, 1.0D, 0.0D).tex(this.u2, this.v1).normal(0.0F, 0.0F, 1.0F).endVertex();
        BufferBuilder.pos(0.0D, 1.0D, 0.0D).tex(this.u1, this.v1).normal(0.0F, 0.0F, 1.0F).endVertex();
        tessellator.draw();
        BufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        BufferBuilder.pos(0.0D, 1.0D, (0.0F - depth)).tex(this.u1, this.v1).normal(0.0F, 0.0F, -1.0F).endVertex();
        BufferBuilder.pos(1.0D, 1.0D, (0.0F - depth)).tex(this.u2, this.v1).normal(0.0F, 0.0F, -1.0F).endVertex();
        BufferBuilder.pos(1.0D, 0.0D, (0.0F - depth)).tex(this.u2, this.v2).normal(0.0F, 0.0F, -1.0F).endVertex();
        BufferBuilder.pos(0.0D, 0.0D, (0.0F - depth)).tex(this.u1, this.v2).normal(0.0F, 0.0F, -1.0F).endVertex();
        tessellator.draw();
        float f5 = 0.5F * (this.u1 - this.u2) / this.width;
        float f6 = 0.5F * (this.v2 - this.v1) / this.height;
        BufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        int k;
        float f7;
        float f8;

        for (k = 0; k < this.width; k++) {
            f7 = (float) k / (float) this.width;
            f8 = this.u1 + (this.u2 - this.u1) * f7 - f5;
            BufferBuilder.pos(f7, 0.0D, (0.0F - depth)).tex(f8, this.v2).normal(-1.0F, 0.0F, 0.0F).endVertex();
            BufferBuilder.pos(f7, 0.0D, 0.0D).tex(f8, this.v2).normal(-1.0F, 0.0F, 0.0F).endVertex();
            BufferBuilder.pos(f7, 1.0D, 0.0D).tex(f8, this.v1).normal(-1.0F, 0.0F, 0.0F).endVertex();
            BufferBuilder.pos(f7, 1.0D, (0.0F - depth)).tex(f8, this.v1).normal(-1.0F, 0.0F, 0.0F).endVertex();
        }

        tessellator.draw();
        BufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        float f9;

        for (k = 0; k < this.width; k++) {
            f7 = (float) k / (float) this.width;
            f8 = this.u1 + (this.u2 - this.u1) * f7 - f5;
            f9 = f7 + 1.0F / this.width;
            BufferBuilder.pos(f9, 1.0D, (0.0F - depth)).tex(f8, this.v1).normal(1.0F, 0.0F, 0.0F).endVertex();
            BufferBuilder.pos(f9, 1.0D, 0.0D).tex(f8, this.v1).normal(1.0F, 0.0F, 0.0F).endVertex();
            BufferBuilder.pos(f9, 0.0D, 0.0D).tex(f8, this.v2).normal(1.0F, 0.0F, 0.0F).endVertex();
            BufferBuilder.pos(f9, 0.0D, (0.0F - depth)).tex(f8, this.v2).normal(1.0F, 0.0F, 0.0F).endVertex();
        }

        tessellator.draw();
        BufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        for (k = 0; k < this.height; k++) {
            f7 = (float) k / (float) this.height;
            f8 = this.v2 + (this.v1 - this.v2) * f7 - f6;
            f9 = f7 + 1.0F / this.height;
            BufferBuilder.pos(0.0D, f9, 0.0D).tex(this.u1, f8).normal(0.0F, 1.0F, 0.0F).endVertex();
            BufferBuilder.pos(1.0D, f9, 0.0D).tex(this.u2, f8).normal(0.0F, 1.0F, 0.0F).endVertex();
            BufferBuilder.pos(1.0D, f9, (0.0F - depth)).tex(this.u2, f8).normal(0.0F, 1.0F, 0.0F).endVertex();
            BufferBuilder.pos(0.0D, f9, (0.0F - depth)).tex(this.u1, f8).normal(0.0F, 1.0F, 0.0F).endVertex();
        }

        tessellator.draw();
        BufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        for (k = 0; k < this.height; k++) {
            f7 = (float) k / (float) this.height;
            f8 = this.v2 + (this.v1 - this.v2) * f7 - f6;
            BufferBuilder.pos(1.0D, f7, 0.0D).tex(this.u2, f8).normal(0.0F, -1.0F, 0.0F).endVertex();
            BufferBuilder.pos(0.0D, f7, 0.0D).tex(this.u1, f8).normal(0.0F, -1.0F, 0.0F).endVertex();
            BufferBuilder.pos(0.0D, f7, (0.0F - depth)).tex(this.u1, f8).normal(0.0F, -1.0F, 0.0F).endVertex();
            BufferBuilder.pos(1.0D, f7, (0.0F - depth)).tex(this.u2, f8).normal(0.0F, -1.0F, 0.0F).endVertex();
        }

        tessellator.draw();
        GlStateManager.popMatrix();
    }
}
