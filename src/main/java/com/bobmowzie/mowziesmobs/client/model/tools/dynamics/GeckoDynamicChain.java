package com.bobmowzie.mowziesmobs.client.model.tools.dynamics;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.*;

import java.beans.FeatureDescriptor;
import java.lang.Math;

/**
 * Created by BobMowzie on 8/30/2018.
 */
public class GeckoDynamicChain {
    public Vec3[] p;
    public Vec3[] p0;
    private Vec3[] v;
    private Vec3[] a;
    private float[] m;
    private float[] d;
    private final Entity entity;

    public Vec3[] renderPos;
    public Vec3[] prevRenderPos;

    public Vec3[] pOrig;
    private int prevUpdateTick;

    private float prevUpdateTime;

    public GeckoDynamicChain(Entity entity) {
        this.entity = entity;
        p = new Vec3[0];
        p0 = new Vec3[0];
        v = new Vec3[0];
        a = new Vec3[0];
        m = new float[0];
        d = new float[0];
        pOrig = new Vec3[0];
        renderPos = new Vec3[0];
        prevRenderPos = new Vec3[0];
        prevUpdateTick = -1;
        prevUpdateTime = 0;
    }

    public void updateSpringConstraint(float gravityAmount, float dampAmount, float stiffness, float maxForce, boolean doAttract, float attractFalloff, int numUpdates, float deltaTime) {
        float deltaTimePerUpdate = deltaTime / (float)numUpdates;
        for (int j = 0; j < numUpdates; j++) {
            p[0] = p0[0].add(pOrig[0].subtract(p0[0]).scale((double)(j + 1) / (double) (numUpdates)));
            for (int i = 1; i < p.length; i++) {
                Vec3 prevPosition = new Vec3(p[i].x, p[i].y, p[i].z);

                Vec3 force = new Vec3(0, 0, 0);
                Vec3 gravity = new Vec3(0, -gravityAmount, 0);
                force = force.add(gravity);
                if (doAttract) {
                    Vec3 attract = pOrig[0].subtract(p[i]);
                    force = force.add(attract.scale(1 / (1 + i * i * attractFalloff)));
                }
                a[i] = force.scale(1 / m[i]);
                p[i] = p[i].add((p[i].subtract(p0[i])).scale(1.0 - dampAmount)).add(a[i].scale(deltaTimePerUpdate * deltaTimePerUpdate).scale(1.0 - dampAmount));

                Vec3 vectorToPrevious;
                vectorToPrevious = p[i].subtract(p[i - 1]);
                vectorToPrevious = vectorToPrevious.normalize().scale(d[i]);
                p[i] = p[i - 1].add(vectorToPrevious);

                p0[i] = new Vec3(prevPosition.x, prevPosition.y, prevPosition.z);
            }
        }

        p0[0] = new Vec3(pOrig[0].x, pOrig[0].y, pOrig[0].z);
    }

    public void setChain(MowzieGeoBone[] chainOrig, MowzieGeoBone[] chainDynamic) {
        if (p.length != chainOrig.length || Double.isNaN(p[0].x)) {
            p = new Vec3[chainOrig.length];
            p0 = new Vec3[chainOrig.length];
            v = new Vec3[chainOrig.length];
            a = new Vec3[chainOrig.length];
            m = new float[chainOrig.length];
            d = new float[chainOrig.length];
            pOrig = new Vec3[chainOrig.length];
            renderPos = new Vec3[chainOrig.length];
            prevRenderPos = new Vec3[chainOrig.length];
            for (int i = 0; i < chainOrig.length; i++) {
                Vector3d pos = chainOrig[i].getWorldPosition();
                pOrig[i] = new Vec3(pos.x, pos.y, pos.z);
                p[i] = new Vec3(pos.x, pos.y, pos.z);
                p0[i] = new Vec3(pos.x, pos.y, pos.z);
                renderPos[i] = new Vec3(pos.x, pos.y, pos.z);
                prevRenderPos[i] = new Vec3(pos.x, pos.y, pos.z);
                v[i] = new Vec3(0, 0, 0);
                a[i] = new Vec3(0, 0, 0);
                m[i] = 1;//0.5f + 0.5f / (i + 1);
                if (i > 0) {
                    d[i] = (float) pOrig[i].distanceTo(pOrig[i - 1]);
                } else {
                    d[i] = 0f;
                }
            }

            for (int i = 0; i < chainOrig.length; i++) {
                if (chainDynamic[i] == null) {
                    chainDynamic[i] = new MowzieGeoBone(chainOrig[i]);
                }
            }
        }
    }

