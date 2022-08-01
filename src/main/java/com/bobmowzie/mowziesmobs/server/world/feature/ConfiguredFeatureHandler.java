package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ConfiguredFeatureHandler {
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_WROUGHT_CHAMBER = FeatureHandler.WROUGHTNAUT_CHAMBER.get().configured(FeatureConfiguration.NONE);
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_BARAKOA_VILLAGE = FeatureHandler.BARAKOA_VILLAGE.get().configured(FeatureConfiguration.NONE);
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_FROSTMAW = FeatureHandler.FROSTMAW.get().configured(FeatureConfiguration.NONE);

    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(MowziesMobs.MODID, "configured_wrought_chamber"), CONFIGURED_WROUGHT_CHAMBER);
        Registry.register(registry, new ResourceLocation(MowziesMobs.MODID, "configured_barakoa_village"), CONFIGURED_BARAKOA_VILLAGE);
        Registry.register(registry, new ResourceLocation(MowziesMobs.MODID, "configured_frostmaw_spawn"), CONFIGURED_FROSTMAW);

        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(FeatureHandler.WROUGHTNAUT_CHAMBER.get(), CONFIGURED_WROUGHT_CHAMBER);
        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(FeatureHandler.BARAKOA_VILLAGE.get(), CONFIGURED_BARAKOA_VILLAGE);
        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(FeatureHandler.FROSTMAW.get(), CONFIGURED_FROSTMAW);
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        ResourceLocation biomeName = event.getName();
        if (biomeName == null) return;
        if (ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.biomeConfig, biomeName)) {
//            System.out.println("Added frostmaw biome: " + biomeName.toString());
            event.getGeneration().getStructures().add(() -> CONFIGURED_FROSTMAW);
        }
        if (ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.biomeConfig, biomeName)) {
//            System.out.println("Added Barako biome: " + biomeName.toString());
            event.getGeneration().getStructures().add(() -> CONFIGURED_BARAKOA_VILLAGE);
        }
        if (ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig, biomeName)) {
//            System.out.println("Added Ferrous Wroughtnaut biome: " + biomeName.toString());
            event.getGeneration().getStructures().add(() -> CONFIGURED_WROUGHT_CHAMBER);
        }
    }
}
