package com.bobmowzie.mowziesmobs.client.model.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ModelPlayerAnimated<T extends LivingEntity> extends PlayerModel<T> {
    public ModelPlayerAnimated(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (!(entityIn instanceof PlayerEntity)) {
            return;
        }
        float delta = Minecraft.getInstance().getRenderPartialTicks();
        PlayerEntity player = (PlayerEntity) entityIn;
        ModelBipedAnimated.doMowzieAnimations(player, this, delta);
        this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);
        this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
        this.bipedLeftArmwear.copyModelAngles(this.bipedLeftArm);
        this.bipedRightArmwear.copyModelAngles(this.bipedRightArm);
        this.bipedBodyWear.copyModelAngles(this.bipedBody);
        this.bipedHeadwear.copyModelAngles(this.bipedHead);
    }
}
