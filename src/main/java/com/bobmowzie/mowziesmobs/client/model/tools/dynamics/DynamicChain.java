package com.bobmowzie.mowziesmobs.client.model.tools.dynamics;

import com.bobmowzie.mowziesmobs.client.render.RenderUtils;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.math.MathHelper;
import net.minecraft.world.phys.Vec3;

/**
 * Created by BobMowzie on 8/30/2018.
 */
public class DynamicChain {
    private Vec3[] p;
    private Vec3[] v;
    private Vec3[] a;
    private Vec3[] F;
    private float[] m;
    private float[] d;
    private Vec3[] T;
    private Vec3[] r;
    private Vec3[] rv;
    private Vec3[] ra;
    private final Entity entity;
    private Vec3 prevP;
    private Vec3 prevV;

    private Vec3[] pOrig;
    private int prevUpdateTick;

    public DynamicChain(Entity entity) {
        this.entity = entity;
        p = new Vec3[0];
        v = new Vec3[0];
        a = new Vec3[0];
        F = new Vec3[0];
        m = new float[0];
        d = new float[0];
        T = new Vec3[0];
        ra = new Vec3[0];
        rv = new Vec3[0];
        r = new Vec3[0];
        pOrig = new Vec3[0];
        prevUpdateTick = -1;
    }

    public void updateBendConstraint(float gravityAmount, float stiffness, float stiffnessFalloff, float damping, int numUpdates, boolean useFloor) {
        Vec3[] prevPos = new Vec3[p.length];
        Vec3[] prevVel = new Vec3[v.length];
        for (int i = 0; i < p.length; i++) {
            prevPos[i] = p[i];
            prevVel[i] = v[i];
        }
        for (int j = 0; j < numUpdates; j++) {
            for (int i = 0; i < p.length - 1; i++) {
                if (i == 0) {
                    Vec3 root = pOrig[i]; //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());

                    p[i] = root;
                    v[i] = p[i].subtract(prevPos[i]);
                    a[i] = v[i].subtract(prevVel[i]);
                }

                Vec3 target = angleBetween(
                        pOrig[i], //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()),
                        pOrig[i + 1]); //origModelRenderers[i + 1].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()));
                //float gravity = 1 - (float) Math.pow(1 - gravityAmount, (i + 1));
                //target = new Vec3(target.x, (1-gravity) * target.y + gravity * Math.PI, target.z);

                r[i] = angleBetween(p[i], p[i + 1]);

                T[i] = wrapAngles(r[i].subtract(target)).scale(-stiffness/(Math.pow(i + 1, stiffnessFalloff)));
                double down = Math.PI/2;
                Vec3 gravityVec = wrapAngles(new Vec3(
                        0,
                        gravityAmount * d[i+1] * m[i+1] * (Math.sin(down - r[i].y + down)),
                        0));
                Vec3 floorVec = new Vec3(0, 1 * d[i+1] * m[i+1] * (Math.sin(Math.PI/2 - r[i].y + Math.PI/2)), 0);
                if (useFloor && entity.isOnGround() && p[i+1].y < entity.getPosY()) {
                    T[i] = T[i].subtract(floorVec);
                }
                T[i] = wrapAngles(T[i].add(gravityVec));
                ra[i] = T[i].scale(1 / (m[i + 1] * d[i + 1] * d[i + 1]));
                rv[i] = rv[i].add(ra[i].scale(1/((float)numUpdates))).scale(damping);
                rv[i] = wrapAngles(rv[i]);
                r[i] = r[i].add(rv[i].scale(1/((float)numUpdates)));
                r[i] = wrapAngles(r[i]);

                p[i + 1] = fromPitchYaw((float)(r[i].y - Math.PI/2), (float)(r[i].x - Math.PI/2)).scale(d[i + 1]).add(p[i]);
                v[i + 1] = p[i + 1].subtract(prevPos[i+1]);
                a[i + 1] = v[i + 1].subtract(prevVel[i+1]);
            }
        }
    }

