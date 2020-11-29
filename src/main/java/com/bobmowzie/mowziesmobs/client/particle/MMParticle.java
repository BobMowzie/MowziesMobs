package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory.ParticleArgs;
import com.bobmowzie.mowziesmobs.client.particles.*;
import com.bobmowzie.mowziesmobs.client.particles.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleRibbon;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public enum MMParticle {
    ORB(new ParticleOrb.OrbFactory(null)),
    SNOWFLAKE(new ParticleSnowFlake.SnowFlakeFactory(null)),
    RING(new ParticleRing.RingFactory(null)),
    CLOUD(new ParticleCloud.CloudFactory(null)),
    VANILLA_CLOUD(new ParticleVanillaCloudExtended.CloudFactory(null)),
    RIBBON_FLAT(new ParticleRibbon.ParticleRibbonFactory(new ResourceLocation(MowziesMobs.MODID, "particles/trail_flat"))),
    RIBBON_STREAKS(new ParticleRibbon.ParticleRibbonFactory(new ResourceLocation(MowziesMobs.MODID, "particles/trail_streaks"))),
    RIBBON_GLOW(new ParticleRibbon.ParticleRibbonFactory(new ResourceLocation(MowziesMobs.MODID, "particles/trail_glow"))),
    SPARKLE(new ParticleSparkle.SparkleFactory(null)),
    FALLING_BLOCK(new ParticleFallingBlock.FallingBlockFactory(null));

    private ParticleFactory factory;

    private MMParticle(ParticleFactory<?, ?> factory) {
        this.factory = factory;
    }

    public ParticleFactory getFactory() {
        return factory;
    }

    /**
     * Creates a new instance of this particle
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Particle create(World world, double x, double y, double z) {
        return create(world, x, y, z, null);
    }

    /**
     * Creates a new instance of this particle
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param args
     * @return
     */
    public Particle create(World world, double x, double y, double z, @Nullable ParticleArgs args) {
        return factory.create(world, x, y, z, args);
    }

    /**
     * Spawns this particle
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Particle spawn(World world, double x, double y, double z) {
        return spawn(world, x, y, z, null);
    }

    /**
     * Spawns this particle
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param args
     * @return
     */
    public Particle spawn(World world, double x, double y, double z, @Nullable ParticleArgs args) {
        return factory.spawn(world, x, y, z, args);
    }
}
