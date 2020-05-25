package com.bobmowzie.mowziesmobs.client.particles.util;

public class ParticleComponent {
    public ParticleComponent() {

    }

    public void update(MowzieParticleBase particle) {

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
}
