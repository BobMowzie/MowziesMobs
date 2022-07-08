package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class ModelBipedAnimated<T extends LivingEntity> extends HumanoidModel<T> {
    public ModelBipedAnimated(ModelPart root) {
        super(root);
        this.body = new ModelPartMatrix(body);
        this.head = new ModelPartMatrix(head);
        this.rightArm = new ModelPartMatrix(rightArm);
        this.leftArm = new ModelPartMatrix(leftArm);
        this.rightLeg = new ModelPartMatrix(rightLeg);
        this.leftLeg = new ModelPartMatrix(leftLeg);
    }

    public static void copyFromGeckoModel(HumanoidModel<?> bipedModel, ModelGeckoPlayerThirdPerson geckoModel) {
        ((ModelPartMatrix)bipedModel.body).setWorldXform(geckoModel.bipedBody().getWorldSpaceXform());
        ((ModelPartMatrix)bipedModel.body).setWorldNormal(geckoModel.bipedBody().getWorldSpaceNormal());
        
        ((ModelPartMatrix)bipedModel.head).setWorldXform(geckoModel.bipedHead().getWorldSpaceXform());
        ((ModelPartMatrix)bipedModel.head).setWorldNormal(geckoModel.bipedHead().getWorldSpaceNormal());

        ((ModelPartMatrix)bipedModel.leftLeg).setWorldXform(geckoModel.bipedLeftLeg().getWorldSpaceXform());
        ((ModelPartMatrix)bipedModel.leftLeg).setWorldNormal(geckoModel.bipedLeftLeg().getWorldSpaceNormal());

        ((ModelPartMatrix)bipedModel.rightLeg).setWorldXform(geckoModel.bipedRightLeg().getWorldSpaceXform());
        ((ModelPartMatrix)bipedModel.rightLeg).setWorldNormal(geckoModel.bipedRightLeg().getWorldSpaceNormal());

        ((ModelPartMatrix)bipedModel.rightArm).setWorldXform(geckoModel.bipedRightArm().getWorldSpaceXform());
        ((ModelPartMatrix)bipedModel.rightArm).setWorldNormal(geckoModel.bipedRightArm().getWorldSpaceNormal());

        ((ModelPartMatrix)bipedModel.leftArm).setWorldXform(geckoModel.bipedLeftArm().getWorldSpaceXform());
        ((ModelPartMatrix)bipedModel.leftArm).setWorldNormal(geckoModel.bipedLeftArm().getWorldSpaceNormal());
    }

    public static void setUseMatrixMode(HumanoidModel<? extends LivingEntity> bipedModel, boolean useMatrixMode) {
        ((ModelPartMatrix)bipedModel.body).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.head).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.leftLeg).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.rightLeg).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.rightArm).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.leftArm).setUseMatrixMode(useMatrixMode);
    }
}
