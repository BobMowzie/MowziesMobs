package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class ConfiguredFeatureHandler {

    public static Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_WROUGHT_CHAMBER;
    public static Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_UMVUTHANA_GROVE;
    public static Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_FROSTMAW;
    public static Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_MONASTERY;

    public static Holder<StructureSet> WROUGHT_CHAMBERS;
    public static Holder<StructureSet> UMVUTHANA_GROVES;
    public static Holder<StructureSet> FROSTMAWS;
    public static Holder<StructureSet> MONASTERIES;

    public static final Set<ResourceLocation> FERROUS_WROUGHTNAUT_BIOMES = new HashSet<>();
    public static final Set<ResourceLocation> UMVUTHI_BIOMES = new HashSet<>();
    public static final Set<ResourceLocation> FROSTMAW_BIOMES = new HashSet<>();
    public static final Set<ResourceLocation> SCULPTOR_BIOMES = new HashSet<>();

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

    public static void registerConfiguredFeatures() {
        CONFIGURED_WROUGHT_CHAMBER = register(createFeatureKey("wrought_chamber"), FeatureHandler.WROUGHTNAUT_CHAMBER.get().configured(NoneFeatureConfiguration.INSTANCE, TagHandler.HAS_MOWZIE_STRUCTURE));
        CONFIGURED_UMVUTHANA_GROVE = register(createFeatureKey("umvuthana_grove"), FeatureHandler.UMVUTHANA_GROVE.get().configured(NoneFeatureConfiguration.INSTANCE, TagHandler.HAS_MOWZIE_STRUCTURE));
        CONFIGURED_FROSTMAW = register(createFeatureKey("frostmaw_spawn"), FeatureHandler.FROSTMAW.get().configured(NoneFeatureConfiguration.INSTANCE, TagHandler.HAS_MOWZIE_STRUCTURE, true));
//        CONFIGURED_MONASTERY = register(createFeatureKey("monastery"), FeatureHandler.MONASTERY.get().configured(new JigsawConfiguration(PlainVillagePools.START, 0), TagHandler.HAS_MOWZIE_STRUCTURE));

        WROUGHT_CHAMBERS = register(createSetKey("wrought_chambers"), CONFIGURED_WROUGHT_CHAMBER, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23217347));
        UMVUTHANA_GROVES = register(createSetKey("umvuthana_groves"), CONFIGURED_UMVUTHANA_GROVE, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23311138));
        FROSTMAWS = register(createSetKey("frostmaw_spawns"), CONFIGURED_FROSTMAW, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23317578));
//        MONASTERIES = register(createSetKey("monasteries"), CONFIGURED_MONASTERY, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 25327374));
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        ResourceLocation biomeName = event.getName();
        if (biomeName == null) return;
        ResourceKey<Biome> biomeKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, biomeName);

        if (ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added Ferrous Wroughtnaut biome: " + biomeName.toString());
            FERROUS_WROUGHTNAUT_BIOMES.add(biomeName);
        }
        if (ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added Barako biome: " + biomeName.toString());
            UMVUTHI_BIOMES.add(biomeName);
        }
        if (ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added frostmaw biome: " + biomeName.toString());
            FROSTMAW_BIOMES.add(biomeName);
        }
        /*if (ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added frostmaw biome: " + biomeName.toString());
            SCULPTOR_BIOMES.add(biomeName);
        }*/
    }
}
