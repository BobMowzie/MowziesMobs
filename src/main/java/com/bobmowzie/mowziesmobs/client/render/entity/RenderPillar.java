package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelPillar;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.util.RenderUtils;

@OnlyIn(Dist.CLIENT)
public class RenderPillar extends RenderGeomancyBase<EntityPillar> {
    private static final ResourceLocation TEXTURE_DIRT = new ResourceLocation("textures/blocks/dirt.png");

    public RenderPillar(EntityRendererProvider.Context mgr) {
        super(mgr, new ModelPillar());
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPillar entity) {
        return TEXTURE_DIRT;
    }

    @Override
    public void preRender(PoseStack poseStack, EntityPillar animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        EntityGeomancyBase.GeomancyTier tier = getEntity().getTier();
        MowzieGeoModel<EntityPillar> mowzieModel = (MowzieGeoModel<EntityPillar>) getGeoModel();
        MowzieGeoBone tier1 = mowzieModel.getMowzieBone("tier1");
        MowzieGeoBone tier2 = mowzieModel.getMowzieBone("tier2");
        MowzieGeoBone tier3 = mowzieModel.getMowzieBone("tier3");
        MowzieGeoBone tier4 = mowzieModel.getMowzieBone("tier4");
        MowzieGeoBone tier5 = mowzieModel.getMowzieBone("tier5");
        tier1.setChildrenHidden(true);
        tier2.setChildrenHidden(true);
        tier3.setChildrenHidden(true);
        tier4.setChildrenHidden(true);
        tier5.setChildrenHidden(true);
        if (tier == EntityGeomancyBase.GeomancyTier.NONE) tier1.setChildrenHidden(false);
        else if (tier == EntityGeomancyBase.GeomancyTier.SMALL) tier2.setChildrenHidden(false);
        else if (tier == EntityGeomancyBase.GeomancyTier.MEDIUM) tier3.setChildrenHidden(false);
        else if (tier == EntityGeomancyBase.GeomancyTier.LARGE) tier4.setChildrenHidden(false);
        else tier5.setChildrenHidden(false);
    }

    @Override
    public void render(EntityPillar pillar, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float height = pillar.prevPrevHeight + (pillar.prevHeight - pillar.prevPrevHeight) * partialTick;
        poseStack.translate(0, height - 0.5f, 0);

        int numRenders = (int) Math.ceil(pillar.getHeight()) + 1;
        for (int i = 0; i < numRenders; i++) {
            poseStack.translate(0, -1, 0);
            super.render(pillar, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public void renderRecursively(PoseStack poseStack, EntityPillar animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    @Override
    public void renderCube(PoseStack poseStack, GeoCube cube, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.translate(-0.5, 0.5f, -0.5);
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        blockrendererdispatcher.renderSingleBlock(getEntity().getBlock(), poseStack, getCurrentMultiBufferSource(), packedLight, packedOverlay);
    }
}
