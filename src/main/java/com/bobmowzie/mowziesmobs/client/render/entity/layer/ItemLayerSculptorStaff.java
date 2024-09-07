package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.client.render.entity.RenderSculptor;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ItemLayerSculptorStaff extends GeckoItemlayer<EntitySculptor> {
    public ItemLayerSculptorStaff(GeoRenderer rendererIn, String boneName) {
        super(rendererIn, boneName, null);
    }

    @Override
    public void renderForBone(PoseStack poseStack, EntitySculptor animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        renderedItem = animatable.heldStaff;
        super.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }

    @Override
    protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, EntitySculptor animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        float disappearProgress = Math.min(((RenderSculptor)getRenderer()).disappearController * 2f, 1f);
        poseStack.scale(1.0f - disappearProgress, 1.0f - disappearProgress, 1.0f - disappearProgress);
        super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
