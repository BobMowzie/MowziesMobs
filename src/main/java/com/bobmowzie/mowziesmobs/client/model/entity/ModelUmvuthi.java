package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Triple;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.ArrayList;
import java.util.List;

public class ModelUmvuthi extends MowzieAnimatedGeoModel<EntityUmvuthi> {
    public ModelUmvuthi() {
        super();
    }

    @Override
    public ResourceLocation getModelLocation(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/umvuthi.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/umvuthi_3.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityUmvuthi object) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/umvuthi.animation.json");
    }

    @Override
    public void codeAnimations(EntityUmvuthi entity, Integer uniqueID, AnimationEvent<?> customPredicate) {
        float frame = entity.frame + customPredicate.getPartialTick();

        if (entity.isAlive()) {
            MowzieGeoBone neck1 = getMowzieBone("neck");
            MowzieGeoBone neck2 = getMowzieBone("neck2");
            MowzieGeoBone headJoint = getMowzieBone("headJoint");
            MowzieGeoBone head = getMowzieBone("head");
            MowzieGeoBone[] lookPieces = new MowzieGeoBone[] { neck1, neck2, headJoint, head };
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            float headYaw = Mth.wrapDegrees(extraData.netHeadYaw);
            float headPitch = Mth.wrapDegrees(extraData.headPitch);
            for (MowzieGeoBone bone : lookPieces) {
                bone.addRotationX(headPitch * ((float) Math.PI / 180F) / (float) lookPieces.length);
                bone.addRotationY(headYaw * ((float) Math.PI / 180F) / (float) lookPieces.length);
            }
        }

        MowzieGeoBone rightThigh = getMowzieBone("rightThigh");
        MowzieGeoBone leftThigh = getMowzieBone("leftThigh");

        float liftLegs = entity.legsUp.getAnimationProgressSinSqrt(customPredicate.getPartialTick());
        leftThigh.addRotationX(1f * liftLegs);
        rightThigh.addRotationX(1f * liftLegs);
        leftThigh.addRotationZ(-1.5f * liftLegs);
        rightThigh.addRotationZ(1.5f * liftLegs);
        leftThigh.addRotationY(0.5f * liftLegs);
        rightThigh.addRotationY(-0.5f * liftLegs);

        float bellyBounceControl = getControllerValue("bellyBounceController");
        float jiggleSpeed = 2.5f;
        float jiggleScale = (float) (bellyBounceControl * 0.1 * Math.cos(jiggleSpeed * frame));
        MowzieGeoBone stomach = getMowzieBone("stomach");
        MowzieGeoBone chest = getMowzieBone("chest");
        MowzieGeoBone tail = getMowzieBone("tail");
        stomach.setScale(stomach.getScaleX() + jiggleScale, stomach.getScaleY() + jiggleScale, stomach.getScaleZ() + jiggleScale);
        chest.addPositionY(jiggleScale * 3);
        leftThigh.addPositionX(-jiggleScale * 5);
        rightThigh.addPositionX(jiggleScale * 5);
        tail.addPositionZ(jiggleScale * 4);

        float featherShakeControl = getControllerValue("featherShakeController");
        List<Triple<MowzieGeoBone, Direction.Axis, Boolean>> feathers = new ArrayList<>();
        feathers.add(Triple.of(getMowzieBone("neckFeathersFront1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersFront2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersFront3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersFront4"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack1"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack2"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack3"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack4"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersBack5"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft1"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft2"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft3"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft4"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersLeft5"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight1"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight2"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight3"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight4"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("neckFeathersRight5"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersFront1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersFront2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersFront3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersLeft1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersLeft2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersLeft3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersRight1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersRight2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestFeathersRight3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersFront1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersFront2"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersFront3"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight3"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight4"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersRight5"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft1"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft2"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft3"), Direction.Axis.X, true));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft4"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("bellyFeathersLeft5"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestDraperyFront"), Direction.Axis.X, false));
        feathers.add(Triple.of(getMowzieBone("chestDraperyRight"), Direction.Axis.Z, false));
        feathers.add(Triple.of(getMowzieBone("chestDraperyLeft"), Direction.Axis.Z, true));
        feathers.add(Triple.of(getMowzieBone("chestDraperyBack"), Direction.Axis.X, true));

        for (Triple<MowzieGeoBone, Direction.Axis, Boolean> feather : feathers) {
            MowzieGeoBone bone = feather.getLeft();
            float oscillation = (float) (featherShakeControl * 0.13 * Math.cos(1.4 * frame + bone.getPivotY() * -0.15 + bone.getPivotZ() * -0.1));
            if (feather.getRight()) oscillation *= -1;
            Direction.Axis axis = feather.getMiddle();
            if (axis == Direction.Axis.X) {
                bone.addRotationX(oscillation);
            }
            else if (axis == Direction.Axis.Y) {
                bone.addRotationY(oscillation);
            }
            else {
                bone.addRotationZ(oscillation);
            }
        }


    }
}