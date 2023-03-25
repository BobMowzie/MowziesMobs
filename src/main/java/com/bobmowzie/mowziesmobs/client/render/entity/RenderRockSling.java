package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelRockSling;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityRockSling;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class RenderRockSling extends GeoProjectilesRenderer<EntityRockSling> {

    private int currentTick = -1;

    public RenderRockSling(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelRockSling());
    }

    @Override
    public RenderType getRenderType(EntityRockSling animatable, float partialTick, PoseStack poseStack,
                                    MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight,
                                    ResourceLocation texture) {
        return RenderType.entityCutout(getTextureLocation(animatable));
    }
}
