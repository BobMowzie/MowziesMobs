package com.bobmowzie.mowziesmobs.server.world;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.HashSet;
import java.util.Set;

public class BiomeChecker {
    private Set<BiomeCombo> comboList;
    private Set<ResourceLocation> whitelist;
    private Set<ResourceLocation> blacklist;

    public BiomeChecker(ConfigHandler.BiomeConfig biomeConfig) {
        comboList = new HashSet<>();
        for (String biomeComboString : biomeConfig.biomeTags.get()) {
            BiomeCombo biomeCombo = new BiomeCombo(biomeComboString);
            comboList.add(biomeCombo);
        }
        whitelist = new HashSet<>();
        for (String biomeString : biomeConfig.biomeWhitelist.get()) {
            whitelist.add(new ResourceLocation(biomeString));
        }
        blacklist = new HashSet<>();
        for (String biomeString : biomeConfig.biomeBlacklist.get()) {
            blacklist.add(new ResourceLocation(biomeString));
        }
    }
    
    public boolean isBiomeInConfig(Holder<Biome> biome) {
        for (ResourceLocation biomeName : whitelist) {
            TagKey<Biome> tagKey = TagKey.create(Registries.BIOME, biomeName);
            if (biome.is(tagKey)) return true;
            if (biome.is(biomeName)) return true;
        }
        for (ResourceLocation biomeName : blacklist) {
            TagKey<Biome> tagKey = TagKey.create(Registries.BIOME, biomeName);
            if (biome.is(tagKey)) return false;
            if (biome.is(biomeName)) return false;
        }

        for (BiomeCombo biomeCombo : comboList) {
            if (biomeCombo.acceptsBiome(biome)) return true;
        }
        return false;
    }

    private static class BiomeCombo {
        ResourceLocation[] neededTags;
        boolean[] inverted;
        private BiomeCombo(String biomeComboString) {
            String[] typeStrings = biomeComboString.replace(" ", "").split(",");
            neededTags = new ResourceLocation[typeStrings.length];
            inverted = new boolean[typeStrings.length];
            for (int i = 0; i < typeStrings.length; i++) {
                if (typeStrings[i].length() == 0) {
                    continue;
                }
                inverted[i] = typeStrings[i].charAt(0) == '!';
                String name = typeStrings[i].replace("!", "");
                neededTags[i] = new ResourceLocation(name);
            }
        }

        private boolean acceptsBiome(Holder<Biome> biome) {
            for (int i = 0; i < neededTags.length; i++) {
                ResourceLocation neededBiomeName = neededTags[i];
                if (neededBiomeName == null) continue;
                TagKey<Biome> neededBiomeTag = TagKey.create(Registries.BIOME, neededBiomeName);
                boolean failIfMatches = inverted[i];
                if (failIfMatches) {
                    if (biome.is(neededBiomeTag) || biome.is(neededBiomeName)) {
                        return false;
                    }
                }
                else {
                    if (!(biome.is(neededBiomeTag) || biome.is(neededBiomeName))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
