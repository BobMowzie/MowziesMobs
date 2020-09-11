package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Josh on 1/10/2019.
 */

@GameRegistry.ObjectHolder(MowziesMobs.MODID)
@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public class PotionTypeHandler {
    private PotionTypeHandler() {
    }

    public static final Potion POISON_RESIST = null;
    public static final Potion LONG_POISON_RESIST = null;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        Potion resist = new Potion("poisonResist", new EffectInstance[]{new EffectInstance(PotionHandler.POISON_RESIST, 3600)}).setRegistryName("poison_resist");
        Potion longResist = new Potion("poisonResist", new EffectInstance[]{new EffectInstance(PotionHandler.POISON_RESIST, 9600)}).setRegistryName("long_poison_resist");
        event.getRegistry().registerAll(
                resist,
                longResist
        );

        PotionHelper.addMix(Potions.AWKWARD, ItemHandler.NAGA_FANG, resist);
        PotionHelper.addMix(resist, Items.REDSTONE, longResist);

    }
}
