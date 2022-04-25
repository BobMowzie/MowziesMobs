package com.bobmowzie.mowziesmobs.client.particle.util;
import net.minecraft.world.phys.Vec3;

public abstract class ParticleRotation {
    public void setPrevValues() {

    }

    public static class FaceCamera extends ParticleRotation {
        public float faceCameraAngle;
        public float prevFaceCameraAngle;
        public FaceCamera(float faceCameraAngle) {
            this.faceCameraAngle = faceCameraAngle;
        }

        @Override
        public void setPrevValues() {
            prevFaceCameraAngle = faceCameraAngle;
        }
    }

    public static class EulerAngles extends ParticleRotation {
        public float yaw, pitch, roll;
        public float prevYaw, prevPitch, prevRoll;
        public EulerAngles(float yaw, float pitch, float roll) {
            this.yaw = this.prevYaw = yaw;
            this.pitch = this.prevPitch = pitch;
            this.roll = this.prevRoll = roll;
        }

        @Override
        public void setPrevValues() {
            prevYaw = yaw;
            prevPitch = pitch;
            prevRoll = roll;
        }
    }

    public static class OrientVector extends ParticleRotation {
        public Vec3 orientation;
        public Vec3 prevOrientation;
        public OrientVector(Vec3 orientation) {
            this.orientation = this.prevOrientation = orientation;
        }

        @Override
        public void setPrevValues() {
            prevOrientation = orientation;
        }
    }
}
