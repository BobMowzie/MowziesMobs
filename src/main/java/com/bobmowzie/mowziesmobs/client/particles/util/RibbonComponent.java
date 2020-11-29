package com.bobmowzie.mowziesmobs.client.particles.util;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;

public class RibbonComponent extends ParticleComponent {
    int length;
    MMParticle ribbon;
    double yaw, pitch, roll, scale, r, g, b, a;
    boolean faceCamera;
    boolean emissive;
    ParticleComponent[] components;

    public RibbonComponent(MMParticle particle, int length, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, boolean faceCamera, boolean emissive, ParticleComponent[] components) {
        this.length = length;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.scale = scale;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.emissive = emissive;
        this.faceCamera = faceCamera;
        this.components = components;

        if (particle.getFactory() instanceof ParticleRibbon.ParticleRibbonFactory) {
            this.ribbon = particle;
        }
        else {
            System.out.println("Specified non-ribbon particle for ribbon component");
            this.ribbon = null;
        }
    }

    @Override
    public void init(AdvancedParticleBase particle) {
        super.init(particle);
        if (particle != null) {

            ParticleComponent[] newComponents = new ParticleComponent[components.length + 1];
            for (int i = 0; i < components.length; i++) {
                newComponents[i] = components[i];
            }
            newComponents[components.length] = new AttachToParticle(particle);

            ParticleRibbon.spawnRibbon(particle.getWorld(), ribbon, length, particle.getPosX(), particle.getPosY(), particle.getPosZ(), 0, 0, 0, faceCamera, yaw, pitch, roll, scale, r, g, b, a, 0, particle.getMaxAge() + length, emissive, newComponents);
        }
    }

    private static class AttachToParticle extends ParticleComponent {
        AdvancedParticleBase attachedParticle;

        public AttachToParticle(AdvancedParticleBase attachedParticle) {
            this.attachedParticle = attachedParticle;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            super.init(particle);
            attachedParticle.ribbon = (ParticleRibbon) particle;
        }
    }

    public static class PropertyOverLength extends ParticleComponent {
        public enum EnumRibbonProperty {
            RED, GREEN, BLUE, ALPHA,
            SCALE
        }
        private AnimData animData;
        private EnumRibbonProperty property;

        public PropertyOverLength(EnumRibbonProperty property, AnimData animData) {
            this.animData = animData;
            this.property = property;
        }

        public float evaluate(float t) {
            return animData.evaluate(t);
        }

        public EnumRibbonProperty getProperty() {
            return property;
        }
    }
}
