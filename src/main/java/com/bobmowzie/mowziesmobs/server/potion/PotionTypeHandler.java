package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
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

    public static final PotionType POISON_RESIST = null;
    public static final PotionType LONG_POISON_RESIST = null;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PotionType> event) {
        PotionType resist = new PotionType("poisonResist", new PotionEffect[]{new PotionEffect(PotionHandler.POISON_RESIST, 3600)}).setRegistryName("poison_resist");
        PotionType longResist = new PotionType("poisonResist", new PotionEffect[]{new PotionEffect(PotionHandler.POISON_RESIST, 9600)}).setRegistryName("long_poison_resist");
        event.getRegistry().registerAll(
                resist,
                longResist
        );

        PotionHelper.addMix(PotionTypes.AWKWARD, ItemHandler.NAGA_FANG, resist);
        PotionHelper.addMix(resist, Items.REDSTONE, longResist);

    }
}
