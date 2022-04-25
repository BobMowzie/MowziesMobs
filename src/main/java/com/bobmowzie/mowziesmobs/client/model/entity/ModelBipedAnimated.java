package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ModelRendererMatrix;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityAxeAttack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;

public class ModelBipedAnimated<T extends LivingEntity> extends HumanoidModel<T> {
    public ModelBipedAnimated(float modelSize) {
        super(modelSize);
        this.body = new ModelRendererMatrix(body);
        this.head = new ModelRendererMatrix(head);
        this.rightArm = new ModelRendererMatrix(rightArm);
        this.leftArm = new ModelRendererMatrix(leftArm);
        this.rightLeg = new ModelRendererMatrix(rightLeg);
        this.leftLeg = new ModelRendererMatrix(leftLeg);
    }

    public static void copyFromGeckoModel(HumanoidModel<?> bipedModel, ModelGeckoPlayerThirdPerson geckoModel) {
        ((ModelRendererMatrix)bipedModel.body).setWorldXform(geckoModel.bipedBody().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.body).setWorldNormal(geckoModel.bipedBody().getWorldSpaceNormal());
        
        ((ModelRendererMatrix)bipedModel.head).setWorldXform(geckoModel.bipedHead().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.head).setWorldNormal(geckoModel.bipedHead().getWorldSpaceNormal());

        ((ModelRendererMatrix)bipedModel.leftLeg).setWorldXform(geckoModel.bipedLeftLeg().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.leftLeg).setWorldNormal(geckoModel.bipedLeftLeg().getWorldSpaceNormal());

        ((ModelRendererMatrix)bipedModel.rightLeg).setWorldXform(geckoModel.bipedRightLeg().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.rightLeg).setWorldNormal(geckoModel.bipedRightLeg().getWorldSpaceNormal());

        ((ModelRendererMatrix)bipedModel.rightArm).setWorldXform(geckoModel.bipedRightArm().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.rightArm).setWorldNormal(geckoModel.bipedRightArm().getWorldSpaceNormal());

        ((ModelRendererMatrix)bipedModel.leftArm).setWorldXform(geckoModel.bipedLeftArm().getWorldSpaceXform());
        ((ModelRendererMatrix)bipedModel.leftArm).setWorldNormal(geckoModel.bipedLeftArm().getWorldSpaceNormal());
    }

    public static void setUseMatrixMode(HumanoidModel<? extends LivingEntity> bipedModel, boolean useMatrixMode) {
        ((ModelRendererMatrix)bipedModel.body).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.head).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.leftLeg).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.rightLeg).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.rightArm).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.leftArm).setUseMatrixMode(useMatrixMode);
    }
}
