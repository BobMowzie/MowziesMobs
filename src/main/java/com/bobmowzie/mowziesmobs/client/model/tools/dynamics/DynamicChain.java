package com.bobmowzie.mowziesmobs.client.model.tools.dynamics;

import com.bobmowzie.mowziesmobs.client.render.RenderUtils;
import com.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Created by BobMowzie on 8/30/2018.
 */
public class DynamicChain {
    private Vector3d[] p;
    private Vector3d[] v;
    private Vector3d[] a;
    private Vector3d[] F;
    private float[] m;
    private float[] d;
    private Vector3d[] T;
    private Vector3d[] r;
    private Vector3d[] rv;
    private Vector3d[] ra;
    private final Entity entity;
    private Vector3d prevP;
    private Vector3d prevV;

    private Vector3d[] pOrig;
    private int prevUpdateTick;

    public DynamicChain(Entity entity) {
        this.entity = entity;
        p = new Vector3d[0];
        v = new Vector3d[0];
        a = new Vector3d[0];
        F = new Vector3d[0];
        m = new float[0];
        d = new float[0];
        T = new Vector3d[0];
        ra = new Vector3d[0];
        rv = new Vector3d[0];
        r = new Vector3d[0];
        pOrig = new Vector3d[0];
        prevUpdateTick = -1;
    }

    public void updateBendConstraint(float gravityAmount, float stiffness, float stiffnessFalloff, float damping, int numUpdates, boolean useFloor) {
        Vector3d[] prevPos = new Vector3d[p.length];
        Vector3d[] prevVel = new Vector3d[v.length];
        for (int i = 0; i < p.length; i++) {
            prevPos[i] = p[i];
            prevVel[i] = v[i];
        }
        for (int j = 0; j < numUpdates; j++) {
            for (int i = 0; i < p.length - 1; i++) {
                if (i == 0) {
                    Vector3d root = pOrig[i]; //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());

                    p[i] = root;
                    v[i] = p[i].subtract(prevPos[i]);
                    a[i] = v[i].subtract(prevVel[i]);
                }

                Vector3d target = angleBetween(
                        pOrig[i], //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()),
                        pOrig[i + 1]); //origModelRenderers[i + 1].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()));
                //float gravity = 1 - (float) Math.pow(1 - gravityAmount, (i + 1));
                //target = new Vector3d(target.x, (1-gravity) * target.y + gravity * Math.PI, target.z);

                r[i] = angleBetween(p[i], p[i + 1]);

                T[i] = wrapAngles(r[i].subtract(target)).scale(-stiffness/(Math.pow(i + 1, stiffnessFalloff)));
                double down = Math.PI/2;
                Vector3d gravityVec = wrapAngles(new Vector3d(
                        0,
                        gravityAmount * d[i+1] * m[i+1] * (Math.sin(down - r[i].y + down)),
                        0));
                Vector3d floorVec = new Vector3d(0, 1 * d[i+1] * m[i+1] * (Math.sin(Math.PI/2 - r[i].y + Math.PI/2)), 0);
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

                Vector3d disp;
                if (i == 0) {
                    Vector3d root = pOrig[i]; //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());
                    disp = p[i].subtract(root);
                } else {
                    disp = p[i].subtract(p[i - 1]);
                }
                disp = disp.normalize().scale(disp.length() - d[i]);
                Vector3d damp = v[i].scale(dampAmount);
                Vector3d gravity = new Vector3d(0, -gravityAmount, 0);
                Vector3d attract = pOrig[0].subtract(p[i]); //origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()).subtract(p[i]);
                F[i] = disp.scale(-stiffness * (disp.length())).add(gravity.scale(m[i])).subtract(damp);
                if (i == 0 || doAttract) {
                    F[i] = F[i].add(attract.scale(1 / (1 + i * i * attractFalloff)));
                }
                if (F[i].length() > maxForce) F[i].normalize().scale(maxForce);

//            if (disp.length() > 0 && F[i].dotProduct(disp) > 0) {
//                Vector3d antiStretch = disp.normalize().scale(1.5 * F[i].dotProduct(disp) / (disp.length()));
//                F[i] = F[i].add(antiStretch);
//                System.out.println(antiStretch);
//            }
            }
        }
    }

