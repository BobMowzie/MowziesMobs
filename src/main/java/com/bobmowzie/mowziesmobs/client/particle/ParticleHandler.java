package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleData;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonParticleData;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleHandler {

    public static final DeferredRegister<ParticleType<?>> REG = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MowziesMobs.MODID);

    public static final RegistryObject<BasicParticleType> SPARKLE = register("sparkle", false);
    public static final RegistryObject<ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>> VANILLA_CLOUD_EXTENDED = register("vanilla_cloud_extended", ParticleVanillaCloudExtended.VanillaCloudData.DESERIALIZER);
    public static final RegistryObject<ParticleType<ParticleSnowFlake.SnowflakeData>> SNOWFLAKE = register("snowflake", ParticleSnowFlake.SnowflakeData.DESERIALIZER);
    public static final RegistryObject<ParticleType<ParticleCloud.CloudData>> CLOUD = register("cloud_soft", ParticleCloud.CloudData.DESERIALIZER);
    public static final RegistryObject<ParticleType<ParticleOrb.OrbData>> ORB = register("orb_0", ParticleOrb.OrbData.DESERIALIZER);
    public static final RegistryObject<ParticleType<ParticleRing.RingData>> RING = register("ring_0", ParticleRing.RingData.DESERIALIZER);

    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING2 = register("ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING_BIG = register("ring_big", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> PIXEL = register("pixel", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ORB2 = register("orb", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> EYE = register("eye", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BUBBLE = register("bubble", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SUN = register("sun", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SUN_NOVA = register("sun_nova", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> FLARE = register("flare", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> FLARE_RADIAL = register("flare_radial", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_IN = register("ring1", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_MESSY = register("burst_messy", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING_SPARKS = register("sparks_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_OUT = register("ring2", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> GLOW = register("glow", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ARROW_HEAD = register("arrow_head", AdvancedParticleData.DESERIALIZER);

    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_FLAT = register("ribbon_flat", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_STREAKS = register("ribbon_streaks", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_GLOW = register("ribbon_glow", RibbonParticleData.DESERIALIZER);

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SPARKLE.get(), ParticleSparkle.SparkleFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.VANILLA_CLOUD_EXTENDED.get(), ParticleVanillaCloudExtended.CloudFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SNOWFLAKE.get(), ParticleSnowFlake.SnowFlakeFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.CLOUD.get(), ParticleCloud.CloudFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.ORB.get(), ParticleOrb.OrbFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RING.get(), ParticleRing.RingFactory::new);

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

    private static RegistryObject<BasicParticleType> register(String key, boolean alwaysShow) {
        return REG.register(key, () -> new BasicParticleType(alwaysShow));
    }

    private static <T extends IParticleData> RegistryObject<ParticleType<T>> register(String key, IParticleData.IDeserializer<T> deserializer) {
        return REG.register(key, () -> new ParticleType<T>(false, deserializer) {
            public Codec<T> func_230522_e_() {
                return null;
            }
        });
    }
}
