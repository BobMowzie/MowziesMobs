package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelNaga;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderNaga extends MobRenderer<EntityNaga, ModelNaga<EntityNaga>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/naga.png");

    public RenderNaga(EntityRenderDispatcher mgr) {
        super(mgr, new ModelNaga<>(), 0);
    }

    @Override
    protected float getFlipDegrees(EntityNaga entity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNaga entity) {
        return TEXTURE;
    }

    @Override
    public void render(EntityNaga entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (entityIn.getAnimation() == EntityNaga.SPIT_ANIMATION && entityIn.mouthPos != null && entityIn.mouthPos.length > 0) entityIn.mouthPos[0] = MowzieRenderUtils.getWorldPosFromModel(entityIn, entityYaw, getModel().mouthSocket);
    }
}