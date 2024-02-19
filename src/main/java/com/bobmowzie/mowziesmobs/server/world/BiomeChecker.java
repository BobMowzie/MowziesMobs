package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeChecker {
    public static boolean isBiomeInConfig(ConfigHandler.BiomeConfig biomeConfig, Holder<Biome> biomeKey) {
        ResourceLocation biomeName = ForgeRegistries.BIOMES.getKey(biomeKey.get());
        if (biomeConfig.biomeWhitelist.get().contains(biomeName.toString())) {
            return true;
        }
        if (biomeConfig.biomeBlacklist.get().contains(biomeName.toString())) {
            return false;
        }
        return false;
    }
}
