package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class PotionHandler {
    private PotionHandler() {
    }

    public static final Potion SUNS_BLESSING = new MowziePotionSunsBlessing().setRegistryName(MowziesMobs.MODID, "suns_blessing");
    public static final Potion GEOMANCY = new MowziePotionGeomancy().setRegistryName(MowziesMobs.MODID, "geomancy");
    public static final Potion FROZEN = new MowziePotionFrozen().setRegistryName(MowziesMobs.MODID, "frozen");
    public static final Potion POISON_RESIST = new MowziePotionPoisonResist().setRegistryName(MowziesMobs.MODID, "poison_resist");

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(
                SUNS_BLESSING,
                GEOMANCY,
                FROZEN,
                POISON_RESIST
        );
    }
}
