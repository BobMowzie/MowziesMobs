package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particles.util.AdvancedParticleData;
import net.minecraft.client.particle.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SparkleParticle extends SpriteTexturedParticle {
    protected SparkleParticle(World worldIn, double x, double y, double z, double vx, double vy, double vz) {
        super(worldIn, x, y, z, vx, vy, vz);
        this.canCollide = false;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<AdvancedParticleData> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(AdvancedParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkleParticle particle = new SparkleParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
//            particle.setColor(typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue());
            particle.selectSpriteRandomly(spriteSet);
            return particle;
        }
    }
}
