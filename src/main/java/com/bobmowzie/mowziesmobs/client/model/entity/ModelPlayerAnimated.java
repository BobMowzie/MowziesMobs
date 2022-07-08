package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.armor.MowzieElytraModel;
import com.bobmowzie.mowziesmobs.client.model.tools.ModelPartMatrix;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class ModelPlayerAnimated<T extends LivingEntity> extends PlayerModel<T> {
    private final List<ModelPart> parts;

    public ModelPlayerAnimated(ModelPart root, boolean smallArmsIn) {
        super(root, smallArmsIn);
        ModelPartMatrix bodyMatrix = new ModelPartMatrix(body);
        ModelPartMatrix headMatrix = new ModelPartMatrix(head);
        ModelPartMatrix rightArmMatrix = new ModelPartMatrix(rightArm);
        ModelPartMatrix leftArmMatrix = new ModelPartMatrix(leftArm);
        ModelPartMatrix rightLegMatrix = new ModelPartMatrix(rightLeg);
        ModelPartMatrix leftLegMatrix = new ModelPartMatrix(leftLeg);

        ModelPartMatrix hatMatrix = new ModelPartMatrix(hat);
        ModelPartMatrix jacketMatrix = new ModelPartMatrix(jacket);
        ModelPartMatrix leftSleeveMatrix = new ModelPartMatrix(leftSleeve);
        ModelPartMatrix rightSleeveMatrix = new ModelPartMatrix(rightSleeve);
        ModelPartMatrix leftPantsMatrix = new ModelPartMatrix(leftPants);
        ModelPartMatrix rightPantsMatrix = new ModelPartMatrix(rightPants);
        ModelPartMatrix earMatrix = new ModelPartMatrix(ear);
        ModelPartMatrix cloakMatrix = new ModelPartMatrix(cloak);

        Map<ModelPart, ModelPart> origToNew = new HashMap<>();
        origToNew.put(body, bodyMatrix);
        origToNew.put(head, headMatrix);
        origToNew.put(rightArm, rightArmMatrix);
        origToNew.put(leftArm, leftArmMatrix);
        origToNew.put(rightLeg, rightLegMatrix);
        origToNew.put(leftLeg, leftLegMatrix);

        origToNew.put(hat, hatMatrix);
        origToNew.put(jacket, jacketMatrix);
        origToNew.put(leftSleeve, leftSleeveMatrix);
        origToNew.put(rightSleeve, rightSleeveMatrix);
        origToNew.put(leftPants, leftPantsMatrix);
        origToNew.put(rightPants, rightPantsMatrix);
        origToNew.put(ear, earMatrix);
        origToNew.put(cloak, cloakMatrix);
        
        this.body = bodyMatrix;
        this.head = headMatrix;
        this.rightArm = rightArmMatrix;
        this.leftArm = leftArmMatrix;
        this.rightLeg = rightLegMatrix;
        this.leftLeg = leftLegMatrix;

        this.hat = hatMatrix;
        this.jacket = jacketMatrix;
        this.leftSleeve = leftSleeveMatrix;
        this.rightSleeve = rightSleeveMatrix;
        this.leftPants = leftPantsMatrix;
        this.rightPants = rightPantsMatrix;
        this.ear = earMatrix;
        this.cloak = cloakMatrix;
        
        List<ModelPart> originalList = root.getAllParts().filter((p_170824_) -> {
            return !p_170824_.isEmpty();
        }).collect(ImmutableList.toImmutableList());

        this.parts = new ArrayList<>();
        for (ModelPart origPart : originalList) {
            ModelPart newPart = origToNew.get(origPart);
            if (newPart != null) this.parts.add(newPart);
        }
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
        return this.parts.get(randomIn.nextInt(this.parts.size()));
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
        ModelBipedAnimated.setUseMatrixMode(modelIn, true);
        super.copyPropertiesTo(modelIn);
    }

    public static void setUseMatrixMode(PlayerModel<? extends LivingEntity> bipedModel, boolean useMatrixMode) {
        ((ModelPartMatrix)bipedModel.hat).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.jacket).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.leftPants).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.rightPants).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.rightSleeve).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.leftSleeve).setUseMatrixMode(useMatrixMode);
        ((ModelPartMatrix)bipedModel.ear).setUseMatrixMode(useMatrixMode);
        ModelBipedAnimated.setUseMatrixMode(bipedModel, useMatrixMode);
    }
}
