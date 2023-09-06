package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelBarako extends MowzieAnimatedGeoModel<EntityBarako> {
    public ModelBarako() {
        super();
    }

    @Override
    public ResourceLocation getModelLocation(EntityBarako object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/barako.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBarako object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/barako.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityBarako object) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/barako.animation.json");
    }

    @Override
    public void codeAnimations(EntityBarako entity, Integer uniqueID, AnimationEvent<?> customPredicate) {

    }
}