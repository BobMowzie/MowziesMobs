package com.bobmowzie.mowziesmobs.client.model.tools;

import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.processor.IBone;

public class RigUtils {
    public static Vec3 lerp(Vec3 v, Vec3 u, float alpha) {
        return new Vec3(
                Mth.lerp(alpha, (float)v.x(), (float) u.x()),
                Mth.lerp(alpha, (float)v.y(), (float) u.y()),
                Mth.lerp(alpha, (float)v.z(), (float) u.z())
        );
    }

    public static Vec3 lerpAngles(Vec3 v, Vec3 u, float alpha) {
        return new Vec3(
                Math.toRadians(Mth.rotLerp(alpha, (float) Math.toDegrees(v.x()), (float) Math.toDegrees(u.x()))),
                Math.toRadians(Mth.rotLerp(alpha, (float) Math.toDegrees(v.y()), (float) Math.toDegrees(u.y()))),
                Math.toRadians(Mth.rotLerp(alpha, (float) Math.toDegrees(v.z()), (float) Math.toDegrees(u.z())))
        );
    }

    public static Vec3 blendAngles(Vec3 v, Vec3 u, float alpha) {
        return new Vec3(
                Math.toRadians(Mth.wrapDegrees(Math.toDegrees(v.x()) * alpha + Math.toDegrees(u.x()))),
                Math.toRadians(Mth.wrapDegrees(Math.toDegrees(v.y()) * alpha + Math.toDegrees(u.y()))),
                Math.toRadians(Mth.wrapDegrees(Math.toDegrees(v.z()) * alpha + Math.toDegrees(u.z())))
        );
    }

    public static class BoneTransform {
        private final Vec3 translation;
        private final Vec3 rotation;
        private final Vec3 scale;

        public BoneTransform(
                double tx, double ty, double tz,
                double rx, double ry, double rz,
                double sx, double sy, double sz
        ) {
            translation = new Vec3(tx, ty, tz);
            rotation = new Vec3(rx, ry, rz);
            scale = new Vec3(sx, sy, sz);
        }

        public BoneTransform(Vec3 t, Vec3 r, Vec3 s) {
            translation = t;
            rotation = r;
            scale = s;
        }

        public BoneTransform blend(BoneTransform other, float alpha) {
            return new BoneTransform(
                    this.translation.scale(alpha).add(other.translation),
                    RigUtils.blendAngles(this.rotation, other.rotation, alpha),
                    this.scale.scale(alpha).add(other.scale)
            );
        }

        public void apply(IBone bone) {
            apply(bone, false);
        }

        public void apply(IBone bone, boolean mirrorX) {
            float mirror = mirrorX ? -1 : 1;
            bone.setPositionX(bone.getPositionX() + mirror * (float) translation.x());
            bone.setPositionY(bone.getPositionY() + (float) translation.y());
            bone.setPositionZ(bone.getPositionZ() + (float) translation.z());

            bone.setRotationX(bone.getRotationX() + (float) rotation.x());
            bone.setRotationY(bone.getRotationY() + mirror * (float) rotation.y());
            bone.setRotationZ(bone.getRotationZ() + mirror * (float) rotation.z());

            bone.setScaleX(bone.getScaleX() * (float) scale.x());
            bone.setScaleY(bone.getScaleY() * (float) scale.y());
            bone.setScaleZ(bone.getScaleZ() * (float) scale.z());
        }
    }

    public static class BlendShape3DEntry {
        private BoneTransform transform;
        private Vec3 direction;
        private float power;

        public BlendShape3DEntry(BoneTransform transform, Vec3 direction, float power) {
            this.transform = transform;
            this.direction = direction.normalize();
            this.power = power;
        }

        public double getWeight(Vec3 dir) {
            double dot = dir.normalize().dot(direction.normalize());
            dot = Math.max(dot, 0);
            dot = Math.pow(dot, 0.01 * power);
            return dot;
        }

        public BoneTransform blend(BoneTransform other, float alpha) {
            return transform.blend(other, alpha);
        }
    }

    public static class BlendShape3D {
        private final BlendShape3DEntry[] entries;
        public BlendShape3D(BlendShape3DEntry[] entries) {
            this.entries = entries;
        }

