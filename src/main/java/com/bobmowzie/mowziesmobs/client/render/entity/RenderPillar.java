package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelPillar;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.util.RenderUtils;

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
    public void renderEarly(EntityPillar animatable, PoseStack stackIn, float partialTicks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.renderEarly(animatable, stackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        EntityGeomancyBase.GeomancyTier tier = getEntity().getTier();
        MowzieAnimatedGeoModel<EntityPillar> model = (MowzieAnimatedGeoModel<EntityPillar>) getGeoModelProvider();
        MowzieGeoBone tier1 = model.getMowzieBone("tier1");
        MowzieGeoBone tier2 = model.getMowzieBone("tier2");
        MowzieGeoBone tier3 = model.getMowzieBone("tier3");
        MowzieGeoBone tier4 = model.getMowzieBone("tier4");
        MowzieGeoBone tier5 = model.getMowzieBone("tier5");
        tier1.hideChildBonesToo = true;
        tier2.hideChildBonesToo = true;
        tier3.hideChildBonesToo = true;
        tier4.hideChildBonesToo = true;
        tier5.hideChildBonesToo = true;
        if (tier == EntityGeomancyBase.GeomancyTier.NONE) tier1.hideChildBonesToo = false;
        else if (tier == EntityGeomancyBase.GeomancyTier.SMALL) tier2.hideChildBonesToo = false;
        else if (tier == EntityGeomancyBase.GeomancyTier.MEDIUM) tier3.hideChildBonesToo = false;
        else if (tier == EntityGeomancyBase.GeomancyTier.LARGE) tier4.hideChildBonesToo = false;
        else tier5.hideChildBonesToo = false;
    }

    @Override
    public void render(GeoModel model, EntityPillar pillar, float partialTicks, RenderType type, PoseStack matrixStackIn, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        float height = pillar.prevPrevHeight + (pillar.prevHeight - pillar.prevPrevHeight) * partialTicks;
        matrixStackIn.translate(0, height - 0.5f, 0);

        int numRenders = (int) Math.ceil(pillar.getHeight()) + 1;
        for (int i = 0; i < numRenders; i++) {
            matrixStackIn.translate(0, -1, 0);
            super.render(model, pillar, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        renderCubesOfBone(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderChildBones(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    @Override
    public void renderCube(GeoCube cube, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        stack.translate(-0.5, 0.5f, -0.5);
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        blockrendererdispatcher.renderSingleBlock(getEntity().getBlock(), stack, getCurrentRTB(), packedLightIn, packedOverlayIn);
    }
}
