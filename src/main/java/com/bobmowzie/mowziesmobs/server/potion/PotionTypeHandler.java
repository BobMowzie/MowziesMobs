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
 * Created by BobMowzie on 1/10/2019.
 */

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PotionTypeHandler {
    private PotionTypeHandler() {}

    public static final Potion POISON_RESIST = new Potion("poison_resist", new EffectInstance(PotionHandler.POISON_RESIST, 3600)).setRegistryName("poison_resist");
    public static final Potion LONG_POISON_RESIST = new Potion("poison_resist", new EffectInstance(PotionHandler.POISON_RESIST, 9600)).setRegistryName("long_poison_resist");

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(
                POISON_RESIST,
                LONG_POISON_RESIST
        );
        PotionBrewing.addMix(Potions.AWKWARD, ItemHandler.NAGA_FANG, POISON_RESIST);
        PotionBrewing.addMix(POISON_RESIST, Items.REDSTONE, LONG_POISON_RESIST);
    }
}
