package com.bobmowzie.mowziesmobs.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderNothing extends EntityRenderer<Entity> {
    public RenderNothing(EntityRendererManager mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

    @Override
    public void render(Entity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

    }
}
