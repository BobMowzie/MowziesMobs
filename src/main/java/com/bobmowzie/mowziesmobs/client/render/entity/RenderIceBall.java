package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelIceBall;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBall;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderIceBall extends EntityRenderer<EntityIceBall> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/effects/ice_ball.png");
    public ModelIceBall model;

    public RenderIceBall(EntityRendererManager mgr) {
        super(mgr);
        model = new ModelIceBall();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityIceBall entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityIceBall entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        RenderSystem.pushMatrix();
        RenderSystem.rotatef(entityYaw, 0, -1, 0);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(this.getEntityTexture(entityIn)));
        model.render(matrixStackIn, ivertexbuilder, packedLightIn, 0, 1, 1, 1, 1);
        RenderSystem.popMatrix();
    }
}