    public void updateSpringConstraint(float gravityAmount, float dampAmount, float stiffness, float maxForce, boolean doAttract, float attractFalloff, int numUpdates) {
        for (int j = 0; j < numUpdates; j++) {
            for (int i = 0; i < p.length; i++) {
                a[i] = F[i].scale(1 / m[i]);
                v[i] = v[i].add(a[i].scale(1/((float)numUpdates)));
                p[i] = p[i].add(v[i].scale(1/((float)numUpdates)));

                Vec3 disp;
                if (i == 0) {
                    Vec3 root = pOrig[i]; //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());
                    disp = p[i].subtract(root);
                } else {
                    disp = p[i].subtract(p[i - 1]);
                }
                disp = disp.normalize().scale(disp.length() - d[i]);
                Vec3 damp = v[i].scale(dampAmount);
                Vec3 gravity = new Vec3(0, -gravityAmount, 0);
                Vec3 attract = pOrig[0].subtract(p[i]); //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()).subtract(p[i]);
                F[i] = disp.scale(-stiffness * (disp.length())).add(gravity.scale(m[i])).subtract(damp);
                if (i == 0 || doAttract) {
                    F[i] = F[i].add(attract.scale(1 / (1 + i * i * attractFalloff)));
                }
                if (F[i].length() > maxForce) F[i].normalize().scale(maxForce);

//            if (disp.length() > 0 && F[i].dotProduct(disp) > 0) {
//                Vec3 antiStretch = disp.normalize().scale(1.5 * F[i].dotProduct(disp) / (disp.length()));
//                F[i] = F[i].add(antiStretch);
//                System.out.println(antiStretch);
//            }
            }
        }
    }

    public void setChain(AdvancedModelRenderer[] chainOrig, AdvancedModelRenderer[] chainDynamic) {
        p = new Vec3[chainOrig.length];
        v = new Vec3[chainOrig.length];
        a = new Vec3[chainOrig.length];
        F = new Vec3[chainOrig.length];
        m = new float[chainOrig.length];
        d = new float[chainOrig.length];
        T = new Vec3[chainOrig.length];
        r = new Vec3[chainOrig.length];
        rv = new Vec3[chainOrig.length];
        ra = new Vec3[chainOrig.length];
        pOrig = new Vec3[chainOrig.length];
        for (int i = 0; i < chainOrig.length; i++) {
            pOrig[i] = chainOrig[i].getWorldPos(entity, 0);
            p[i] = pOrig[i];
            v[i] = new Vec3(0, 0, 0);
            a[i] = new Vec3(0, 0, 0);
            F[i] = new Vec3(0, 0, 0);
            T[i] = new Vec3(0, 0, 0);
            r[i] = new Vec3(0, 0, 0);
            rv[i] = new Vec3(0, 0, 0);
            ra[i] = new Vec3(0, 0, 0);
            m[i] = 0.5f + 0.5f/(i+1);
            if (i > 0) {
                d[i] = (float)p[i].distanceTo(p[i-1]);
            }
            else {
                d[i] = 1f;
            }
            chainOrig[i].showModel = false;
        }

        for (int i = 0; i < chainOrig.length - 1; i++) {
            r[i] = angleBetween(p[i], p[i + 1]);
        }

        prevP = p[0];
        prevV = v[0];

        for (int i = 0; i < chainOrig.length; i++) {
            if (chainDynamic[i] == null) {
                chainDynamic[i] = new AdvancedModelRenderer(chainOrig[i]);
            }
        }
    }

