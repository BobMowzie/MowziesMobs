package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleData;
import com.bobmowzie.mowziesmobs.client.particle.util.DecalParticleData;
import com.bobmowzie.mowziesmobs.client.particle.util.RibbonParticleData;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleHandler {

    public static final DeferredRegister<ParticleType<?>> REG = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MowziesMobs.MODID);

    public static final RegistryObject<SimpleParticleType> SPARKLE = register("sparkle", false);
    public static final RegistryObject<ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>> VANILLA_CLOUD_EXTENDED = REG.register("vanilla_cloud_extended", () -> new ParticleType<ParticleVanillaCloudExtended.VanillaCloudData>(false, ParticleVanillaCloudExtended.VanillaCloudData.DESERIALIZER) {
        @Override
        public Codec<ParticleVanillaCloudExtended.VanillaCloudData> codec() {
            return ParticleVanillaCloudExtended.VanillaCloudData.CODEC(VANILLA_CLOUD_EXTENDED.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleSnowFlake.SnowflakeData>> SNOWFLAKE = REG.register("snowflake", () -> new ParticleType<ParticleSnowFlake.SnowflakeData>(false, ParticleSnowFlake.SnowflakeData.DESERIALIZER) {
        @Override
        public Codec<ParticleSnowFlake.SnowflakeData> codec() {
            return ParticleSnowFlake.SnowflakeData.CODEC(SNOWFLAKE.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleCloud.CloudData>> CLOUD = REG.register("cloud_soft", () -> new ParticleType<ParticleCloud.CloudData>(false, ParticleCloud.CloudData.DESERIALIZER) {
        @Override
        public Codec<ParticleCloud.CloudData> codec() {
            return ParticleCloud.CloudData.CODEC(CLOUD.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleOrb.OrbData>> ORB = REG.register("orb_0", () -> new ParticleType<ParticleOrb.OrbData>(false, ParticleOrb.OrbData.DESERIALIZER) {
        @Override
        public Codec<ParticleOrb.OrbData> codec() {
            return ParticleOrb.OrbData.CODEC(ORB.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleRing.RingData>> RING = REG.register("ring_0", () -> new ParticleType<ParticleRing.RingData>(false, ParticleRing.RingData.DESERIALIZER) {
        @Override
        public Codec<ParticleRing.RingData> codec() {
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

    public static final RegistryObject<ParticleType<DecalParticleData>> STRIX_FOOTPRINT = registerDecal("strix_footprint", DecalParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<DecalParticleData>> GROUND_CRACK = registerDecal("crack", DecalParticleData.DESERIALIZER);

    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_FLAT = registerRibbon("ribbon_flat", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_STREAKS = registerRibbon("ribbon_streaks", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_GLOW = registerRibbon("ribbon_glow", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> RIBBON_SQUIGGLE = registerRibbon("ribbon_squiggle", RibbonParticleData.DESERIALIZER);

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleHandler.SPARKLE.get(), ParticleSparkle.SparkleFactory::new);
        event.registerSpriteSet(ParticleHandler.VANILLA_CLOUD_EXTENDED.get(), ParticleVanillaCloudExtended.CloudFactory::new);
        event.registerSpriteSet(ParticleHandler.SNOWFLAKE.get(), ParticleSnowFlake.SnowFlakeFactory::new);
        event.registerSpriteSet(ParticleHandler.CLOUD.get(), ParticleCloud.CloudFactory::new);
        event.registerSpriteSet(ParticleHandler.ORB.get(), ParticleOrb.OrbFactory::new);
        event.registerSpriteSet(ParticleHandler.RING.get(), ParticleRing.RingFactory::new);

        event.registerSpriteSet(ParticleHandler.RING2.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.RING_BIG.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.PIXEL.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.ORB2.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.EYE.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.BUBBLE.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.SUN.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.SUN_NOVA.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.FLARE.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.FLARE_RADIAL.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.BURST_IN.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.BURST_MESSY.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.RING_SPARKS.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.BURST_OUT.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.GLOW.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.ARROW_HEAD.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleHandler.STRIX_FOOTPRINT.get(), ParticleDecal.Factory::new);
        event.registerSpriteSet(ParticleHandler.GROUND_CRACK.get(), ParticleDecal.Factory::new);

        event.registerSpriteSet(ParticleHandler.RIBBON_FLAT.get(), ParticleRibbon.Factory::new);
        event.registerSpriteSet(ParticleHandler.RIBBON_STREAKS.get(), ParticleRibbon.Factory::new);
        event.registerSpriteSet(ParticleHandler.RIBBON_GLOW.get(), ParticleRibbon.Factory::new);
        event.registerSpriteSet(ParticleHandler.RIBBON_SQUIGGLE.get(), ParticleRibbon.Factory::new);
    }

    private static RegistryObject<SimpleParticleType> register(String key, boolean alwaysShow) {
        return REG.register(key, () -> new SimpleParticleType(alwaysShow));
    }

    private static RegistryObject<ParticleType<AdvancedParticleData>> register(String key, ParticleOptions.Deserializer<AdvancedParticleData> deserializer) {
        return REG.register(key, () -> new ParticleType<AdvancedParticleData>(false, deserializer) {
            public Codec<AdvancedParticleData> codec() {
                return AdvancedParticleData.CODEC(this);
            }
        });
    }

    private static RegistryObject<ParticleType<DecalParticleData>> registerDecal(String key, ParticleOptions.Deserializer<DecalParticleData> deserializer) {
        return REG.register(key, () -> new ParticleType<DecalParticleData>(false, deserializer) {
            public Codec<DecalParticleData> codec() {
                return DecalParticleData.CODEC_RIBBON(this);
            }
        });
    }

    private static RegistryObject<ParticleType<RibbonParticleData>> registerRibbon(String key, ParticleOptions.Deserializer<RibbonParticleData> deserializer) {
        return REG.register(key, () -> new ParticleType<RibbonParticleData>(false, deserializer) {
            public Codec<RibbonParticleData> codec() {
                return RibbonParticleData.CODEC_RIBBON(this);
            }
        });
    }
}
