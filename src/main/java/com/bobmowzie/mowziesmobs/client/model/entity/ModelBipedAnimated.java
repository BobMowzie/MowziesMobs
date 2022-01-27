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

    public static void setUseMatrixMode(BipedModel<? extends LivingEntity> bipedModel, boolean useMatrixMode) {
        ((ModelRendererMatrix)bipedModel.bipedBody).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.bipedHead).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.bipedLeftLeg).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.bipedRightLeg).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.bipedRightArm).setUseMatrixMode(useMatrixMode);
        ((ModelRendererMatrix)bipedModel.bipedLeftArm).setUseMatrixMode(useMatrixMode);
    }
}
