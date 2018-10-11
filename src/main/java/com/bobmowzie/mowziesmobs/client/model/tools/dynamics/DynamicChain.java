package com.bobmowzie.mowziesmobs.client.model.tools.dynamics;

import com.bobmowzie.mowziesmobs.client.model.tools.MathUtils;
import com.bobmowzie.mowziesmobs.client.model.tools.SocketModelRenderer;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.Sys;

import javax.vecmath.Point3d;

/**
 * Created by Josh on 8/30/2018.
 */
public class DynamicChain {
    private Vec3d[] p;
    private Vec3d[] v;
    private Vec3d[] a;
    private Vec3d[] F;
    private float[] m;
    private float[] d;
    private Vec3d[] T;
    private Vec3d[] r;
    private Vec3d[] rv;
    private Vec3d[] ra;
    private Entity entity;
    private Vec3d prevP;
    private Vec3d prevV;

    public SocketModelRenderer[] dynModelRenderers;
    public SocketModelRenderer[] origModelRenderers;

    public DynamicChain(Entity entity) {
        this.entity = entity;
        p = new Vec3d[0];
        v = new Vec3d[0];
        a = new Vec3d[0];
        F = new Vec3d[0];
        m = new float[0];
        d = new float[0];
        T = new Vec3d[0];
        ra = new Vec3d[0];
        rv = new Vec3d[0];
        r = new Vec3d[0];
    }

    public void updateBendConstraint(float gravityAmount, float stiffness, float stiffnessFalloff, float damping, int numUpdates) {
        Vec3d[] prevPos = new Vec3d[p.length];
        Vec3d[] prevVel = new Vec3d[v.length];
        for (int i = 0; i < p.length; i++) {
            prevPos[i] = p[i];
            prevVel[i] = v[i];
        }
        for (int j = 0; j < numUpdates; j++) {
            for (int i = 0; i < p.length - 1; i++) {
                if (i == 0) {
                    Vec3d root = origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());
                    p[i] = root;
                    v[i] = p[i].subtract(prevPos[i]);
                    a[i] = v[i].subtract(prevVel[i]);
                }

                Vec3d target = angleBetween(
                        origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()),
                        origModelRenderers[i + 1].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()));
                float gravity = 1 - (float) Math.pow(1 - gravityAmount, (i + 1));
                target = new Vec3d(target.x, (1-gravity) * target.y + gravity * Math.PI, target.z);

                r[i] = angleBetween(p[i], p[i + 1]);
//                T[i] = multiply(r[i].subtract(target), r[i].subtract(target), true).scale(-stiffness);
                T[i] = r[i].subtract(target).scale(-stiffness/(Math.pow(i + 1, stiffnessFalloff)));
                ra[i] = T[i].scale(1 / m[i + 1]);
                rv[i] = rv[i].add(ra[i].scale(1/((float)numUpdates))).scale(damping);
                r[i] = r[i].add(rv[i].scale(1/((float)numUpdates)));

                p[i + 1] = Vec3d.fromPitchYaw((float) (Math.toDegrees(r[i].y) - 90), (float) (Math.toDegrees(r[i].x) - 90)).scale(d[i + 1]).add(p[i]);
                v[i + 1] = p[i + 1].subtract(prevPos[i+1]);
                a[i + 1] = v[i + 1].subtract(prevVel[i+1]);

