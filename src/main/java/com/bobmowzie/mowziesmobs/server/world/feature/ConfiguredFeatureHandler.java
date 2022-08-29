package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

public class ConfiguredFeatureHandler {

    public static Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_WROUGHT_CHAMBER;
    public static Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_BARAKOA_VILLAGE;
    public static Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_FROSTMAW;

    public static Holder<StructureSet> WROUGHT_CHAMBERS;
    public static Holder<StructureSet> BARAKOA_VILLAGES;
    public static Holder<StructureSet> FROSTMAWS;

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
        CONFIGURED_WROUGHT_CHAMBER = register(createFeatureKey("wrought_chamber"), FeatureHandler.WROUGHTNAUT_CHAMBER.get().configured(NoneFeatureConfiguration.INSTANCE, TagHandler.HAS_WROUGHT_CHAMBER));
        CONFIGURED_BARAKOA_VILLAGE = register(createFeatureKey("barakoa_village"), FeatureHandler.BARAKOA_VILLAGE.get().configured(NoneFeatureConfiguration.INSTANCE, TagHandler.HAS_BARAKOA_VILLAGE));
        CONFIGURED_FROSTMAW = register(createFeatureKey("frostmaw_spawn"), FeatureHandler.FROSTMAW.get().configured(NoneFeatureConfiguration.INSTANCE, TagHandler.HAS_FROSTMAW, true));

        WROUGHT_CHAMBERS = register(createSetKey("wrought_chambers"), CONFIGURED_WROUGHT_CHAMBER, new RandomSpreadStructurePlacement(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get(), ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationSeparation.get(), RandomSpreadType.LINEAR, 23217347));
        BARAKOA_VILLAGES= register(createSetKey("barakoa_villages"), CONFIGURED_BARAKOA_VILLAGE, new RandomSpreadStructurePlacement(ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.generationDistance.get(), ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.generationSeparation.get(), RandomSpreadType.LINEAR, 23311138));
        FROSTMAWS = register(createSetKey("frostmaw_spawns"), CONFIGURED_FROSTMAW, new RandomSpreadStructurePlacement(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get(), ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationSeparation.get(), RandomSpreadType.LINEAR, 23317578));
    }
}
