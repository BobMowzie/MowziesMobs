package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ConfiguredFeatureHandler {
    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_WROUGHT_CHAMBER = register(createFeatureKey("wrought_chamber"), FeatureHandler.WROUGHTNAUT_CHAMBER.get().configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_STRONGHOLD));
    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_BARAKOA_VILLAGE = register(createFeatureKey("barakoa_village"), FeatureHandler.BARAKOA_VILLAGE.get().configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_JUNGLE_TEMPLE));
    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_FROSTMAW = register(createFeatureKey("frostmaw_spawn"), FeatureHandler.FROSTMAW.get().configured(NoneFeatureConfiguration.INSTANCE, BiomeTags.HAS_IGLOO));

    public static final Holder<StructureSet> WROUGHT_CHAMBERS = register(createSetKey("wrought_chambers"), CONFIGURED_WROUGHT_CHAMBER, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23217347));
    public static final Holder<StructureSet> BARAKOA_VILLAGES = register(createSetKey("barakoa_villages"), CONFIGURED_BARAKOA_VILLAGE, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23311138));
    public static final Holder<StructureSet> FROSTMAWS = register(createSetKey("frostmaw_spawns"), CONFIGURED_FROSTMAW, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23317578));

    private static ResourceKey<ConfiguredStructureFeature<?, ?>> createFeatureKey(String name) {
        return ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(MowziesMobs.MODID, name));
    }

    private static ResourceKey<StructureSet> createSetKey(String name) {
        return ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, new ResourceLocation(MowziesMobs.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends StructureFeature<FC>> Holder<ConfiguredStructureFeature<?, ?>> register(ResourceKey<ConfiguredStructureFeature<?, ?>> key, ConfiguredStructureFeature<FC, F> feature) {
        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, key, feature);
    }

    static Holder<StructureSet> register(ResourceKey<StructureSet> key, StructureSet set) {
        return BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, key, set);
    }

    static Holder<StructureSet> register(ResourceKey<StructureSet> key, Holder<ConfiguredStructureFeature<?, ?>> configuredFeature, StructurePlacement placement) {
        return register(key, new StructureSet(configuredFeature, placement));
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
