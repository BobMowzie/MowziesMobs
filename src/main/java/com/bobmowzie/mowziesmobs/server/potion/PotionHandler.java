package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(MowziesMobs.MODID)
@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class PotionHandler {
    private PotionHandler() {
    }

    public static final MowziePotionSunsBlessing SUNS_BLESSING = new MowziePotionSunsBlessing();
    public static final MowziePotionGeomancy GEOMANCY = new MowziePotionGeomancy();
    public static final MowziePotionFrozen FROZEN = new MowziePotionFrozen();
    public static final MowziePotionPoisonResist POISON_RESIST = new MowziePotionPoisonResist();

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
