package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ModelBipedAnimated extends BipedModel {
    public ModelBipedAnimated(float modelSize) {
        super(modelSize);
    }

    @Override
    public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (!(entityIn instanceof PlayerEntity)) {
            return;
        }
        float delta = MowziesMobs.PROXY.getPartialTicks();
        PlayerEntity player = (PlayerEntity) entityIn;
        doMowzieAnimations(player, this, delta);
    }

    public static void doMowzieAnimations(PlayerEntity player, BipedModel model, float delta) {
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null && playerCapability.getGeomancy().tunneling) {
            model.isSneak = false;
            Vec3d moveVec = player.getMotion();
            moveVec = moveVec.normalize();
            if (model instanceof PlayerModel) GlStateManager.rotatef(45 - 45 * (float)moveVec.y, 1.0F, 0.0F, 0.0F);

            float spin = 1f * (player.ticksExisted + delta);
            model.bipedHead.rotateAngleX = 1.57f * Math.min(0f, (float)moveVec.y);
            model.bipedHead.rotateAngleY = spin;
            model.bipedHead.rotateAngleZ = 0;

            model.bipedBody.rotateAngleX = 0;
            model.bipedBody.rotateAngleY = spin;
            model.bipedBody.rotateAngleZ = 0;

            model.bipedLeftArm.rotateAngleX = -3.14f;
            model.bipedLeftArm.rotateAngleY = spin;
            model.bipedLeftArm.rotateAngleZ = 0;
            model.bipedLeftArm.setRotationPoint(5 * (float)Math.sin(spin + Math.PI/2), 2, 5 * (float)Math.cos(spin + Math.PI/2));

            model.bipedRightArm.rotateAngleX = -3.14f;
            model.bipedRightArm.rotateAngleY = spin;
            model.bipedRightArm.rotateAngleZ = 0;
            model.bipedRightArm.setRotationPoint(-5 * (float)Math.sin(spin + Math.PI/2), 2, -5 * (float)Math.cos(spin + Math.PI/2));

            model.bipedLeftLeg.rotateAngleX = 0;
            model.bipedLeftLeg.rotateAngleY = spin;
            model.bipedLeftLeg.rotateAngleZ = 0;
            model.bipedLeftLeg.setRotationPoint(1.9F * (float)Math.sin(spin + Math.PI/2), 12.0F, 1.9F * (float)Math.cos(spin + Math.PI/2));

            model.bipedRightLeg.rotateAngleX = 0;
            model.bipedRightLeg.rotateAngleY = spin;
            model.bipedRightLeg.rotateAngleZ = 0;
            model.bipedRightLeg.setRotationPoint(-1.9F * (float)Math.sin(spin + Math.PI/2), 12.0F, -1.9f * (float)Math.cos(spin + Math.PI/2));
        }

        // Axe of a thousand metals attack animations
        if (playerCapability != null && playerCapability.getUntilAxeSwing() > 0) {
            float frame = (PlayerCapability.SWING_COOLDOWN - playerCapability.getUntilAxeSwing()) + delta;
            RendererModel arm = model.bipedRightArm;
            if (playerCapability.isVerticalSwing()) {
                float swingArc = 3f;
                arm.rotateAngleX = -2.7f + (float) (swingArc * 1 / (1 + Math.exp(1.3f * (-frame + EntityAxeAttack.SWING_DURATION_HOR / 2f))));
                arm.rotateAngleX = Math.min(arm.rotateAngleX, -0.1f);
                if (!model.isSneak) {
                    GlStateManager.translatef(0.0F, 0.3F, 0.0F);
                }
                model.isSneak = true;
            } else {
                float swingArc = 2.5f;
                arm.rotateAngleX = -1.75f + (float) (swingArc * 1 / (1 + Math.exp(1.3f * (-frame + EntityAxeAttack.SWING_DURATION_HOR / 2f))));
                arm.rotateAngleZ = 1.5f;
            }
        }
    }
}
