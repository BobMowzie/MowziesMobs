package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ModelRendererMatrix;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;

public class ModelBipedAnimated<T extends LivingEntity> extends BipedModel<T> {
    public ModelBipedAnimated(float modelSize) {
        super(modelSize);
        this.bipedBody = new ModelRendererMatrix(bipedBody);
        this.bipedHead = new ModelRendererMatrix(bipedHead);
        this.bipedRightArm = new ModelRendererMatrix(bipedRightArm);
        this.bipedLeftArm = new ModelRendererMatrix(bipedLeftArm);
        this.bipedRightLeg = new ModelRendererMatrix(bipedRightLeg);
        this.bipedLeftLeg = new ModelRendererMatrix(bipedLeftLeg);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (!(entityIn instanceof PlayerEntity)) {
            return;
        }
        float delta = Minecraft.getInstance().getRenderPartialTicks();
        PlayerEntity player = (PlayerEntity) entityIn;
        doMowzieAnimations(player, this, delta);
    }

    public static void copyFromGeckoModel(BipedModel<?> bipedModel, ModelGeckoPlayerThirdPerson geckoModel) {
        ((ModelRendererMatrix)bipedModel.bipedBody).setWorldXform(geckoModel.bipedBody().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.bipedBody).setWorldNormal(geckoModel.bipedBody().getWorldSpaceNormal());
        
        ((ModelRendererMatrix)bipedModel.bipedHead).setWorldXform(geckoModel.bipedHead().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.bipedHead).setWorldNormal(geckoModel.bipedHead().getWorldSpaceNormal());

        ((ModelRendererMatrix)bipedModel.bipedLeftLeg).setWorldXform(geckoModel.bipedLeftLeg().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.bipedLeftLeg).setWorldNormal(geckoModel.bipedLeftLeg().getWorldSpaceNormal());

        ((ModelRendererMatrix)bipedModel.bipedRightLeg).setWorldXform(geckoModel.bipedRightLeg().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.bipedRightLeg).setWorldNormal(geckoModel.bipedRightLeg().getWorldSpaceNormal());

        ((ModelRendererMatrix)bipedModel.bipedRightArm).setWorldXform(geckoModel.bipedRightArm().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.bipedRightArm).setWorldNormal(geckoModel.bipedRightArm().getWorldSpaceNormal());

        ((ModelRendererMatrix)bipedModel.bipedLeftArm).setWorldXform(geckoModel.bipedLeftArm().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.bipedLeftArm).setWorldNormal(geckoModel.bipedLeftArm().getWorldSpaceNormal());
    }

    public static void doMowzieAnimations(PlayerEntity player, BipedModel<?> model, float delta) {
        PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
        if (playerCapability != null && playerCapability.getGeomancy().tunneling) {
            model.isSneak = false;
            Vector3d moveVec = player.getMotion();
            moveVec = moveVec.normalize();
//            if (model instanceof PlayerModel) RenderSystem.rotatef(45 - 45 * (float)moveVec.y, 1.0F, 0.0F, 0.0F);

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
            float frame = (30 - playerCapability.getUntilAxeSwing()) + delta;
            boolean rightHanded = player.getPrimaryHand() == HandSide.RIGHT;
            ModelRenderer arm = rightHanded ? model.bipedRightArm : model.bipedLeftArm;
            float handInvert = rightHanded ? 1 : -1;
            if (playerCapability.isVerticalSwing()) {
                float swingArc = 3f;
                arm.rotateAngleX = -2.7f + (float) (swingArc * 1 / (1 + Math.exp(1.3f * (-frame + EntityAxeAttack.SWING_DURATION_HOR / 2f))));
                arm.rotateAngleX = Math.min(arm.rotateAngleX, -0.1f);
                model.isSneak = true;
            } else {
                float swingArc = 2.5f;
                arm.rotateAngleX = -1.75f + (float) (swingArc * 1 / (1 + Math.exp(1.3f * (-frame + EntityAxeAttack.SWING_DURATION_HOR / 2f))));
                arm.rotateAngleZ = handInvert * 1.5f;
            }
        }
    }
}
