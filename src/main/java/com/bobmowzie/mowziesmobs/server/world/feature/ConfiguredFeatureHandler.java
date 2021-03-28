package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ConfiguredFeatureHandler {
    public static StructureFeature<?, ?> CONFIGURED_WROUGHT_CHAMBER = FeatureHandler.WROUGHTNAUT_CHAMBER.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
    public static StructureFeature<?, ?> CONFIGURED_BARAKOA_VILLAGE = FeatureHandler.BARAKOA_VILLAGE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
    public static StructureFeature<?, ?> CONFIGURED_FROSTMAW = FeatureHandler.FROSTMAW.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(MowziesMobs.MODID, "configured_wrought_chamber"), CONFIGURED_WROUGHT_CHAMBER);
        Registry.register(registry, new ResourceLocation(MowziesMobs.MODID, "configured_barakoa_village"), CONFIGURED_BARAKOA_VILLAGE);
        Registry.register(registry, new ResourceLocation(MowziesMobs.MODID, "configured_frostmaw_spawn"), CONFIGURED_FROSTMAW);

        FlatGenerationSettings.STRUCTURES.put(FeatureHandler.WROUGHTNAUT_CHAMBER.get(), CONFIGURED_WROUGHT_CHAMBER);
        FlatGenerationSettings.STRUCTURES.put(FeatureHandler.BARAKOA_VILLAGE.get(), CONFIGURED_BARAKOA_VILLAGE);
        FlatGenerationSettings.STRUCTURES.put(FeatureHandler.FROSTMAW.get(), CONFIGURED_FROSTMAW);
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
