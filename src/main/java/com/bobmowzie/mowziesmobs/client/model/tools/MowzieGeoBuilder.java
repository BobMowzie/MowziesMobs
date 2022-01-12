package com.bobmowzie.mowziesmobs.client.model.tools;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import net.minecraft.world.phys.Vector3f;
import org.apache.commons.lang3.ArrayUtils;
import software.bernie.geckolib3.geo.raw.pojo.Bone;
import software.bernie.geckolib3.geo.raw.pojo.Cube;
import software.bernie.geckolib3.geo.raw.pojo.ModelProperties;
import software.bernie.geckolib3.geo.raw.tree.RawBoneGroup;
import software.bernie.geckolib3.geo.render.GeoBuilder;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.util.VectorUtils;

public class MowzieGeoBuilder extends GeoBuilder {

    @Override
    public GeoBone constructBone(RawBoneGroup bone, ModelProperties properties, GeoBone parent) {
        MowzieGeoBone geoBone = new MowzieGeoBone();

        Bone rawBone = bone.selfBone;
        Vector3f rotation = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(rawBone.getRotation()));
        Vector3f pivot = VectorUtils.convertDoubleToFloat(VectorUtils.fromArray(rawBone.getPivot()));
        rotation.mul(-1, -1, 1);

        geoBone.mirror = rawBone.getMirror();
        geoBone.dontRender = rawBone.getNeverRender();
        geoBone.reset = rawBone.getReset();
        geoBone.inflate = rawBone.getInflate();
        geoBone.parent = parent;
        geoBone.setModelRendererName(rawBone.getName());

        geoBone.setRotationX((float) Math.toRadians(rotation.getX()));
        geoBone.setRotationY((float) Math.toRadians(rotation.getY()));
        geoBone.setRotationZ((float) Math.toRadians(rotation.getZ()));

        geoBone.rotationPointX = -pivot.getX();
        geoBone.rotationPointY = pivot.getY();
        geoBone.rotationPointZ = pivot.getZ();

        if (!ArrayUtils.isEmpty(rawBone.getCubes())) {
            for (Cube cube : rawBone.getCubes()) {
                geoBone.childCubes.add(GeoCube.createFromPojoCube(cube, properties,
                        geoBone.inflate == null ? null : geoBone.inflate / 16, geoBone.mirror));
            }
        }

        for (RawBoneGroup child : bone.children.values()) {
            geoBone.childBones.add(constructBone(child, properties, geoBone));
        }

        return geoBone;
    }
}
