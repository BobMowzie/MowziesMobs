package com.bobmowzie.mowziesmobs.client.model.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.BoneInfo;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.HashMap;
import java.util.Map;

public class ModelSculptor extends MowzieAnimatedGeoModel<EntitySculptor> {
    public ModelSculptor() {
        super();
        trackBone("body");
        trackBone("chest");
        trackBone("thighR");
        trackBone("calfR");
        trackBone("thighL");
        trackBone("calfL");
        trackBone("footR");
    }

    @Override
    public ResourceLocation getModelLocation(EntitySculptor object) {
        return new ResourceLocation(MowziesMobs.MODID, "geo/sculptor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySculptor object) {
        return new ResourceLocation(MowziesMobs.MODID, "textures/entity/sculptor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySculptor object) {
        return new ResourceLocation(MowziesMobs.MODID, "animations/sculptor.animation.json");
    }

    @Override
    public void setLivingAnimations(EntitySculptor entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getBone("head");
        IBone handClosedL = this.getBone("handClosedL");
        IBone handClosedR = this.getBone("handClosedR");
        IBone handOpenL = this.getBone("handOpenL");
        IBone handOpenR = this.getBone("handOpenR");
        IBone frontCloth = this.getBone("clothFront");
        IBone frontCloth2 = this.getBone("clothFront2");
        IBone backCloth = this.getBone("clothBack");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F));

        handClosedL.setHidden(entity.handLOpen);
        handClosedR.setHidden(entity.handROpen);
        handOpenL.setHidden(!entity.handLOpen);
        handOpenR.setHidden(!entity.handROpen);

        frontCloth.setHidden(true);
        frontCloth2.setHidden(true);
        backCloth.setHidden(true);

        beadsCorrections(entity);
        skirtCorrections(entity);
    }

    private void beadsCorrections(EntitySculptor entity) {
        Map<IBone, Vector3d> beadsDirections = new HashMap<>();
        beadsDirections.put(this.getBone("bead1"), new Vector3d(0, -1, 0));
        beadsDirections.put(this.getBone("bead2"), new Vector3d(0, -1, 0));
        beadsDirections.put(this.getBone("bead3"), new Vector3d(0, -1, 0));
        beadsDirections.put(this.getBone("bead4"), new Vector3d(0, -0.5, 0.25));
        beadsDirections.put(this.getBone("bead5"), new Vector3d(0, -0.5, 0.25));
        beadsDirections.put(this.getBone("bead6"), new Vector3d(0, -0.5, 1));
        beadsDirections.put(this.getBone("bead7"), new Vector3d(0, -0.5, 1));
        beadsDirections.put(this.getBone("bead8"), new Vector3d(0, -0.25, 0.75));
        beadsDirections.put(this.getBone("bead9"), new Vector3d(0, -0.25, 0.75));

        IBone headJoint = this.getBone("head_joint");
        Vector3d headPos = new Vector3d(headJoint.getPivotX(), headJoint.getPivotY(), headJoint.getPivotZ());
        IBone head = this.getBone("head");
        Vector3d headDir = new Vector3d(0, 0, -1).normalize();
        headDir = headDir.rotateYaw(head.getRotationY()).rotatePitch(head.getRotationX());
        for (Map.Entry<IBone, Vector3d> beadDir : beadsDirections.entrySet()) {
            IBone bead = beadDir.getKey();
            Vector3d beadPos = new Vector3d(bead.getPivotX(), bead.getPivotY(), bead.getPivotZ());
            Vector3d dir = beadPos.subtract(headPos).normalize().mul(1, -1, 1);
            double dot = dir.dotProduct(headDir);
            dot = Math.pow(Math.max(dot, 0), 3.5);
            Vector3d moveDir = beadDir.getValue().normalize();
            addPosition(bead, moveDir.scale(dot * 3));
        }
        head.setHidden(false);
    }

    private void skirtCorrections(EntitySculptor entity) {
        IBone headJoint = this.getBone("head_joint");
        IBone skirtM = this.getBone("skirtM");
        IBone skirtL = this.getBone("skirtL");
        IBone skirtR = this.getBone("skirtR");
        GeoBone thighR = (GeoBone) this.getBone("thighR");
        GeoBone calfR = (GeoBone) this.getBone("calfR");
        GeoBone thighL = (GeoBone) this.getBone("thighL");
        GeoBone calfL = (GeoBone) this.getBone("calfL");
        GeoBone footR = (GeoBone) this.getBone("footR");
        headJoint.setHidden(true);
        Vector3d thighToKneeR = getModelPosition(calfR).subtract(getModelPosition(thighR)).mul(0, 1, 1).normalize();
        Vector3d thighToKneeL = getModelPosition(calfL).subtract(getModelPosition(thighL)).mul(0, 1, 1).normalize();
        double angleR = MathHelper.atan2(thighToKneeR.getY(), thighToKneeR.getZ());
        double angleL = MathHelper.atan2(thighToKneeL.getY(), thighToKneeL.getZ());
        skirtM.setRotationX((float) (skirtM.getRotationX() - (angleR + angleL) / 2f) - 1.0f);

        IBone test = this.getBone("test");
        setModelPosition(test, getModelPosition(thighR));
        IBone test2 = this.getBone("test2");
        setModelPosition(test2, getModelPosition(calfR));
        IBone test3 = this.getBone("test3");
        setModelPosition(test3, getModelPosition(footR));
        System.out.println(getModelPosition(getBone("body")));
    }

    protected void addRotOffsetFromBone(IBone source, IBone target) {
        target.setRotationX(target.getInitialSnapshot().rotationValueX + source.getRotationX() - source.getInitialSnapshot().rotationValueX);
        target.setRotationY(target.getInitialSnapshot().rotationValueY + source.getRotationY() - source.getInitialSnapshot().rotationValueY);
        target.setRotationZ(target.getInitialSnapshot().rotationValueZ + source.getRotationZ() - source.getInitialSnapshot().rotationValueZ);
    }

    protected void addPosition(IBone bone, Vector3d vec) {
        bone.setPositionX((float) (bone.getPositionX() + vec.getX()));
        bone.setPositionY((float) (bone.getPositionY() + vec.getY()));
        bone.setPositionZ((float) (bone.getPositionZ() + vec.getZ()));
    }

    protected Vector3d getModelPosition(IBone bone) {
        if (bone == null) {
            System.out.println("Bone is null!");
            return new Vector3d(0, 0, 0);
        }
        String boneName = bone.getName();
        BoneInfo boneInfo = boneInfoMap.get(boneName);
        Matrix4f matrix = boneInfo.modelSpaceXform;

        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix);
        return new Vector3d(vec.getX() * 16f, vec.getY() * 16f, vec.getZ() * 16f);
    }

    protected void setModelPosition(IBone bone, Vector3d vec) {
//        Matrix4f matrix = boneInfoMap.get(bone.getName()).modelSpaceXform;

        bone.setPositionX((float) (vec.getX()));
        bone.setPositionY((float) (vec.getY()));
        bone.setPositionZ((float) (vec.getZ()));
    }
}