    public void updateChain(float delta, MowzieGeoBone[] chainOrig, MowzieGeoBone[] chainDynamic, float gravityAmount, float stiffness, float stiffnessFalloff, float damping, int numUpdates, boolean useFloor) {
        if (p.length != chainOrig.length || Double.isNaN(p[0].x)) {
            return;
        }

        float currentTime = entity.tickCount + delta;

        for (int i = 0; i < chainOrig.length; i++) {
            prevRenderPos[i] = new Vec3(renderPos[i].x, renderPos[i].y, renderPos[i].z);
        }

        for (int i = 0; i < chainOrig.length; i++) {
            Vector3d p = chainOrig[i].getWorldPosition();
            pOrig[i] = new Vec3(p.x, p.y, p.z);
            if (i > 0) {
                d[i] = (float) pOrig[i].distanceTo(pOrig[i - 1]);
            } else {
                d[i] = 0f;
            }
        }

        // Run physics update
        if (!Minecraft.getInstance().isPaused()) {
            updateSpringConstraint(gravityAmount, damping, stiffness, 1, false, 0, numUpdates, currentTime - prevUpdateTime);
        }

        for (int i = 0; i < chainOrig.length; i++) {
            renderPos[i] = new Vec3(p[i].x, p[i].y, p[i].z);
        }

        prevUpdateTime = currentTime;

        if (Minecraft.getInstance().isPaused()) delta = 0.5f;
        setChainFromRenderPos(chainOrig, chainDynamic, delta);
    }

    // Set the xform matrix of the dynamic chain bones according to the computed render pos
    private void setChainFromRenderPos(MowzieGeoBone[] chainOrig, MowzieGeoBone[] chainDynamic, float delta) {
        for (int i = chainDynamic.length - 1; i >= 0; i--) {
            if (chainDynamic[i] == null) return;

            chainDynamic[i].setForceMatrixTransform(true);
            chainDynamic[i].setHidden(false);
            chainOrig[i].setHidden(true);
            chainOrig[i].setDynamicJoint(true);

            Matrix4f xformOverride = new Matrix4f();

            // Translation
            xformOverride = xformOverride.translate((float) p[i].x, (float) p[i].y, (float) p[i].z);
//            xformOverride = xformOverride.translate((float) pOrig[i].x, (float) pOrig[i].y, (float) pOrig[i].z);

            // Rotation - based on translations
            if (i < chainOrig.length - 1) {
                Quaternionf q;
                Vector3d p1 = new Vector3d(p[i].x, p[i].y, p[i].z);
                Vector3d p2 = new Vector3d(p[i + 1].x, p[i + 1].y, p[i + 1].z);
                Vector3d desiredDir = p2.sub(p1, new Vector3d()).normalize();
                Vector3d startingDir = new Vector3d(0, -1, 0);
                double dot = desiredDir.dot(startingDir);
                if (dot > 0.9999999) {
                    q = new Quaternionf();
                }
                else {
                    Vector3d cross = startingDir.cross(desiredDir);
                    double w = Math.sqrt(desiredDir.lengthSquared() * startingDir.lengthSquared()) + dot;
                    q = new Quaternionf(cross.x, cross.y, cross.z, w).normalize();
                }
                xformOverride.rotate(q);
            }

            chainDynamic[i].setWorldSpaceMatrix(xformOverride);
        }
    }

    private static Vec3 fromPitchYaw(float pitch, float yaw) {
        float f = Mth.cos(-yaw - (float)Math.PI);
        float f1 = Mth.sin(-yaw - (float)Math.PI);
        float f2 = -Mth.cos(-pitch);
        float f3 = Mth.sin(-pitch);
        return new Vec3(f1 * f2, f3, f * f2);
    }

