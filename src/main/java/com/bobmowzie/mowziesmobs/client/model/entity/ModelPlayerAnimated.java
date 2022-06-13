package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.armor.MowzieElytraModel;
import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.google.common.collect.Lists;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ModelPlayerAnimated<T extends LivingEntity> extends PlayerModel<T> {
    private List<ModelPart> modelRenderers = Lists.newArrayList();

    public ModelPlayerAnimated(ModelPart root, boolean smallArmsIn) {
        super(root, smallArmsIn);
        this.body = new ModelPartMatrix(body);
        this.head = new ModelPartMatrix(head);
        this.rightArm = new ModelPartMatrix(rightArm);
        this.leftArm = new ModelPartMatrix(leftArm);
        this.rightLeg = new ModelPartMatrix(rightLeg);
        this.leftLeg = new ModelPartMatrix(leftLeg);

        this.hat = new ModelPartMatrix(hat);
        this.jacket = new ModelPartMatrix(jacket);
        this.leftSleeve = new ModelPartMatrix(leftSleeve);
        this.rightSleeve = new ModelPartMatrix(rightSleeve);
        this.leftPants = new ModelPartMatrix(leftPants);
        this.rightPants = new ModelPartMatrix(rightPants);
        this.ear = new ModelPartMatrix(ear);

        modelRenderers.add(ear);
        modelRenderers.add(cloak);
        if (smallArmsIn) {
            modelRenderers.add(leftArm);
            modelRenderers.add(rightArm);
            modelRenderers.add(leftSleeve);
            modelRenderers.add(rightSleeve);
        }
        else {
            modelRenderers.add(leftArm);
            modelRenderers.add(leftSleeve);
            modelRenderers.add(rightSleeve);
        }
        modelRenderers.add(leftLeg);
        modelRenderers.add(leftPants);
        modelRenderers.add(rightPants);
        modelRenderers.add(jacket);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
        this.ear.copyFrom(this.head);
    }

    @Override
    public ModelPart getRandomModelPart(Random randomIn) {
        return this.modelRenderers.get(randomIn.nextInt(this.modelRenderers.size()));
    }

    @Override
    public void copyPropertiesTo(EntityModel<T> p_217111_1_) {
        super.copyPropertiesTo(p_217111_1_);
        if (p_217111_1_ instanceof MowzieElytraModel) {
            MowzieElytraModel<?> elytraModel = (MowzieElytraModel<?>) p_217111_1_;
            elytraModel.bipedBody.copyFrom(this.body);
        }
    }

    @Override
    public void copyPropertiesTo(HumanoidModel<T> modelIn) {
        if (!(modelIn.body instanceof ModelPartMatrix)) {
            modelIn.head = new ModelPartMatrix(modelIn.head);
            modelIn.hat = new ModelPartMatrix(modelIn.hat);
            modelIn.body = new ModelPartMatrix(modelIn.body);
            modelIn.leftArm = new ModelPartMatrix(modelIn.leftArm);
            modelIn.rightArm = new ModelPartMatrix(modelIn.rightArm);
            modelIn.leftLeg = new ModelPartMatrix(modelIn.leftLeg);
            modelIn.rightLeg = new ModelPartMatrix(modelIn.rightLeg);
        }
        setUseMatrixMode(modelIn, true);
        super.copyPropertiesTo(modelIn);
    }

    public static void setUseMatrixMode(HumanoidModel<? extends LivingEntity> bipedModel, boolean useMatrixMode) {
        ModelBipedAnimated.setUseMatrixMode(bipedModel, useMatrixMode);
    }
}
