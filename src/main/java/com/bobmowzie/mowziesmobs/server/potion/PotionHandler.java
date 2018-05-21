package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(MowziesMobs.MODID)
@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class PotionHandler {
    private PotionHandler() {
    }

    public static final MowziePotionSunsBlessing SUNS_BLESSING = null;
    public static final MowziePotionGeomancy GEOMANCY = null;
    public static final MowziePotionFrozen FROZEN = null;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(
                new MowziePotionSunsBlessing(),
                new MowziePotionGeomancy(),
                new MowziePotionFrozen()
        );
    }
}