                r[i] = wrapAngles(r[i]);
                rv[i] = wrapAngles(rv[i]);
            }
            if (r != null && r.length > 0) {
//                System.out.println(v[2]);
            }
        }
    }

    public void updateSpringConstraint(float gravityAmount, float dampAmount, float stiffness, float maxForce, boolean doAttract, float attractFalloff, int numUpdates) {
        for (int j = 0; j < numUpdates; j++) {
            for (int i = 0; i < p.length; i++) {
                a[i] = F[i].scale(1 / m[i]);
                v[i] = v[i].add(a[i].scale(1/((float)numUpdates)));
                p[i] = p[i].add(v[i].scale(1/((float)numUpdates)));

                Vec3d disp;
                if (i == 0) {
                    Vec3d root = origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks());
                    disp = p[i].subtract(root);
                } else {
                    disp = p[i].subtract(p[i - 1]);
                }
                disp = disp.normalize().scale(disp.lengthVector() - d[i]);
                Vec3d damp = v[i].scale(dampAmount);
                Vec3d gravity = new Vec3d(0, -gravityAmount, 0);
                Vec3d attract = origModelRenderers[i].getWorldPos(entity, LLibrary.PROXY.getPartialTicks()).subtract(p[i]);
                F[i] = disp.scale(-stiffness * (disp.lengthVector())).add(gravity.scale(m[i])).subtract(damp);
                if (i == 0 || doAttract) {
                    F[i] = F[i].add(attract.scale(1 / (1 + i * i * attractFalloff)));
                }
                if (F[i].lengthVector() > maxForce) F[i].normalize().scale(maxForce);

//            if (disp.lengthVector() > 0 && F[i].dotProduct(disp) > 0) {
//                Vec3d antiStretch = disp.normalize().scale(1.5 * F[i].dotProduct(disp) / (disp.lengthVector()));
//                F[i] = F[i].add(antiStretch);
//                System.out.println(antiStretch);
//            }
            }
        }
    }

    public void setChain(SocketModelRenderer[] chain) {
        if (origModelRenderers == null) {
            origModelRenderers = chain;
            dynModelRenderers = new SocketModelRenderer[chain.length];
            p = new Vec3d[chain.length];
            v = new Vec3d[chain.length];
            a = new Vec3d[chain.length];
            F = new Vec3d[chain.length];
            m = new float[chain.length];
            d = new float[chain.length];
            T = new Vec3d[chain.length];
            r = new Vec3d[chain.length];
            rv = new Vec3d[chain.length];
            ra = new Vec3d[chain.length];
            for (int i = 0; i < chain.length; i++) {
                dynModelRenderers[i] = new SocketModelRenderer(chain[i]);
                p[i] = chain[i].getWorldPos(entity, 0);
                v[i] = new Vec3d(0, 0, 0);
                a[i] = new Vec3d(0, 0, 0);
                F[i] = new Vec3d(0, 0, 0);
                T[i] = new Vec3d(0, 0, 0);
                r[i] = new Vec3d(0, 0, 0);
                rv[i] = new Vec3d(0, 0, 0);
                ra[i] = new Vec3d(0, 0, 0);
                m[i] = 1;
                if (i > 0) {
                    d[i] = (float)p[i].distanceTo(p[i-1]);
                }
                else {
                    d[i] = 0;
                }
                chain[i].isHidden = true;
            }

            prevP = p[0];
            prevV = v[0];
        }
    }

    public void updateChain(float delta) {
        for (int i = dynModelRenderers.length - 1; i >= 0; i--) {
            Vec3d renderPos = p[i].add(v[i].scale(delta)).add(a[i].scale(0.5 * delta * delta));
            dynModelRenderers[i].setWorldPos(entity, renderPos, delta);

            if (i < dynModelRenderers.length - 1) {
                Vec3d p1 = new Vec3d(dynModelRenderers[i].rotationPointX, dynModelRenderers[i].rotationPointY, dynModelRenderers[i].rotationPointZ);
                Vec3d p2 = new Vec3d(dynModelRenderers[i+1].rotationPointX, dynModelRenderers[i+1].rotationPointY, dynModelRenderers[i+1].rotationPointZ);
                Vec3d diff = p2.subtract(p1);
                float yaw = (float)Math.atan2(diff.x, diff.z);
                float pitch = -(float)Math.asin(diff.y/diff.lengthVector());
                dynModelRenderers[i].rotateAngleY = dynModelRenderers[i].defaultRotationY + yaw;
                dynModelRenderers[i].rotateAngleX = dynModelRenderers[i].defaultRotationZ + pitch;


                Vec3d diffRotated = diff;
                diffRotated = diffRotated.rotateYaw(yaw);
                diffRotated = diffRotated.rotatePitch(pitch);
//                System.out.println(diffRotated);
                dynModelRenderers[i].setScale(1, 1, 1);
//                dynModelRenderers[i].setScale(1, 1, 1 + (float)diffRotated.z/16);
            }
        }
    }

    public void render(float f5) {
        for (int i = 0; i < dynModelRenderers.length - 1; i++) {
            dynModelRenderers[i].render(f5);
        }
    }

    private static Vec3d angleBetween(Vec3d p1, Vec3d p2) {
        float dz = (float) (p2.z - p1.z);
        float dx = (float) (p2.x - p1.x);
        float dy = (float) (p2.y - p1.y);
        float yaw = (float) Math.atan2(dz, dx);
        float pitch = (float) Math.atan2(Math.sqrt(dz * dz + dx * dx), dy);
        return wrapAngles(new Vec3d(yaw, pitch, 0));
    }

    private static Vec3d wrapAngles(Vec3d r) {
        double x = Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(r.x)));
        double y = Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(r.y)));
        double z = Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(r.z)));

        return new Vec3d(x,y,z);
    }

    private static Vec3d multiply(Vec3d u, Vec3d v, boolean preserveDir) {
        if (preserveDir) {
            return new Vec3d(u.x * Math.abs(v.x), u.y * Math.abs(v.y), u.z * Math.abs(v.z));
        }
        return new Vec3d(u.x * v.x, u.y * v.y, u.z * v.z);
    }
}
