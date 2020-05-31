package com.bobmowzie.mowziesmobs.client.particle;

import javax.annotation.Nullable;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particles.*;
import com.bobmowzie.mowziesmobs.client.particles.util.MowzieParticleBase;
import com.bobmowzie.mowziesmobs.client.particles.util.ParticleRibbon;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.client.particle.ParticleFactory.ParticleArgs;

public enum MMParticle {
    ORB(new ParticleOrb.OrbFactory()),
    SNOWFLAKE(new ParticleSnowFlake.SnowFlakeFactory()),
    RING(new ParticleRing.RingFactory()),
    RING2(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/ring"))),
    RING_BIG(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/ring_big"))),
    CLOUD(new ParticleCloud.CloudFactory()),
    VANILLA_CLOUD(new ParticleVanillaCloudExtended.CloudFactory()),
    PIXEL(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/pixel"))),
    ORB2(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/orb"))),
    EYE(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/eye"))),
    BUBBLE(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/bubble"))),
    SUN(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/sun"))),
    SUN_NOVA(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/sun_nova"))),
    FLARE(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/flare"))),
    FLARE_RADIAL(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/flare_radial"))),
    BURST_IN(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/ring1"))),
    BURST_MESSY(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/burst_messy"))),
    RING_SPARKS(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/sparks_ring"))),
    BURST_OUT(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/ring2"))),
    GLOW(new MowzieParticleBase.ParticleBaseFactory(new ResourceLocation(MowziesMobs.MODID, "particles/glow"))),
    RIBBON_FLAT(new ParticleRibbon.ParticleRibbonFactory(new ResourceLocation(MowziesMobs.MODID, "particles/trail_flat"))),
    RIBBON_STREAKS(new ParticleRibbon.ParticleRibbonFactory(new ResourceLocation(MowziesMobs.MODID, "particles/trail_streaks"))),
    RIBBON_GLOW(new ParticleRibbon.ParticleRibbonFactory(new ResourceLocation(MowziesMobs.MODID, "particles/trail_glow"))),
    SPARKLE(new ParticleSparkle.SparkleFactory()),
    FALLING_BLOCK(new ParticleFallingBlock.FallingBlockFactory());

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
