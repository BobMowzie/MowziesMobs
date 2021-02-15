package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBlockSwapper;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityIceBreath;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderBlockSwapper extends EntityRenderer<EntityBlockSwapper> {
    public RenderBlockSwapper(EntityRendererManager mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBlockSwapper entity) {
        return null;
    }

    @Override
    public void render(EntityBlockSwapper entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

    }
}
