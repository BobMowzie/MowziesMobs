package com.bobmowzie.mowziesmobs.client.particle.util;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.Quaternion;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vector3f;

public abstract class ParticleComponent {
    public ParticleComponent() {

    }

    public void init(AdvancedParticleBase particle) {

    }

    public void preUpdate(AdvancedParticleBase particle) {

    }

    public void postUpdate(AdvancedParticleBase particle) {

    }

    public void preRender(AdvancedParticleBase particle, float partialTicks) {

    }

    public void postRender(AdvancedParticleBase particle, IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks, int lightmap) {

    }

    public abstract static class AnimData {
        public float evaluate(float t) {
            return 0;
        }
    }

    public static class KeyTrack extends AnimData {
        float[] values;
        float[] times;

        public KeyTrack(float[] values, float[] times) {
            this.values = values;
            this.times = times;
            if (values.length != times.length) System.out.println("Malformed key track. Must have same number of keys and values or key track will evaluate to 0.");
        }

        @Override
        public float evaluate(float t) {
            if (values.length != times.length) return 0;
            for (int i = 0; i < times.length; i++) {
                float time = times[i];
                if (t == time) return values[i];
                else if (t < time) {
                    if (i == 0) return values[0];
                    float a = (t - times[i - 1]) / (time - times[i - 1]);
                    return values[i - 1] * (1 - a) + values[i] * a;
                }
                else {
                    if (i == values.length - 1) return values[i];
                }
            }
            return 0;
        }

        public static KeyTrack startAndEnd(float startValue, float endValue) {
            return new KeyTrack(new float[] {startValue, endValue}, new float[] {0, 1});
        }

        public static KeyTrack oscillate(float value1, float value2, int frequency) {
            if (frequency <= 1) new KeyTrack(new float[] {value1, value2}, new float[] {0, 1});
            float step = 1.0f / frequency;
            float[] times = new float[frequency + 1];
            float[] values = new float[frequency + 1];
            for (int i = 0; i < frequency + 1; i++) {
                float value = i % 2 == 0 ? value1 : value2;
                times[i] = step * i;
                values[i] = value;
            }
            return new KeyTrack(values, times);
        }
    }

    public static class Oscillator extends AnimData {
        float value1, value2;
        float frequency;
        float phaseShift;

        public Oscillator(float value1, float value2, float frequency, float phaseShift) {
            this.value1 = value1;
            this.value2 = value2;
            this.frequency = frequency;
            this.phaseShift = phaseShift;
        }

        @Override
        public float evaluate(float t) {
            float a = (value2 - value1) / 2f;
            return (float) (value1 + a + a * Math.cos(t * frequency + phaseShift));
        }
    }

    public static class Constant extends AnimData {
        float value;

        public Constant(float value) {
            this.value = value;
        }

        @Override
        public float evaluate(float t) {
            return value;
        }
    }

    public static Constant constant(float value) {
        return new Constant(value);
    }

    public static class PropertyControl extends ParticleComponent {
        public enum EnumParticleProperty {
            POS_X, POS_Y, POS_Z,
            MOTION_X, MOTION_Y, MOTION_Z,
            RED, GREEN, BLUE, ALPHA,
            SCALE,
            YAW, PITCH, ROLL, // For not facing camera
            PARTICLE_ANGLE, // For facing camera
            AIR_DRAG
        }

        private final AnimData animData;
        private final EnumParticleProperty property;
        private final boolean additive;
        public PropertyControl(EnumParticleProperty property, AnimData animData, boolean additive) {
            this.property = property;
            this.animData = animData;
            this.additive = additive;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            float value = animData.evaluate(0);
            applyUpdate(particle, value);
            applyRender(particle, value);
        }

        @Override
        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            float ageFrac = (particle.getAge() + partialTicks) / particle.getMaxAge();
            float value = animData.evaluate(ageFrac);
            applyRender(particle, value);
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            float value = animData.evaluate(ageFrac);
            applyUpdate(particle, value);
        }

