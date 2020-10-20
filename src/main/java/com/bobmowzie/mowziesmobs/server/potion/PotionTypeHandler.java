package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Josh on 1/10/2019.
 */

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public class PotionTypeHandler {
    private PotionTypeHandler() {
    }

    public static final Potion POISON_RESIST = new Potion("poisonResist", new EffectInstance[]{new EffectInstance(PotionHandler.POISON_RESIST, 3600)}).setRegistryName("poison_resist");
    public static final Potion LONG_POISON_RESIST = new Potion("poisonResist", new EffectInstance[]{new EffectInstance(PotionHandler.POISON_RESIST, 9600)}).setRegistryName("long_poison_resist");;

    private static Method brewing_mixes;
    private static void addMix(Potion start, Item ingredient, Potion result) {
        if (brewing_mixes == null) {
            brewing_mixes = ObfuscationReflectionHelper.findMethod(PotionBrewing.class, "addMix", Potion.class, Item.class, Potion.class);
            brewing_mixes.setAccessible(true);
        }
        try {
            brewing_mixes.invoke(null, start, ingredient, result);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(
                POISON_RESIST,
                LONG_POISON_RESIST
        );
        addMix(Potions.AWKWARD, ItemHandler.NAGA_FANG, POISON_RESIST);
        addMix(POISON_RESIST, Items.REDSTONE, LONG_POISON_RESIST);
    }
}
