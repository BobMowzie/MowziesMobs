package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ConfiguredFeatureHandler {
    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_WROUGHT_CHAMBER = register(createKey("wrought_chamber"), FeatureHandler.WROUGHTNAUT_CHAMBER.get().configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_STRONGHOLD));
    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_BARAKOA_VILLAGE = register(createKey("barakoa_village"), FeatureHandler.BARAKOA_VILLAGE.get().configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_JUNGLE_TEMPLE));
    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_FROSTMAW = register(createKey("frostmaw_spawn"), FeatureHandler.FROSTMAW.get().configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_IGLOO));

    private static ResourceKey<ConfiguredStructureFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(MowziesMobs.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends StructureFeature<FC>> Holder<ConfiguredStructureFeature<?, ?>> register(ResourceKey<ConfiguredStructureFeature<?, ?>> key, ConfiguredStructureFeature<FC, F> feature) {
        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, key, feature);
    }

    // TODO Switch to set biome in structure config
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        /*ResourceLocation biomeName = event.getName();
        if (biomeName == null) return;

        if (ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig, biomeName)) {
//            System.out.println("Added Ferrous Wroughtnaut biome: " + biomeName.toString());
            event.getGeneration().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).add(PLACED_WROUGHT_CHAMBER);
        }
        if (ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.biomeConfig, biomeName)) {
//            System.out.println("Added Barako biome: " + biomeName.toString());
            event.getGeneration().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).add(PLACED_BARAKOA_VILLAGE);
        }
        if (ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.biomeConfig, biomeName)) {
//            System.out.println("Added frostmaw biome: " + biomeName.toString());
            event.getGeneration().getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES).add(PLACED_FROSTMAW);
        }*/
    }
}