        private void applyUpdate(AdvancedParticleBase particle, float value) {
            if (property == EnumParticleProperty.POS_X) {
                if (additive) particle.setPosX(particle.getX() + value);
                else particle.setPosX(value);
            }
            else if (property == EnumParticleProperty.POS_Y) {
                if (additive) particle.setPosY(particle.getY() + value);
                else particle.setPosY(value);
            }
            else if (property == EnumParticleProperty.POS_Z) {
                if (additive) particle.setPosZ(particle.getZ() + value);
                else particle.setPosZ(value);
            }
            else if (property == EnumParticleProperty.MOTION_X) {
                if (additive) particle.setMotionX(particle.getMotionX() + value);
                else particle.setMotionX(value);
            }
            else if (property == EnumParticleProperty.MOTION_Y) {
                if (additive) particle.setMotionY(particle.getMotionY() + value);
                else particle.setMotionY(value);
            }
            else if (property == EnumParticleProperty.MOTION_Z) {
                if (additive) particle.setMotionZ(particle.getMotionZ() + value);
                else particle.setMotionZ(value);
            }
            else if (property == EnumParticleProperty.AIR_DRAG) {
                if (additive) particle.airDrag += value;
                else particle.airDrag = value;
            }
        }

        private void applyRender(AdvancedParticleBase particle, float value) {
            if (property == EnumParticleProperty.RED) {
                if (additive) particle.red += value;
                else particle.red = value;
            }
            else if (property == EnumParticleProperty.GREEN) {
                if (additive) particle.green += value;
                else particle.green = value;
            }
            else if (property == EnumParticleProperty.BLUE) {
                if (additive) particle.blue += value;
                else particle.blue = value;
            }
            else if (property == EnumParticleProperty.ALPHA) {
                if (additive) particle.alpha += value;
                else particle.alpha = value;
            }
            else if (property == EnumParticleProperty.SCALE) {
                if (additive) particle.scale += value;
                else particle.scale = value;
            }
            else if (property == EnumParticleProperty.YAW) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles) {
                    ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) particle.rotation;
                    if (additive) eulerRot.yaw += value;
                    else eulerRot.yaw = value;
                }
            }
            else if (property == EnumParticleProperty.PITCH) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles) {
                    ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) particle.rotation;
                    if (additive) eulerRot.pitch += value;
                    else eulerRot.pitch = value;
                }
            }
            else if (property == EnumParticleProperty.ROLL) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles) {
                    ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) particle.rotation;
                    if (additive) eulerRot.roll += value;
                    else eulerRot.roll = value;
                }
            }
            else if (property == EnumParticleProperty.PARTICLE_ANGLE) {
                if (particle.rotation instanceof ParticleRotation.FaceCamera) {
                    ParticleRotation.FaceCamera faceCameraRot = (ParticleRotation.FaceCamera) particle.rotation;
                    if (additive) faceCameraRot.faceCameraAngle += value;
                    else faceCameraRot.faceCameraAngle = value;
                }
            }
        }
    }

    public static class PinLocation extends ParticleComponent {
        private final Vec3[] location;

        public PinLocation(Vec3[] location) {
            this.location = location;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            if (location != null && location.length > 0) {
                particle.setPos(location[0].x, location[0].y, location[0].z);
            }
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            if (location != null && location.length > 0) {
                particle.setPos(location[0].x, location[0].y, location[0].z);
            }
        }
    }

    public static class Attractor extends ParticleComponent {
        public enum EnumAttractorBehavior {
            LINEAR,
            EXPONENTIAL,
            SIMULATED,
        }

        private final Vec3[] location;
        private final float strength;
        private final float killDist;
        private final EnumAttractorBehavior behavior;
        private Vec3 startLocation;

        public Attractor(Vec3[] location, float strength, float killDist, EnumAttractorBehavior behavior) {
            this.location = location;
            this.strength = strength;
            this.killDist = killDist;
            this.behavior = behavior;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            startLocation = new Vec3(particle.getX(), particle.getY(), particle.getZ());
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / (particle.getMaxAge() - 1);
            if (location.length > 0) {
                Vec3 destinationVec = location[0];
                Vec3 currPos = new Vec3(particle.getX(), particle.getY(), particle.getZ());
                Vec3 diff = destinationVec.subtract(currPos);
                if (diff.length() < killDist) particle.setExpired();
                if (behavior == EnumAttractorBehavior.EXPONENTIAL) {
                    Vec3 path = destinationVec.subtract(startLocation).scale(Math.pow(ageFrac, strength)).add(startLocation).subtract(currPos);
                    particle.move(path.x, path.y, path.z);
                }
                else if (behavior == EnumAttractorBehavior.LINEAR) {
                    Vec3 path = destinationVec.subtract(startLocation).scale(ageFrac).add(startLocation).subtract(currPos);
                    particle.move(path.x, path.y, path.z);
                }
                else {
                    double dist = Math.max(diff.length(), 0.001);
                    diff = diff.normalize().scale(strength / (dist * dist));
                    particle.setMotionX(Math.min(particle.getMotionX() + diff.x, 5));
                    particle.setMotionY(Math.min(particle.getMotionY() + diff.y, 5));
                    particle.setMotionZ(Math.min(particle.getMotionZ() + diff.z, 5));
                }
            }
        }
    }

    public static class Orbit extends ParticleComponent {
        private final Vec3[] location;
        private final AnimData phase;
        private final AnimData radius;
        private final AnimData axisX;
        private final AnimData axisY;
        private final AnimData axisZ;
        private final boolean faceCamera;

        public Orbit(Vec3[] location, AnimData phase, AnimData radius, AnimData axisX, AnimData axisY, AnimData axisZ, boolean faceCamera) {
            this.location = location;
            this.phase = phase;
            this.radius = radius;
            this.axisX = axisX;
            this.axisY = axisY;
            this.axisZ = axisZ;
            this.faceCamera = faceCamera;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            apply(particle, 0);
        }

        @Override
        public void preUpdate(AdvancedParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            apply(particle, ageFrac);
        }

        private void apply(AdvancedParticleBase particle, float t) {
            float p = phase.evaluate(t);
            float r = radius.evaluate(t);
            Vector3f axis;
//            if (faceCamera) {
//                axis = Particle.cameraViewDir;
//            }
//            else {
                axis = new Vector3f(axisX.evaluate(t), axisY.evaluate(t), axisZ.evaluate(t));
                axis.normalize();
//            }

            Quaternion quat = new Quaternion(axis, p * (float) Math.PI * 2, false);
            Vector3f up = new Vector3f(0, 1, 0);
            Vector3f start = axis;
            start.cross(up);
            start.normalize();
            if (axis.equals(up)) {
                start = new Vector3f(1, 0, 0);
            }
            Vector3f newPos = start;
            newPos.transform(quat);
            newPos.mul(r);

            if (location.length > 0 && location[0] != null) {
                newPos.add((float)location[0].x, (float)location[0].y, (float)location[0].z);
            }
            particle.setPos(newPos.x(), newPos.y(), newPos.z());
        }
    }

    public static class FaceMotion extends ParticleComponent {
        public FaceMotion() {

        }

        @Override
        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            super.preRender(particle, partialTicks);
            double dx = particle.getX() - particle.getPrevPosX();
            double dy = particle.getY() - particle.getPrevPosY();
            double dz = particle.getZ() - particle.getPrevPosZ();
            double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (d != 0) {
                if (particle.rotation instanceof ParticleRotation.EulerAngles) {
                    ParticleRotation.EulerAngles eulerRot = (ParticleRotation.EulerAngles) particle.rotation;
                    double a = dy / d;
                    a = Math.max(-1, Math.min(1, a));
                    float pitch = -(float) Math.asin(a);
                    float yaw = -(float) (Math.atan2(dz, dx) + Math.PI);
                    eulerRot.roll = pitch;
                    eulerRot.yaw = yaw;
//                particle.roll = (float) Math.PI / 2;
                }
                else if (particle.rotation instanceof ParticleRotation.OrientVector) {
                    ParticleRotation.OrientVector orientRot = (ParticleRotation.OrientVector) particle.rotation;
                    orientRot.orientation = new Vec3(dx, dy, dz).normalize();
                }
            }
        }
    }
}
