package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleData;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleHandler {

    public static final DeferredRegister<ParticleType<?>> REG = new DeferredRegister<>(ForgeRegistries.PARTICLE_TYPES, MowziesMobs.MODID);

    public static final RegistryObject<BasicParticleType> SPARKLE = REG.register("sparkle", () -> new BasicParticleType(false));
    public static final RegistryObject<ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>> VANILLA_CLOUD_EXTENDED = REG.register("vanilla_cloud_extended", () -> new ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>(false, ParticleVanillaCloudExtended.VanillaCloudData.DESERIALIZER));
    public static final RegistryObject<ParticleType<ParticleSnowFlake.SnowflakeData>> SNOWFLAKE = REG.register("snowflake", () -> new ParticleType<>(false, ParticleSnowFlake.SnowflakeData.DESERIALIZER));
    public static final RegistryObject<ParticleType<ParticleCloud.CloudData>> CLOUD = REG.register("cloud_soft", () -> new ParticleType<>(false, ParticleCloud.CloudData.DESERIALIZER));
    public static final RegistryObject<ParticleType<ParticleOrb.OrbData>> ORB = REG.register("orb_0", () -> new ParticleType<>(false, ParticleOrb.OrbData.DESERIALIZER));
    public static final RegistryObject<ParticleType<ParticleRing.RingData>> RING = REG.register("ring_0", () -> new ParticleType<>(false, ParticleRing.RingData.DESERIALIZER));
    public static final RegistryObject<ParticleType<ParticleFallingBlock.FallingBlockData>> FALLING_BLOCK = REG.register("falling_block", () -> new ParticleType<>(false, ParticleFallingBlock.FallingBlockData.DESERIALIZER));

    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING2 = REG.register("ring", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING_BIG = REG.register("ring_big", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> PIXEL = REG.register("pixel", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ORB2 = REG.register("orb", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> EYE = REG.register("eye", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BUBBLE = REG.register("bubble", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SUN = REG.register("sun", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SUN_NOVA = REG.register("sun_nova", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> FLARE = REG.register("flare", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> FLARE_RADIAL = REG.register("flare_radial", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_IN = REG.register("ring1", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_MESSY = REG.register("burst_messy", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING_SPARKS = REG.register("sparks_ring", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_OUT = REG.register("ring2", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> GLOW = REG.register("glow", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ARROW_HEAD = REG.register("arrow_head", () -> new ParticleType<>(false, AdvancedParticleData.DESERIALIZER));

    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_FLAT = REG.register("ribbon_flat", () -> new ParticleType<>(false, RibbonParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_STREAKS = REG.register("ribbon_streaks", () -> new ParticleType<>(false, RibbonParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_GLOW = REG.register("ribbon_glow", () -> new ParticleType<>(false, RibbonParticleData.DESERIALIZER));

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SPARKLE.get(), ParticleSparkle.SparkleFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.VANILLA_CLOUD_EXTENDED.get(), ParticleVanillaCloudExtended.CloudFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SNOWFLAKE.get(), ParticleSnowFlake.SnowFlakeFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.CLOUD.get(), ParticleCloud.CloudFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.ORB.get(), ParticleOrb.OrbFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RING.get(), ParticleRing.RingFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.FALLING_BLOCK.get(), ParticleFallingBlock.FallingBlockFactory::new);

        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RING2.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RING_BIG.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.PIXEL.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.ORB2.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.EYE.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.BUBBLE.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SUN.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SUN_NOVA.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.FLARE.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.FLARE_RADIAL.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.BURST_IN.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.BURST_MESSY.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RING_SPARKS.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.BURST_OUT.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.GLOW.get(), AdvancedParticleBase.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.ARROW_HEAD.get(), AdvancedParticleBase.Factory::new);

        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RIBBON_FLAT.get(), ParticleRibbon.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RIBBON_STREAKS.get(), ParticleRibbon.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RIBBON_GLOW.get(), ParticleRibbon.Factory::new);
    }
}
