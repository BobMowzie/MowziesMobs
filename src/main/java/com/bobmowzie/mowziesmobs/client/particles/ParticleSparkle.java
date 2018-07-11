package com.bobmowzie.mowziesmobs.client.particles;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory;
import com.bobmowzie.mowziesmobs.client.particle.ParticleTextureStitcher;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Josh on 6/2/2017.
 */
public class ParticleSparkle extends Particle implements ParticleTextureStitcher.IParticleSpriteReceiver {
    private float red, green, blue;
    private float scale;

    public ParticleSparkle(World world, double x, double y, double z, double vx, double vy, double vz, double r, double g, double b, double scale, int duration) {
        super(world, x, y, z);
        this.scale = (float) scale * 0.5f;
        particleMaxAge = duration;
        motionX = vx;
        motionY = vy;
        motionZ = vz;
        red = (float) r;
        green = (float) g;
        blue = (float) b;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (particleAge >= particleMaxAge) {
            setExpired();
        }
        particleAge++;
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float a = ((float)particleAge + partialTicks)/particleMaxAge;
        particleAlpha = -4 * a * a + 4 * a;
        if (particleAlpha < 0.01) particleAlpha = 0.01f;
        particleScale = (-4 * a * a + 4 * a) * scale;

        //TODO: Override rendering to support rgb parameters
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    @Override
    public void setParticleTextureIndex(int particleTextureIndex) {
        if (this.getFXLayer() != 0)
        {
        }
        else
        {
            this.particleTextureIndexX = particleTextureIndex % 16;
            this.particleTextureIndexY = particleTextureIndex / 16;
        }
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

    @Override
    public boolean shouldDisableDepth() {
        return true;
    }
}
