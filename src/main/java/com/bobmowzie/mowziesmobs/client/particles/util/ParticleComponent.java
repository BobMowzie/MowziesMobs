package com.bobmowzie.mowziesmobs.client.particles.util;

import net.minecraft.util.math.Vec3d;

public class ParticleComponent {
    public ParticleComponent() {

    }

    public void update(MowzieParticleBase particle) {

    }

    public static class KeyTrack {
        float[] values;
        float[] times;

        public KeyTrack(float[] values, float[] times) {
            this.values = values;
            this.times = times;
            if (values.length != times.length) System.out.println("Malformed key track. Must have same number of keys and values or key track will evaluate to 0.");
        }

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

    public static class PropertyControl extends ParticleComponent {
        public enum EnumParticleProperty {
            POS_X, POS_Y, POS_Z,
            MOTION_X, MOTION_Y, MOTION_Z,
            RED, GREEN, BLUE, ALPHA,
            SCALE,
            YAW, PITCH, ROLL,
            AIR_DRAG
        }

        private KeyTrack keyTrack;
        private EnumParticleProperty property;
        private boolean additive;
        public PropertyControl(EnumParticleProperty property, KeyTrack keyTrack, boolean additive) {
            this.property = property;
            this.keyTrack = keyTrack;
            this.additive = additive;
        }

        @Override
        public void update(MowzieParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            float value = keyTrack.evaluate(ageFrac);
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
            else if (property == EnumParticleProperty.RED) {
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
            else if (property == EnumParticleProperty.AIR_DRAG) {
                if (additive) particle.airDrag += value;
                else particle.airDrag = value;
            }
        }
    }

    public static class AlphaControl extends ParticleComponent {
        float startAlpha, endAlpha;
        public AlphaControl(float startAlpha, float endAlpha) {
            this.startAlpha = startAlpha;
            this.endAlpha = endAlpha;
        }

        @Override
        public void update(MowzieParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            particle.alpha = startAlpha * (1f - ageFrac) + endAlpha * ageFrac;
        }
    }

    public static class SizeControl extends ParticleComponent {
        float startAlpha, endAlpha;
        public SizeControl(float startAlpha, float endAlpha) {
            this.startAlpha = startAlpha;
            this.endAlpha = endAlpha;
        }

        @Override
        public void update(MowzieParticleBase particle) {
            float ageFrac = particle.getAge() / particle.getMaxAge();
            particle.alpha = startAlpha * (1f - ageFrac) + endAlpha * ageFrac;
        }
    }

    public static class PinLocation extends ParticleComponent {
        private Vec3d[] location;

        public PinLocation(Vec3d[] location) {
            this.location = location;
        }

        @Override
        public void update(MowzieParticleBase particle) {
            if (location.length > 0) {
                particle.setPosition(location[0].x, location[0].y, location[0].z);
            }
        }
    }
}