        public void evaluate(IBone bone, Vec3 dir) {
            evaluate(bone, dir, false);
        }

        private double[] getWeights(Vec3 dir) {
            double[] weights = new double[entries.length];
            double[] dotProducts = new double[entries.length];
            double totalDotProduct = 0.0;
            for (int i = 0; i < entries.length; i++) {
                BlendShape3DEntry entry = entries[i];
                double dot = 1.0 - entry.getWeight(dir);
                if (dot > 0.0) {
                    totalDotProduct += 1.0 / dot;
                    dotProducts[i] = dot;
                }
                else {
                    weights[i] = 1.0;
                    return weights;
                }
            }
            for (int i = 0; i < entries.length; i++) {
                double dot_prod = totalDotProduct * dotProducts[i];
                if (dot_prod > 0) weights[i] = 1 / dot_prod;
                else weights[i] = 0.0;
            }
            return weights;
        }

        private double[] getWeightsGradientBand(Vec3 dir) {
            double[] weights = new double[entries.length];
            double[] sqrdDistances = new double[entries.length];
            double[] angularDistances = new double[entries.length];
            double totalSqrdDistance = 0.0;
            double totalAngularDistance = 0.0;
            for (int i = 0; i < entries.length; i++) {
                BlendShape3DEntry entry = entries[i];
                double sqrdDistance = dir.subtract(entry.direction).dot(dir.subtract(entry.direction));
                if (sqrdDistance > 0.0) {
                    double angularDistance = -(Mth.clamp(dir.dot(entry.direction), -1, 1) - 1) * 0.5;
                    totalSqrdDistance += 1.0 / sqrdDistance;
                    if (angularDistance > 0) totalAngularDistance += 1.0 / angularDistance;
                    sqrdDistances[i] = sqrdDistance;
                    angularDistances[i] = angularDistance;
                }
                else {
                    weights[i] = 1.0;
                    return weights;
                }
            }
            for (int i = 0; i < entries.length; i++) {
                double sqrdDistance = totalSqrdDistance * sqrdDistances[i];
                double angularDistance = totalAngularDistance * angularDistances[i];
                if (sqrdDistance > 0.0 && angularDistance > 0.0)
                    weights[i] = (1.0 / sqrdDistance) * 0.5 + (1.0 / angularDistance) * 0.5;
                else if (sqrdDistance > 0.0)
                    weights[i] = (1 / sqrdDistance) * 0.5 + 0.5;
                else weights[i] = 0.0;
            }
            return weights;
        }

        public void evaluate(IBone bone, Vec3 d, boolean mirrorX) {
            Vec3 dir = mirrorX ? d.multiply(-1, 1, 1) : d;
            dir = dir.normalize();

            double[] weights = getWeights(dir);
            BoneTransform transform = new BoneTransform(
                    0.0, 0.0 ,0.0,
                    0.0, 0.0, 0.0,
                    0.0, 0.0, 0.0
            );
            for (int i = 0; i < entries.length; i++) {
                BlendShape3DEntry entry = entries[i];
                transform = entry.blend(transform, (float) Mth.clamp(weights[i],0, 1));
            }
            transform.apply(bone, mirrorX);
        }
    }

    public static Quaternion matrixToQuaternion(Matrix3f matrix)
    {
        double tr = matrix.m00 + matrix.m11 + matrix.m22;
        double qw = 0;
        double qx = 0;
        double qy = 0;
        double qz = 0;

        if (tr > 0)
        {
            double S = Math.sqrt(tr+1.0) * 2; // S=4*qw
            qw = 0.25 * S;
            qx = (matrix.m21 - matrix.m12) / S;
            qy = (matrix.m02 - matrix.m20) / S;
            qz = (matrix.m10 - matrix.m01) / S;
        }
        else if ((matrix.m00 > matrix.m11) & (matrix.m00 > matrix.m22))
        {
            double S = Math.sqrt(1.0 + matrix.m00 - matrix.m11 - matrix.m22) * 2; // S=4*qx
            qw = (matrix.m21 - matrix.m12) / S;
            qx = 0.25 * S;
            qy = (matrix.m01 + matrix.m10) / S;
            qz = (matrix.m02 + matrix.m20) / S;
        }
        else if (matrix.m11 > matrix.m22)
        {
            double S = Math.sqrt(1.0 + matrix.m11 - matrix.m00 - matrix.m22) * 2; // S=4*qy
            qw = (matrix.m02 - matrix.m20) / S;
            qx = (matrix.m01 + matrix.m10) / S;
            qy = 0.25 * S;
            qz = (matrix.m12 + matrix.m21) / S;
        }
        else
        {
            double S = Math.sqrt(1.0 + matrix.m22 - matrix.m00 - matrix.m11) * 2; // S=4*qz
            qw = (matrix.m10 - matrix.m01) / S;
            qx = (matrix.m02 + matrix.m20) / S;
            qy = (matrix.m12 + matrix.m21) / S;
            qz = 0.25 * S;
        }

        return new Quaternion((float)qw, (float)qx, (float)qy, (float)qz);
    }

