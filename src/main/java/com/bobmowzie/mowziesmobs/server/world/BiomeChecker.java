package com.bobmowzie.mowziesmobs.server.world;

import java.util.HashSet;
import java.util.Set;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class BiomeChecker {
	
    private static class BiomeCombo {
        String[] neededTypes;
        boolean[] inverted;
        private BiomeCombo(String biomeComboString) {
            String[] typeStrings = biomeComboString.replace(" ", "").split(",");
            neededTypes = new String[typeStrings.length];
            inverted = new boolean[typeStrings.length];
            for (int i = 0; i < typeStrings.length; i++) {
                if (typeStrings[i].length() == 0) {
                    continue;
                }
                inverted[i] = typeStrings[i].charAt(0) == '!';
                String name = typeStrings[i].replace("!", "");
                String neededType = name;
                neededTypes[i] = neededType;
            }
        }

        private boolean acceptsBiome(String biome) {
            Set<String> thisTypes = Set.of(biome);
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
    
    public static boolean isBiomeInConfig(ConfigHandler.BiomeConfig biomeConfig, TagKey<Biome> biomeKey) {
    	String tagName = biomeKey.location().toString();
    	
        if (biomeConfig.biomeWhitelist.get().contains(tagName)) {
            return true;
        }
        if (biomeConfig.biomeBlacklist.get().contains(tagName)) {
            return false;
        }
        
        Set<BiomeCombo> biomeCombos = new HashSet<>();
        for (String biomeComboString : biomeConfig.biomeTypes.get()) {
            BiomeCombo biomeCombo = new BiomeCombo(biomeComboString);
            biomeCombos.add(biomeCombo);
        }
        for (BiomeCombo biomeCombo : biomeCombos) {
            if (biomeCombo.acceptsBiome(tagName)) return true;
        }
        return false;
    }
}
