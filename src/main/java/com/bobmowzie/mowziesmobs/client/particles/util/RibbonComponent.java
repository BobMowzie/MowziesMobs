package com.bobmowzie.mowziesmobs.client.particles.util;

import com.bobmowzie.mowziesmobs.client.particle.MMParticle;

public class RibbonComponent extends ParticleComponent {
    int length;
    MMParticle ribbon;

    public RibbonComponent(int length, MMParticle particle) {
        this.length = length;
        if (particle.getFactory() instanceof ParticleRibbon.ParticleRibbonFactory) {
            this.ribbon = particle;
        }
        else {
            System.out.println("Specified non-ribbon particle for ribbon component");
            this.ribbon = null;
        }
    }

    @Override
    public void init(MowzieParticleBase particle) {
        super.init(particle);
        if (particle != null) {
            ParticleRibbon.spawnRibbon(particle.getWorld(), ribbon, length, particle.getPosX(), particle.getPosY(), particle.getPosZ(), 0, 0, 0, true, 0, 0, 0, 1F, 1, 1, 1, 1, 1, particle.getMaxAge(), true, new ParticleComponent[]{
                    new AttachToParticle(particle)
            });
        }
    }

    private static class AttachToParticle extends ParticleComponent {
        MowzieParticleBase attachedParticle;

        public AttachToParticle(MowzieParticleBase attachedParticle) {
            this.attachedParticle = attachedParticle;
        }

        @Override
        public void init(MowzieParticleBase particle) {
            super.init(particle);
            attachedParticle.ribbon = (ParticleRibbon) particle;
        }
    }
}