    public void updateChain(float delta, AdvancedModelRenderer[] chainOrig, AdvancedModelRenderer[] chainDynamic, float gravityAmount, float stiffness, float stiffnessFalloff, float damping, int numUpdates, boolean useFloor) {
        if (p.length != chainOrig.length || Double.isNaN(p[1].x)) {
            setChain(chainOrig, chainDynamic);
        }

        if (prevUpdateTick != entity.ticksExisted) {
            for (int i = 0; i < chainOrig.length; i++) {
                pOrig[i] = chainOrig[i].getWorldPos(entity, delta);
            }

            updateBendConstraint(gravityAmount, stiffness, stiffnessFalloff, damping, numUpdates, useFloor);

            prevUpdateTick = entity.ticksExisted;
        }

        if (chainDynamic == null) return;
        if (Minecraft.getInstance().isGamePaused()) delta = 0.5f;
        for (int i = chainDynamic.length - 1; i >= 0; i--) {
            if (chainDynamic[i] == null) return;
            Vec3 renderPos = p[i].add(v[i].scale(delta)).add(a[i].scale(0.5 * delta * delta));
            chainDynamic[i].setWorldPos(entity, renderPos, delta);

            if (i < chainDynamic.length - 1) {
                Vec3 p1 = new Vec3(chainDynamic[i].rotationPointX, chainDynamic[i].rotationPointY, chainDynamic[i].rotationPointZ);
                Vec3 p2 = new Vec3(chainDynamic[i+1].rotationPointX, chainDynamic[i+1].rotationPointY, chainDynamic[i+1].rotationPointZ);
                Vec3 diff = p2.subtract(p1);
                float yaw = (float)Math.atan2(diff.x, diff.z);
                float pitch = -(float)Math.asin(diff.y/diff.length());
                chainDynamic[i].rotateAngleY = chainDynamic[i].defaultRotationY + yaw;
                chainDynamic[i].rotateAngleX = chainDynamic[i].defaultRotationZ + pitch;
                chainDynamic[i].rotateAngleZ = (float) r[i].z;

                Vec3 diffRotated = diff;
                diffRotated = diffRotated.rotateYaw(yaw);
                diffRotated = diffRotated.rotatePitch(pitch);
//                System.out.println(diffRotated);
//                dynModelRenderers[i].setScale(1, 1, 1);
//                dynModelRenderers[i].setScale(1, 1, 1 + (float)diffRotated.z/16);
            }
        }
    }

    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, AdvancedModelRenderer[] dynModelRenderers) {
        if (dynModelRenderers == null) return;
        for (int i = 0; i < dynModelRenderers.length - 1; i++) {
            if (dynModelRenderers[i] == null) return;
            dynModelRenderers[i].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }

    private static Vec3 fromPitchYaw(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch);
        float f3 = MathHelper.sin(-pitch);
        return new Vec3(f1 * f2, f3, f * f2);
    }

    private static Vec3 angleBetween(Vec3 p1, Vec3 p2) {
//        Quaternion q = new Quaternion();
//        Vec3 v1 = p2.subtract(p1);
//        Vec3 v2 = new Vec3(1, 0, 0);
//        Vec3 a = v1.crossProduct(v2);
//        q.setX((float) a.x);
//        q.setY((float) a.y);
//        q.setZ((float) a.z);
//        q.setW((float)(Math.sqrt(Math.pow(v1.length(), 2) * Math.pow(v2.length(), 2)) + v1.dotProduct(v2)));
//        return q;

        float dz = (float) (p2.z - p1.z);
        float dx = (float) (p2.x - p1.x);
        float dy = (float) (p2.y - p1.y);

        float yaw = (float) MathHelper.atan2(dz, dx);
        float pitch = (float) MathHelper.atan2(Math.sqrt(dz * dz + dx * dx), dy);
        return wrapAngles(new Vec3(yaw, pitch, 0));

//        Vec3 vec1 = p2.subtract(p1);
//        Vec3 vec2 = new Vec3(1, 0, 0);
//        Vec3 vec1YawCalc = new Vec3(vec1.x, vec2.y, vec1.z);
//        Vec3 vec1PitchCalc = new Vec3(vec2.x, vec1.y, vec2.z);
//        float yaw = (float) Math.acos((vec1YawCalc.dotProduct(vec2))/(vec1YawCalc.length() * vec2.length()));
//        float pitch = (float) Math.acos((vec1PitchCalc.dotProduct(vec2))/(vec1PitchCalc.length() * vec2.length()));
//        return new Vec3(yaw, pitch, 0);

//        Vec3 vec1 = p2.subtract(p1).normalize();
//        Vec3 vec2 = new Vec3(0, 0, -1);
//        return toEuler(vec1.crossProduct(vec2).normalize(), Math.acos(vec1.dotProduct(vec2)/(vec1.length() * vec2.length())));

//        Vec3 vec1 = p2.subtract(p1);
//        Vec3 vec2 = new Vec3(0, 0, -1);
//        Vec3 vec1XY = new Vec3(vec1.x, vec1.y, 0);
//        Vec3 vec2XY = new Vec3(vec2.x, vec2.y, 0);
//        Vec3 vec1XZ = new Vec3(vec1.x, 0, vec2.z);
//        Vec3 vec2XZ = new Vec3(vec2.x, 0, vec2.z);
//        Vec3 vec1YZ = new Vec3(0, vec1.y, vec2.z);
//        Vec3 vec2YZ = new Vec3(0, vec2.y, vec2.z);
//        double yaw = Math.acos(vec1XZ.dotProduct(vec2XZ));
//        double pitch = Math.acos(vec1YZ.dotProduct(vec2YZ));
//        double roll = Math.acos(vec1XY.dotProduct(vec2XY));
//        return new Vec3(yaw - Math.PI/2, pitch + Math.PI/2, 0);

//        return toPitchYaw(p1.subtract(p2).normalize());
    }

    public static Vec3 toPitchYaw(Vec3 vector)
    {
//        float f = MathHelper.cos(-p_189986_1_ * 0.017453292F - (float)Math.PI);
//        float f1 = MathHelper.sin(-p_189986_1_ * 0.017453292F - (float)Math.PI);
//        float f2 = -MathHelper.cos(-p_189986_0_ * 0.017453292F);
//        float f3 = MathHelper.sin(-p_189986_0_ * 0.017453292F);
//        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));

        double f3 = vector.y;
        double pitch = -Math.asin(f3);
        double f2 = -Math.cos(pitch);
//        if (Math.abs(f2) < 0.0001) return new Vec3(0, pitch, 0);
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
