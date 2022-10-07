package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.RigUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityPillar;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.HashMap;
import java.util.Map;

public class ModelPillar extends MowzieAnimatedGeoModel<EntityPillar> {
    public ModelPillar() {
        super();
    }

    @Override
    public ResourceLocation getModelLocation(EntityPillar object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/geomancy_pillar.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPillar object) {
        return null;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityPillar object) {
        return null;
    }
}