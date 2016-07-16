package com.bobmowzie.mowziesmobs.server.potion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public enum PotionHandler {
    INSTANCE;

    public Potion sunsBlessing;

    private void registerPotions() {
        sunsBlessing = new MowziePotionSunsBlessing();
        GameRegistry.register(sunsBlessing);
    }

    public void onInit() {
        registerPotions();
    }
}