    public void setChain(AdvancedModelRenderer[] chainOrig, AdvancedModelRenderer[] chainDynamic) {
        p = new Vector3d[chainOrig.length];
        v = new Vector3d[chainOrig.length];
        a = new Vector3d[chainOrig.length];
        F = new Vector3d[chainOrig.length];
        m = new float[chainOrig.length];
        d = new float[chainOrig.length];
        T = new Vector3d[chainOrig.length];
        r = new Vector3d[chainOrig.length];
        rv = new Vector3d[chainOrig.length];
        ra = new Vector3d[chainOrig.length];
        pOrig = new Vector3d[chainOrig.length];
        for (int i = 0; i < chainOrig.length; i++) {
            pOrig[i] = chainOrig[i].getWorldPos(entity, 0);
            p[i] = pOrig[i];
            v[i] = new Vector3d(0, 0, 0);
            a[i] = new Vector3d(0, 0, 0);
            F[i] = new Vector3d(0, 0, 0);
            T[i] = new Vector3d(0, 0, 0);
            r[i] = new Vector3d(0, 0, 0);
            rv[i] = new Vector3d(0, 0, 0);
            ra[i] = new Vector3d(0, 0, 0);
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
            Vector3d renderPos = p[i].add(v[i].scale(delta)).add(a[i].scale(0.5 * delta * delta));
            chainDynamic[i].setWorldPos(entity, renderPos, delta);

            if (i < chainDynamic.length - 1) {
                Vector3d p1 = new Vector3d(chainDynamic[i].rotationPointX, chainDynamic[i].rotationPointY, chainDynamic[i].rotationPointZ);
                Vector3d p2 = new Vector3d(chainDynamic[i+1].rotationPointX, chainDynamic[i+1].rotationPointY, chainDynamic[i+1].rotationPointZ);
                Vector3d diff = p2.subtract(p1);
                float yaw = (float)Math.atan2(diff.x, diff.z);
                float pitch = -(float)Math.asin(diff.y/diff.length());
                chainDynamic[i].rotateAngleY = chainDynamic[i].defaultRotationY + yaw;
                chainDynamic[i].rotateAngleX = chainDynamic[i].defaultRotationZ + pitch;
                chainDynamic[i].rotateAngleZ = (float) r[i].z;

                Vector3d diffRotated = diff;
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

    private static Vector3d fromPitchYaw(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch);
        float f3 = MathHelper.sin(-pitch);
        return new Vector3d(f1 * f2, f3, f * f2);
    }

    private static Vector3d angleBetween(Vector3d p1, Vector3d p2) {
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

        float yaw = (float) MathHelper.atan2(dz, dx);
        float pitch = (float) MathHelper.atan2(Math.sqrt(dz * dz + dx * dx), dy);
        return wrapAngles(new Vector3d(yaw, pitch, 0));

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

    public static Vector3d toPitchYaw(Vector3d vector)
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

        return wrapAngles(new Vector3d(yaw, pitch, 0));
    }

    private static Vector3d toEuler(Vector3d axis, double angle) {
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

        return new Vector3d(yaw, pitch, roll);
    }

    private static Vector3d wrapAngles(Vector3d r) {
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

        return new Vector3d(x,y,z);
    }

    private static Vector3d multiply(Vector3d u, Vector3d v, boolean preserveDir) {
        if (preserveDir) {
            return new Vector3d(u.x * Math.abs(v.x), u.y * Math.abs(v.y), u.z * Math.abs(v.z));
        }
        return new Vector3d(u.x * v.x, u.y * v.y, u.z * v.z);
    }
}