    private static Vec3 angleBetween(Vec3 p1, Vec3 p2) {
//        Quaternion q = new Quaternion();
//        Vector3d v1 = p2.subtract(p1);
//        Vector3d v2 = new Vector3d(1, 0, 0);
//        Vector3d a = v1.crossProduct(v2);
//        q.setX((float) a.x);
//        q.setY((float) a.y);
//        q.setZ((float) a.z);
//        q.setW((float)(Math.sqrt(Math.pow(v1.length(), 2) * Math.pow(v2.length(), 2)) + v1.dotProduct(v2)));
//        return q;

        float dz = (float) (p2.z - p1.z);
        float dx = (float) (p2.x - p1.x);
        float dy = (float) (p2.y - p1.y);

        float yaw = (float) Mth.atan2(dz, dx);
        float pitch = (float) Mth.atan2(Math.sqrt(dz * dz + dx * dx), dy);
        return wrapAngles(new Vec3(yaw, pitch, 0));

//        Vector3d vec1 = p2.subtract(p1);
//        Vector3d vec2 = new Vector3d(1, 0, 0);
//        Vector3d vec1YawCalc = new Vector3d(vec1.x, vec2.y, vec1.z);
//        Vector3d vec1PitchCalc = new Vector3d(vec2.x, vec1.y, vec2.z);
//        float yaw = (float) Math.acos((vec1YawCalc.dotProduct(vec2))/(vec1YawCalc.length() * vec2.length()));
//        float pitch = (float) Math.acos((vec1PitchCalc.dotProduct(vec2))/(vec1PitchCalc.length() * vec2.length()));
//        return new Vector3d(yaw, pitch, 0);

//        Vector3d vec1 = p2.subtract(p1).normalize();
//        Vector3d vec2 = new Vector3d(0, 0, -1);
//        return toEuler(vec1.crossProduct(vec2).normalize(), Math.acos(vec1.dotProduct(vec2)/(vec1.length() * vec2.length())));

//        Vector3d vec1 = p2.subtract(p1);
//        Vector3d vec2 = new Vector3d(0, 0, -1);
//        Vector3d vec1XY = new Vector3d(vec1.x, vec1.y, 0);
//        Vector3d vec2XY = new Vector3d(vec2.x, vec2.y, 0);
//        Vector3d vec1XZ = new Vector3d(vec1.x, 0, vec2.z);
//        Vector3d vec2XZ = new Vector3d(vec2.x, 0, vec2.z);
//        Vector3d vec1YZ = new Vector3d(0, vec1.y, vec2.z);
//        Vector3d vec2YZ = new Vector3d(0, vec2.y, vec2.z);
//        double yaw = Math.acos(vec1XZ.dotProduct(vec2XZ));
//        double pitch = Math.acos(vec1YZ.dotProduct(vec2YZ));
//        double roll = Math.acos(vec1XY.dotProduct(vec2XY));
//        return new Vector3d(yaw - Math.PI/2, pitch + Math.PI/2, 0);

//        return toPitchYaw(p1.subtract(p2).normalize());
    }

    public static Vec3 toPitchYaw(Vec3 vector)
    {
//        float f = MathHelper.cos(-p_189986_1_ * 0.017453292F - (float)Math.PI);
//        float f1 = MathHelper.sin(-p_189986_1_ * 0.017453292F - (float)Math.PI);
//        float f2 = -MathHelper.cos(-p_189986_0_ * 0.017453292F);
//        float f3 = MathHelper.sin(-p_189986_0_ * 0.017453292F);
//        return new Vector3d((double)(f1 * f2), (double)f3, (double)(f * f2));

        double f3 = vector.y;
        double pitch = -Math.asin(f3);
        double f2 = -Math.cos(pitch);
//        if (Math.abs(f2) < 0.0001) return new Vector3d(0, pitch, 0);
        double f1 = vector.x/f2;
        double yaw = -Math.asin(f1) + Math.PI/2;

        return wrapAngles(new Vec3(yaw, pitch, 0));
    }

    private static Vec3 toEuler(Vec3 axis, double angle) {
        //System.out.println(axis + ", " + angle);
        double s=Math.sin(angle);
        double c=Math.cos(angle);
        double t=1-c;

        double yaw = 0;
        double pitch = 0;
        double roll = 0;

        double x = axis.x;
        double y = axis.y;
        double z = axis.z;

        if ((x*y*t + z*s) > 0.998) { // north pole singularity detected
            yaw = 2*Math.atan2(x*Math.sin(angle/2),Math.cos(angle/2));
            pitch = Math.PI/2;
            roll = 0;
        }
        else if ((x*y*t + z*s) < -0.998) { // south pole singularity detected
            yaw = -2*Math.atan2(x*Math.sin(angle/2),Math.cos(angle/2));
            pitch = -Math.PI/2;
            roll = 0;
        }
        else {
            yaw = Math.atan2(y * s - x * z * t, 1 - (y * y + z * z) * t);
            pitch = Math.asin(x * y * t + z * s);
            roll = Math.atan2(x * s - y * z * t, 1 - (x * x + z * z) * t);
        }

        return new Vec3(yaw, pitch, roll);
    }

    private static Vec3 wrapAngles(Vec3 r) {
//        double x = Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(r.x)));
//        double y = Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(r.y)));
//        double z = Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(r.z)));

        double x = r.x;
        double y = r.y;
        double z = r.z;

        while (x > Math.PI) x -= 2 * Math.PI;
        while (x < -Math.PI) x += 2 * Math.PI;

        while (y > Math.PI) y -= 2 * Math.PI;
        while (y < -Math.PI) y += 2 * Math.PI;

        while (z > Math.PI) z -= 2 * Math.PI;
        while (z < -Math.PI) z += 2 * Math.PI;

        return new Vec3(x,y,z);
    }

    private static Vec3 multiply(Vec3 u, Vec3 v, boolean preserveDir) {
        if (preserveDir) {
            return new Vec3(u.x * Math.abs(v.x), u.y * Math.abs(v.y), u.z * Math.abs(v.z));
        }
        return new Vec3(u.x * v.x, u.y * v.y, u.z * v.z);
    }
}
