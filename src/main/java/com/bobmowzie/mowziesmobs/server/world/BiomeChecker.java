package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BiomeChecker {
    private static class BiomeCombo {
        BiomeDictionary.Type[] neededTypes;
        boolean[] inverted;
        private BiomeCombo(String biomeComboString) {
            String[] typeStrings = biomeComboString.toUpperCase().replace(" ", "").split(",");
            neededTypes = new BiomeDictionary.Type[typeStrings.length];
            inverted = new boolean[typeStrings.length];
            for (int i = 0; i < typeStrings.length; i++) {
                if (typeStrings[i].length() == 0) {
                    continue;
                }
                inverted[i] = typeStrings[i].charAt(0) == '!';
                String name = typeStrings[i].replace("!", "");
                Collection<BiomeDictionary.Type> allTypes = BiomeDictionary.Type.getAll();
                BiomeDictionary.Type neededType = BiomeDictionary.Type.getType(name);
                if (!allTypes.contains(neededType)) System.out.println("Mowzie's Mobs config warning: No biome dictionary type with name '" + name + "'. Unable to check for type.");
                neededTypes[i] = neededType;
            }
        }

        private boolean acceptsBiome(RegistryKey<Biome> biome) {
            Set<BiomeDictionary.Type> thisTypes = BiomeDictionary.getTypes(biome);
            for (int i = 0; i < neededTypes.length; i++) {
                if (neededTypes[i] == null) continue;
                if (!inverted[i]) {
                    if (!thisTypes.contains(neededTypes[i])) {
                        return false;
                    }
                }
                else {
                    if (thisTypes.contains(neededTypes[i])) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public static boolean isBiomeInConfig(ConfigHandler.BiomeConfig biomeConfig, ResourceLocation biomeName) {
        if (biomeConfig.biomeWhitelist.get().contains(biomeName.toString())) {
            return true;
        }
        if (biomeConfig.biomeBlacklist.get().contains(biomeName.toString())) {
            return false;
        }
        Set<BiomeCombo> biomeCombos = new HashSet<>();
        for (String biomeComboString : biomeConfig.biomeTypes.get()) {
            BiomeCombo biomeCombo = new BiomeCombo(biomeComboString);
            biomeCombos.add(biomeCombo);
        }
        RegistryKey<Biome> biomeRegistryKey = RegistryKey.getOrCreateKey(ForgeRegistries.Keys.BIOMES, biomeName);
        for (BiomeCombo biomeCombo : biomeCombos) {
            if (biomeCombo.acceptsBiome(biomeRegistryKey)) return true;
        }
        return false;
    }
}
