package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.client.model.armor.MowzieElytraModel;
import com.bobmowzie.mowziesmobs.client.model.tools.ModelRendererMatrix;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Random;

public class ModelPlayerAnimated<T extends LivingEntity> extends PlayerModel<T> {
    private List<ModelRenderer> modelRenderers = Lists.newArrayList();

    public ModelPlayerAnimated(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
        this.bipedBody = new ModelRendererMatrix(bipedBody);
        this.bipedHead = new ModelRendererMatrix(bipedHead);
        this.bipedRightArm = new ModelRendererMatrix(bipedRightArm);
        this.bipedLeftArm = new ModelRendererMatrix(bipedLeftArm);
        this.bipedRightLeg = new ModelRendererMatrix(bipedRightLeg);
        this.bipedLeftLeg = new ModelRendererMatrix(bipedLeftLeg);

        this.bipedHeadwear = new ModelRendererMatrix(bipedHeadwear);
        this.bipedBodyWear = new ModelRendererMatrix(bipedBodyWear);
        this.bipedLeftArmwear = new ModelRendererMatrix(bipedLeftArmwear);
        this.bipedRightArmwear = new ModelRendererMatrix(bipedRightArmwear);
        this.bipedLeftLegwear = new ModelRendererMatrix(bipedLeftLegwear);
        this.bipedRightLegwear = new ModelRendererMatrix(bipedRightLegwear);
        this.bipedDeadmau5Head = new ModelRendererMatrix(bipedDeadmau5Head);

        modelRenderers.add(bipedDeadmau5Head);
        modelRenderers.add(bipedCape);
        if (smallArmsIn) {
            modelRenderers.add(bipedLeftArm);
            modelRenderers.add(bipedRightArm);
            modelRenderers.add(bipedLeftArmwear);
            modelRenderers.add(bipedRightArmwear);
        }
        else {
            modelRenderers.add(bipedLeftArm);
            modelRenderers.add(bipedLeftArmwear);
            modelRenderers.add(bipedRightArmwear);
        }
        modelRenderers.add(bipedLeftLeg);
        modelRenderers.add(bipedLeftLegwear);
        modelRenderers.add(bipedRightLegwear);
        modelRenderers.add(bipedBodyWear);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);
        this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
        this.bipedLeftArmwear.copyModelAngles(this.bipedLeftArm);
        this.bipedRightArmwear.copyModelAngles(this.bipedRightArm);
        this.bipedBodyWear.copyModelAngles(this.bipedBody);
        this.bipedHeadwear.copyModelAngles(this.bipedHead);
        this.bipedDeadmau5Head.copyModelAngles(this.bipedHead);
    }

    @Override
    public ModelRenderer getRandomModelRenderer(Random randomIn) {
        return this.modelRenderers.get(randomIn.nextInt(this.modelRenderers.size()));
    }

    @Override
    public void copyModelAttributesTo(EntityModel<T> p_217111_1_) {
        super.copyModelAttributesTo(p_217111_1_);
        if (p_217111_1_ instanceof MowzieElytraModel) {
            MowzieElytraModel<?> elytraModel = (MowzieElytraModel<?>) p_217111_1_;
            elytraModel.bipedBody.copyModelAngles(this.bipedBody);
        }
    }

    @Override
    public void setModelAttributes(BipedModel<T> modelIn) {
        if (!(modelIn.bipedBody instanceof ModelRendererMatrix)) {
            modelIn.bipedHead = new ModelRendererMatrix(modelIn.bipedHead);
            modelIn.bipedHeadwear = new ModelRendererMatrix(modelIn.bipedHeadwear);
            modelIn.bipedBody = new ModelRendererMatrix(modelIn.bipedBody);
            modelIn.bipedLeftArm = new ModelRendererMatrix(modelIn.bipedLeftArm);
            modelIn.bipedRightArm = new ModelRendererMatrix(modelIn.bipedRightArm);
            modelIn.bipedLeftLeg = new ModelRendererMatrix(modelIn.bipedLeftLeg);
            modelIn.bipedRightLeg = new ModelRendererMatrix(modelIn.bipedRightLeg);
        }
        super.setModelAttributes(modelIn);
    }
}
