package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFrostmaw;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.ItemLayer;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * Created by BobMowzie on 5/8/2017.
 */
public class RenderFrostmaw extends MobRenderer<EntityFrostmaw, ModelFrostmaw<EntityFrostmaw>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/frostmaw.png");

    public RenderFrostmaw(EntityRendererProvider.Context mgr) {
        super(mgr, new ModelFrostmaw<>(), 3.5f);
        addLayer(new ItemLayer<>(this, getModel().iceCrystalHand, ItemHandler.ICE_CRYSTAL.get().getDefaultInstance(), ItemTransforms.TransformType.GROUND));
        addLayer(new ItemLayer<>(this, getModel().iceCrystal, ItemHandler.ICE_CRYSTAL.get().getDefaultInstance(), ItemTransforms.TransformType.GROUND));
    }

    @Override
    protected float getFlipDegrees(EntityFrostmaw entity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFrostmaw entity) {
        return RenderFrostmaw.TEXTURE;
    }

    @Override
    public void render(EntityFrostmaw entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (entity.getAnimation() == EntityFrostmaw.SWIPE_ANIMATION || entity.getAnimation() == EntityFrostmaw.SWIPE_TWICE_ANIMATION || entity.getAnimation() == EntityFrostmaw.ICE_BREATH_ANIMATION || entity.getAnimation() == EntityFrostmaw.ICE_BALL_ANIMATION || !entity.getActive()) {
            Vec3 rightHandPos = MowzieRenderUtils.getWorldPosFromModel(entity, entityYaw, model.rightHandSocket);
            Vec3 leftHandPos = MowzieRenderUtils.getWorldPosFromModel(entity, entityYaw, model.leftHandSocket);
            Vec3 mouthPos = MowzieRenderUtils.getWorldPosFromModel(entity, entityYaw, model.mouthSocket);
            entity.setSocketPosArray(0, rightHandPos);
            entity.setSocketPosArray(1, leftHandPos);
            entity.setSocketPosArray(2, mouthPos);
        }
    }
}