    public static void removeMatrixRotation(Matrix4f matrix)
    {
        matrix.m00 = 1;
        matrix.m11 = 1;
        matrix.m22 = 1;
        matrix.m01 = 0;
        matrix.m02 = 0;
        matrix.m10 = 0;
        matrix.m12 = 0;
        matrix.m20 = 0;
        matrix.m21 = 0;
    }

    public static void removeMatrixTranslation(Matrix4f matrix)
    {
        matrix.m03 = 0;
        matrix.m13 = 0;
        matrix.m23 = 0;
    }

    public static Quaternion betweenVectors(Vec3 u, Vec3 v) {
        Vec3 a = u.cross(v);
        float w = (float) (Math.sqrt(u.lengthSqr() * v.lengthSqr()) + u.dot(v));
        Quaternion q = new Quaternion((float) a.x(), -(float) a.y(), -(float) a.z(), w);
        q.normalize();
        return q;
    }

    public static Vector3f translationFromMatrix(Matrix4f matrix4f) {
        return new Vector3f(matrix4f.m03, matrix4f.m13, matrix4f.m23);
    }

    public static Vector3f eulerAnglesZYXFromMatrix(Matrix4f matrix4f) {
        // From https://www.geometrictools.com/Documentation/EulerAngles.pdf
        float thetaZ;
        float thetaY;
        float thetaX;
        if (matrix4f.m20 < 1f) {
            if (matrix4f.m20 > -1f) {
                thetaY = (float) Math.asin(-matrix4f.m20);
                thetaZ = (float) Math.atan2(matrix4f.m10, matrix4f.m00) ;
                thetaX = (float) Math.atan2(matrix4f.m21, matrix4f.m22);
            }
            else { // m20 = −1
                thetaY = (float) (Math.PI / 2f);
                thetaZ = -(float) Math.atan2(-matrix4f.m12, matrix4f.m11);
                thetaX = 0;
            }
        }
        else { // m20 = +1
            thetaY = -(float) (Math.PI / 2f);
            thetaZ = (float) Math.atan2(-matrix4f.m12, matrix4f.m11);
            thetaX = 0;
        }
        return new Vector3f(thetaX, thetaY, thetaZ);
    }

    public static Vector3f eulerAnglesXYZFromMatrix(Matrix4f matrix4f) {
        // From https://www.geometrictools.com/Documentation/EulerAngles.pdf
        float thetaZ;
        float thetaY;
        float thetaX;
        if (matrix4f.m20 < 1f) {
            if (matrix4f.m20 > -1f) {
                thetaY = (float) Math.asin(matrix4f.m02);
                thetaX = (float) Math.atan2(-matrix4f.m12, matrix4f.m22) ;
                thetaZ = (float) Math.atan2(-matrix4f.m01, matrix4f.m00);
            }
            else { // m20 = −1
                thetaY = -(float) (Math.PI / 2f);
                thetaX = -(float) Math.atan2(matrix4f.m10, matrix4f.m11);
                thetaZ = 0;
            }
        }
        else { // m20 = +1
            thetaY = (float) (Math.PI / 2f);
            thetaX = (float) Math.atan2(matrix4f.m10, matrix4f.m11);
            thetaZ = 0;
        }
        return new Vector3f(thetaX, thetaY, thetaZ);
    }
}
