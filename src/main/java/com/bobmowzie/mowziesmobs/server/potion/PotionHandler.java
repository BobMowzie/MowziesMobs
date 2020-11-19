package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PotionHandler {
    private PotionHandler() {
    }

    public static final MowzieEffectSunsBlessing SUNS_BLESSING = (MowzieEffectSunsBlessing) new MowzieEffectSunsBlessing().setRegistryName(MowziesMobs.MODID, "suns_blessing");
    public static final MowzieEffectGeomancy GEOMANCY = (MowzieEffectGeomancy) new MowzieEffectGeomancy().setRegistryName(MowziesMobs.MODID, "geomancy");
    public static final MowzieEffectFrozen FROZEN = (MowzieEffectFrozen) new MowzieEffectFrozen().setRegistryName(MowziesMobs.MODID, "frozen");
    public static final MowzieEffectPoisonResist POISON_RESIST = (MowzieEffectPoisonResist) new MowzieEffectPoisonResist().setRegistryName(MowziesMobs.MODID, "poison_resist");

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
