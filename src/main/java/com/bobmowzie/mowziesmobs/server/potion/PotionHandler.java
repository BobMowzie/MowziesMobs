package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class PotionHandler {
    private PotionHandler() {
    }

    public static final MowziePotionSunsBlessing SUNS_BLESSING = (MowziePotionSunsBlessing) new MowziePotionSunsBlessing().setRegistryName(MowziesMobs.MODID, "suns_blessing");
    public static final MowziePotionGeomancy GEOMANCY = (MowziePotionGeomancy) new MowziePotionGeomancy().setRegistryName(MowziesMobs.MODID, "geomancy");
    public static final MowziePotionFrozen FROZEN = (MowziePotionFrozen) new MowziePotionFrozen().setRegistryName(MowziesMobs.MODID, "frozen");
    public static final MowziePotionPoisonResist POISON_RESIST = (MowziePotionPoisonResist) new MowziePotionPoisonResist().setRegistryName(MowziesMobs.MODID, "poison_resist");

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
