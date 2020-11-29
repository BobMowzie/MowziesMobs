package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particles.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particles.util.AdvancedParticleData;
import net.minecraft.client.Minecraft;
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

    public static final RegistryObject<ParticleType<AdvancedParticleData>> SPARKLE = REG.register("sparkle", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));

    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING2 = REG.register("ring", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING_BIG = REG.register("ring_big", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> PIXEL = REG.register("pixel", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ORB2 = REG.register("orb", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> EYE = REG.register("eye", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BUBBLE = REG.register("bubble", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SUN = REG.register("sun", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SUN_NOVA = REG.register("sun_nova", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> FLARE = REG.register("flare", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> FLARE_RADIAL = REG.register("flare_radial", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_IN = REG.register("ring1", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_MESSY = REG.register("burst_messy", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> RING_SPARKS = REG.register("sparks_ring", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BURST_OUT = REG.register("ring2", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> GLOW = REG.register("glow", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ARROW_HEAD = REG.register("arrow_head", () -> new ParticleType<AdvancedParticleData>(false, AdvancedParticleData.DESERIALIZER));

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ParticleHandler.SPARKLE.get(), AdvancedParticleBase.Factory::new);
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
    }
}
