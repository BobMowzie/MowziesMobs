package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleData;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonParticleData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.sounds.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleHandler {

    public static final DeferredRegister<ParticleType<?>> REG = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MowziesMobs.MODID);

    public static final RegistryObject<BasicParticleType> SPARKLE = register("sparkle", false);
    public static final RegistryObject<ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>> VANILLA_CLOUD_EXTENDED = REG.register("vanilla_cloud_extended", () -> new ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>(false, ParticleVanillaCloudExtended.VanillaCloudData.DESERIALIZER) {
        @Override
        public Codec<ParticleVanillaCloudExtended.VanillaCloudData> func_230522_e_() {
            return ParticleVanillaCloudExtended.VanillaCloudData.CODEC(VANILLA_CLOUD_EXTENDED.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleSnowFlake.SnowflakeData>> SNOWFLAKE = REG.register("snowflake", () -> new ParticleType<ParticleSnowFlake.SnowflakeData>(false, ParticleSnowFlake.SnowflakeData.DESERIALIZER) {
        @Override
        public Codec<ParticleSnowFlake.SnowflakeData> func_230522_e_() {
            return ParticleSnowFlake.SnowflakeData.CODEC(SNOWFLAKE.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleCloud.CloudData>> CLOUD = REG.register("cloud_soft", () -> new ParticleType<ParticleCloud.CloudData>(false, ParticleCloud.CloudData.DESERIALIZER) {
        @Override
        public Codec<ParticleCloud.CloudData> func_230522_e_() {
            return ParticleCloud.CloudData.CODEC(CLOUD.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleOrb.OrbData>> ORB = REG.register("orb_0", () -> new ParticleType<ParticleOrb.OrbData>(false, ParticleOrb.OrbData.DESERIALIZER) {
        @Override
        public Codec<ParticleOrb.OrbData> func_230522_e_() {
            return ParticleOrb.OrbData.CODEC(ORB.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleRing.RingData>> RING = REG.register("ring_0", () -> new ParticleType<ParticleRing.RingData>(false, ParticleRing.RingData.DESERIALIZER) {
        @Override
        public Codec<ParticleRing.RingData> func_230522_e_() {
            return ParticleRing.RingData.CODEC(RING.get());
        }
    });

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

    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_FLAT = registerRibbon("ribbon_flat", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_STREAKS = registerRibbon("ribbon_streaks", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_GLOW = registerRibbon("ribbon_glow", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_SQUIGGLE = registerRibbon("ribbon_squiggle", RibbonParticleData.DESERIALIZER);

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
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.RIBBON_SQUIGGLE.get(), ParticleRibbon.Factory::new);
    }

    private static RegistryObject<BasicParticleType> register(String key, boolean alwaysShow) {
        return REG.register(key, () -> new BasicParticleType(alwaysShow));
    }

    private static RegistryObject<ParticleType<AdvancedParticleData>> register(String key, IParticleData.IDeserializer<AdvancedParticleData> deserializer) {
        return REG.register(key, () -> new ParticleType<AdvancedParticleData>(false, deserializer) {
            public Codec<AdvancedParticleData> func_230522_e_() {
                return AdvancedParticleData.CODEC(this);
            }
        });
    }

    private static RegistryObject<ParticleType<RibbonParticleData>> registerRibbon(String key, IParticleData.IDeserializer<RibbonParticleData> deserializer) {
        return REG.register(key, () -> new ParticleType<RibbonParticleData>(false, deserializer) {
            public Codec<RibbonParticleData> func_230522_e_() {
                return RibbonParticleData.CODEC_RIBBON(this);
            }
        });
    }
}
