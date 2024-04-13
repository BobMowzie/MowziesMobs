package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EffectHandler {
    private EffectHandler() {
    }

	public static final DeferredRegister<MobEffect> REG = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MowziesMobs.MODID);
	
    public static final RegistryObject<EffectSunsBlessing> SUNS_BLESSING = REG.register("suns_blessing", () -> new EffectSunsBlessing());
    public static final RegistryObject<EffectGeomancy> GEOMANCY = REG.register("geomancy", () -> new EffectGeomancy());
    public static final RegistryObject<EffectFrozen> FROZEN = REG.register("frozen", () -> new EffectFrozen());
    public static final RegistryObject<EffectPoisonResist> POISON_RESIST = REG.register("poison_resist", () -> new EffectPoisonResist());
    public static final RegistryObject<EffectSunblock> SUNBLOCK = REG.register("sunblock", () -> new EffectSunblock());

    public static void addOrCombineEffect(LivingEntity entity, MobEffect effect, int duration, int amplifier, boolean ambient, boolean showParticles) {
        if (effect == null) return;
        MobEffectInstance effectInst = entity.getEffect(effect);
        MobEffectInstance newEffect = new MobEffectInstance(effect, duration, amplifier, ambient, showParticles);
        if (effectInst != null) effectInst.update(newEffect);
        else entity.addEffect(newEffect);
    }
}
