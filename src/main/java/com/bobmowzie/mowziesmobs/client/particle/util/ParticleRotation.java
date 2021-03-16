package com.bobmowzie.mowziesmobs.client.particle.util;
import net.minecraft.util.math.vector.Vector3d;

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
        public Vector3d orientation;
        public Vector3d prevOrientation;
        public OrientVector(Vector3d orientation) {
            this.orientation = this.prevOrientation = orientation;
        }

        @Override
        public void setPrevValues() {
            prevOrientation = orientation;
        }
    }
}
