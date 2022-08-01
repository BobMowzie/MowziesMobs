package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by BobMowzie on 1/10/2019.
 */

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PotionTypeHandler {
    private PotionTypeHandler() {}

    public static final Potion POISON_RESIST = new Potion("poison_resist", new MobEffectInstance(EffectHandler.POISON_RESIST, 3600)).setRegistryName("poison_resist");
    public static final Potion LONG_POISON_RESIST = new Potion("poison_resist", new MobEffectInstance(EffectHandler.POISON_RESIST, 9600)).setRegistryName("long_poison_resist");

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
