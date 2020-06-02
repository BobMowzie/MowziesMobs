package com.bobmowzie.mowziesmobs.client.particles.util;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3d;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

public abstract class ParticleComponent {
    public ParticleComponent() {

    }

    public void init(MowzieParticleBase particle) {

    }

    public void preUpdate(MowzieParticleBase particle) {

    }

    public void postUpdate(MowzieParticleBase particle) {

    }

    public void preRender(MowzieParticleBase particle, float partialTicks) {

    }

    public void postRender(MowzieParticleBase particle, BufferBuilder buffer, float partialTicks, int lightmapJ, int lightmapK) {

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

        private AnimData animData;
        private EnumParticleProperty property;
        private boolean additive;
        public PropertyControl(EnumParticleProperty property, AnimData animData, boolean additive) {
            this.property = property;
            this.animData = animData;
            this.additive = additive;
        }

        @Override
        public void init(MowzieParticleBase particle) {
            float value = animData.evaluate(0);
            applyUpdate(particle, value);
            applyRender(particle, value);
        }

        @Override
        public void preRender(MowzieParticleBase particle, float partialTicks) {
            float ageFrac = (particle.getAge() + partialTicks) / particle.getMaxAge();
            float value = animData.evaluate(ageFrac);
            applyRender(particle, value);
        }

        @Override
        public void preUpdate(MowzieParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            float value = animData.evaluate(ageFrac);
            applyUpdate(particle, value);
        }

        private void applyUpdate(MowzieParticleBase particle, float value) {
            if (property == EnumParticleProperty.POS_X) {
                if (additive) particle.setPosX(particle.getPosX() + value);
                else particle.setPosX(value);
            }
            else if (property == EnumParticleProperty.POS_Y) {
                if (additive) particle.setPosY(particle.getPosY() + value);
                else particle.setPosY(value);
            }
            else if (property == EnumParticleProperty.POS_Z) {
                if (additive) particle.setPosZ(particle.getPosZ() + value);
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

        private void applyRender(MowzieParticleBase particle, float value) {
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
                if (additive) particle.yaw += value;
                else particle.yaw = value;
            }
            else if (property == EnumParticleProperty.PITCH) {
                if (additive) particle.pitch += value;
                else particle.pitch = value;
            }
            else if (property == EnumParticleProperty.ROLL) {
                if (additive) particle.roll += value;
                else particle.roll = value;
            }
            else if (property == EnumParticleProperty.PARTICLE_ANGLE) {
                if (additive) particle.setAngle(particle.getAngle() + value);
                else particle.setAngle(value);
            }
        }
    }

    public static class PinLocation extends ParticleComponent {
        private Vec3d[] location;

        public PinLocation(Vec3d[] location) {
            this.location = location;
        }

        @Override
        public void init(MowzieParticleBase particle) {
            if (location.length > 0) {
                particle.setPosition(location[0].x, location[0].y, location[0].z);
            }
        }

        @Override
        public void preUpdate(MowzieParticleBase particle) {
            if (location.length > 0) {
                particle.setPosition(location[0].x, location[0].y, location[0].z);
            }
        }
    }

    public static class Attractor extends ParticleComponent {
        public enum EnumAttractorBehavior {
            LINEAR,
            EXPONENTIAL,
            SIMULATED,
        }

        private Vec3d[] location;
        private float strength;
        private float killDist;
        private EnumAttractorBehavior behavior;
        private Vec3d startLocation;

        public Attractor(Vec3d[] location, float strength, float killDist, EnumAttractorBehavior behavior) {
            this.location = location;
            this.strength = strength;
            this.killDist = killDist;
            this.behavior = behavior;
        }

        @Override
        public void init(MowzieParticleBase particle) {
            startLocation = new Vec3d(particle.getPosX(), particle.getPosY(), particle.getPosZ());
        }

        @Override
        public void preUpdate(MowzieParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            if (location.length > 0) {
                Vec3d destinationVec = location[0];
                Vec3d currPos = new Vec3d(particle.getPosX(), particle.getPosY(), particle.getPosZ());
                Vec3d diff = destinationVec.subtract(currPos);
                if (diff.length() < killDist) particle.setExpired();
                if (behavior == EnumAttractorBehavior.EXPONENTIAL) {
                    Vec3d path = destinationVec.subtract(startLocation).scale(Math.pow(ageFrac, strength)).add(startLocation).subtract(currPos);
                    particle.move(path.x, path.y, path.z);
                }
                else if (behavior == EnumAttractorBehavior.LINEAR) {
                    Vec3d path = destinationVec.subtract(startLocation).scale(ageFrac).add(startLocation).subtract(currPos);
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
        private Vec3d[] location;
        private AnimData phase;
        private AnimData radius;
        private AnimData axisX;
        private AnimData axisY;
        private AnimData axisZ;
        private boolean faceCamera;

        public Orbit(Vec3d[] location, AnimData phase, AnimData radius, AnimData axisX, AnimData axisY, AnimData axisZ, boolean faceCamera) {
            this.location = location;
            this.phase = phase;
            this.radius = radius;
            this.axisX = axisX;
            this.axisY = axisY;
            this.axisZ = axisZ;
            this.faceCamera = faceCamera;
        }

        @Override
        public void init(MowzieParticleBase particle) {
            apply(particle, 0);
        }

        @Override
        public void preUpdate(MowzieParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            apply(particle, ageFrac);
        }

        private void apply(MowzieParticleBase particle, float t) {
            float p = phase.evaluate(t);
            float r = radius.evaluate(t);
            Vec3d axis;
            if (faceCamera) {
                axis = Particle.cameraViewDir;
            }
            else {
                axis = new Vec3d(axisX.evaluate(t), axisY.evaluate(t), axisZ.evaluate(t));
                axis = axis.normalize();
            }

            Matrix4d quat = new Matrix4d();
            quat.setIdentity();
            AxisAngle4d axisAngle = new AxisAngle4d(axis.x, axis.y,axis.z, p * (float) Math.PI * 2);
            quat.setRotation(axisAngle);
            Vec3d up = new Vec3d(0, 1, 0);
            Vec3d start = axis.crossProduct(up).normalize();
            if (axis == up) {
                start = new Vec3d(1, 0, 0);
            }
            Point3d newPos = new Point3d(start.x, start.y, start.z);
            quat.transform(newPos);
            newPos.scale(r);

            if (location.length > 0 && location[0] != null) {
                newPos.x += location[0].x;
                newPos.y += location[0].y;
                newPos.z += location[0].z;
            }
            particle.setPosition(newPos.x, newPos.y, newPos.z);
        }
    }

    public static class FaceMotion extends ParticleComponent {
        public FaceMotion() {

        }

        @Override
        public void preRender(MowzieParticleBase particle, float partialTicks) {
            super.preRender(particle, partialTicks);
            double dx = particle.getPosX() - particle.getPrevPosX();
            double dy = particle.getPosY() - particle.getPrevPosY();
            double dz = particle.getPosZ() - particle.getPrevPosZ();
            double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (d != 0) {
                double a = dy / d;
                a = Math.max(-1, Math.min(1, a));
                float pitch = -(float) Math.asin(a);
                float yaw = -(float) (Math.atan2(dz, dx) + Math.PI);
                particle.roll = pitch;
                particle.yaw = yaw;
//                particle.roll = (float) Math.PI / 2;
            }
        }
    }
}
