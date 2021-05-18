package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EffectHandler {
    private EffectHandler() {
    }

    public static final EffectSunsBlessing SUNS_BLESSING = (EffectSunsBlessing) new EffectSunsBlessing().setRegistryName(MowziesMobs.MODID, "suns_blessing");
    public static final EffectGeomancy GEOMANCY = (EffectGeomancy) new EffectGeomancy().setRegistryName(MowziesMobs.MODID, "geomancy");
    public static final EffectFrozen FROZEN = (EffectFrozen) new EffectFrozen().setRegistryName(MowziesMobs.MODID, "frozen");
    public static final EffectPoisonResist POISON_RESIST = (EffectPoisonResist) new EffectPoisonResist().setRegistryName(MowziesMobs.MODID, "poison_resist");

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Effect> event) {
        event.getRegistry().registerAll(
                SUNS_BLESSING,
                GEOMANCY,
                FROZEN,
                POISON_RESIST
        );
    }
}
