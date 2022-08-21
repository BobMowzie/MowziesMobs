package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ConfiguredFeatureHandler {

    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_WROUGHT_CHAMBER = register(createFeatureKey("wrought_chamber"), FeatureHandler.WROUGHTNAUT_CHAMBER.get().configured(NoneFeatureConfiguration.INSTANCE, TagHandler.HAS_MOWZIE_STRUCTURE));
    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_BARAKOA_VILLAGE = register(createFeatureKey("barakoa_village"), FeatureHandler.BARAKOA_VILLAGE.get().configured(NoneFeatureConfiguration.INSTANCE, TagHandler.HAS_MOWZIE_STRUCTURE));
    public static final Holder<ConfiguredStructureFeature<?, ?>> CONFIGURED_FROSTMAW = register(createFeatureKey("frostmaw_spawn"), FeatureHandler.FROSTMAW.get().configured(NoneFeatureConfiguration.INSTANCE, TagHandler.HAS_MOWZIE_STRUCTURE, true));

    public static final Holder<StructureSet> WROUGHT_CHAMBERS = register(createSetKey("wrought_chambers"), CONFIGURED_WROUGHT_CHAMBER, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23217347));
    public static final Holder<StructureSet> BARAKOA_VILLAGES = register(createSetKey("barakoa_villages"), CONFIGURED_BARAKOA_VILLAGE, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23311138));
    public static final Holder<StructureSet> FROSTMAWS = register(createSetKey("frostmaw_spawns"), CONFIGURED_FROSTMAW, new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.TRIANGULAR, 23317578));

    public static final Set<Holder<Biome>> FROSTMAW_BIOMES = new HashSet<>();

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

        ResourceLocation biomeName = event.getName();
        if (biomeName == null) return;
        Biome biome = ForgeRegistries.BIOMES.getValue(biomeName);
        if (biome == null) return;
        ResourceKey<Biome> biomeKey = ForgeRegistries.BIOMES.getResourceKey(biome).get();

        /*if (ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig, biomeName)) {
//            System.out.println("Added Ferrous Wroughtnaut biome: " + biomeName.toString());
            CONFIGURED_WROUGHT_CHAMBER.value().biomes = HolderSet.direct(
                    ImmutableList.<Holder<Biome>>builder()
                            .addAll(CONFIGURED_WROUGHT_CHAMBER.value().biomes)
                            .add(Holder.direct(biome))
                            .build()
            );
        }
        if (ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.biomeConfig, biomeName)) {
//            System.out.println("Added Barako biome: " + biomeName.toString());
            CONFIGURED_BARAKOA_VILLAGE.value().biomes = HolderSet.direct(
                    ImmutableList.<Holder<Biome>>builder()
                            .addAll(CONFIGURED_BARAKOA_VILLAGE.value().biomes)
                            .add(Holder.direct(biome))
                            .build()
            );
        }*/
        if (ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.biomeConfig, biomeKey)) {
//            System.out.println("Added frostmaw biome: " + biomeName.toString());
            FROSTMAW_BIOMES.add(BuiltinRegistries.BIOME.getHolder(biomeKey).get());
        }
    }

    public record CustomHolder(Biome value) implements Holder<Biome> {
        public boolean isBound() {
            return true;
        }

        public boolean is(ResourceLocation p_205727_) {
            return false;
        }

        public boolean is(ResourceKey<Biome> p_205725_) {
            return false;
        }

        public boolean is(TagKey<Biome> p_205719_) {
            return false;
        }

        public boolean is(Predicate<ResourceKey<Biome>> p_205723_) {
            return false;
        }

        public Either<ResourceKey<Biome>, Biome> unwrap() {
            return Either.right(this.value);
        }

        public Optional<ResourceKey<Biome>> unwrapKey() {
            return Optional.empty();
        }

        public Holder.Kind kind() {
            return Holder.Kind.DIRECT;
        }

        public String toString() {
            return "Direct{" + this.value + "}";
        }

        public boolean isValidInRegistry(Registry<Biome> p_205721_) {
            return true;
        }

        public Stream<TagKey<Biome>> tags() {
            return Stream.of();
        }

        public Biome value() {
            return this.value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Holder<?>) {
                Holder<?> otherHolder = (Holder<?>) obj;
                if (otherHolder.value() instanceof Biome) {
                    Biome biome = (Biome) otherHolder.value();
                    return this.value().getRegistryName().equals(biome.getRegistryName());
                }
            }
            return false;
        }
    }
}
