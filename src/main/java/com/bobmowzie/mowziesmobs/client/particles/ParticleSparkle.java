package com.bobmowzie.mowziesmobs.client.particles;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particle.ParticleTextureStitcher;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Josh on 6/2/2017.
 */
public class ParticleSparkle extends Particle implements ParticleTextureStitcher.IParticleSpriteReceiver {
    private float red, green, blue;
    private float scale;

    public ParticleSparkle(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, double scale, int duration) {
        super(world, x, y, z);
        this.scale = (float) scale * 1f;
        maxAge = duration;
        motionX = vx;
        motionY = vy;
        motionZ = vz;
        red = (float) r;
        green = (float) g;
        blue = (float) b;
    }

    @Override
    public void tick() {
        super.tick();
        if (age >= maxAge) {
            setExpired();
        }
        age++;
    }

    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float a = ((float)age + partialTicks)/maxAge;
        particleAlpha = -4 * a * a + 4 * a;
        if (particleAlpha < 0.01) particleAlpha = 0.01f;
        setSize((-4 * a * a + 4 * a) * scale, (-4 * a * a + 4 * a) * scale);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static final class SparkleFactory extends ParticleFactory<ParticleSparkle.SparkleFactory, ParticleSparkle> {
        public SparkleFactory() {
            super(ParticleSparkle.class, ParticleTextureStitcher.create(ParticleSparkle.class, new ResourceLocation(MowziesMobs.MODID, "particles/sparkle")));
        }

        @Override
        public ParticleSparkle createParticle(ImmutableParticleArgs args) {
            if (args.data.length >= 8) return new ParticleSparkle(args.world, args.x, args.y, args.z, (double) args.data[0], (double) args.data[1], (double) args.data[2], (double) args.data[3], (double) args.data[4], (double) args.data[5], (double) args.data[6], (int) args.data[7]);
            return new ParticleSparkle(args.world, args.x, args.y, args.z, 0, 0, 0, 1, 1, 1, 10, 40);
        }
    }
}
