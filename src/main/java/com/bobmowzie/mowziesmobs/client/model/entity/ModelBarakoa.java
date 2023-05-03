package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelBarakoa extends MowzieAnimatedGeoModel<EntityBarakoa> {
    public ModelBarakoa() {
        super();
    }

    @Override
    public ResourceLocation getModelLocation(EntityBarakoa object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/barakoa.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBarakoa object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/barakoa.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityBarakoa object) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/barakoa.animation.json");
    }

    @Override
    public void codeAnimations(EntityBarakoa entity, Integer uniqueID, AnimationEvent<?> customPredicate) {
        IBone head = this.getBone("head");
        IBone neck = this.getBone("neck");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) / 2f);
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) / 2f);
        neck.setRotationX(neck.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) / 2f);
        neck.setRotationY(neck.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) / 2f);
    }
}