package com.bobmowzie.mowziesmobs.client.particle;

import javax.annotation.Nullable;

import com.bobmowzie.mowziesmobs.client.particles.*;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory.ParticleArgs;

public enum MMParticle {
    ORB(new ParticleOrb.OrbFactory()),
    SNOWFLAKE(new ParticleSnowFlake.SnowFlakeFactory()),
    RING(new ParticleRing.RingFactory()),
    CLOUD(new ParticleCloud.CloudFactory()),
    SPARKLE(new ParticleSparkle.SparkleFactory());

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
