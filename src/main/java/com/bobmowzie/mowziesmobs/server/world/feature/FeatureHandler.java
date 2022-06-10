package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

// Adapted from TelepathicGrunt's structure tutorial repo: https://github.com/TelepathicGrunt/StructureTutorialMod
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FeatureHandler {
    public static final DeferredRegister<StructureFeature<?>> REG = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MowziesMobs.MODID);

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> WROUGHTNAUT_CHAMBER = registerStructure("wrought_chamber", () -> (new WroughtnautChamberStructure(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType WROUGHTNAUT_CHAMBER_PIECE = StructurePieceType.setPieceId(WroughtnautChamberPieces.Piece::new, MowziesMobs.MODID + "wrought_chamber_template");

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> BARAKOA_VILLAGE = registerStructure("barakoa_village", () -> (new BarakoaVillageStructure(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType BARAKOA_VILLAGE_PIECE = StructurePieceType.setPieceId(BarakoaVillagePieces.Piece::new, MowziesMobs.MODID + "barakoa_village_template");
    public static StructurePieceType BARAKOA_VILLAGE_HOUSE = StructurePieceType.setPieceId(BarakoaVillagePieces.HousePiece::new, MowziesMobs.MODID + "barakoa_village_house");
    public static StructurePieceType BARAKOA_VILLAGE_FIREPIT = StructurePieceType.setPieceId(BarakoaVillagePieces.FirepitPiece::new, MowziesMobs.MODID + "barakoa_village_firepit");
    public static StructurePieceType BARAKOA_VILLAGE_STAKE = StructurePieceType.setPieceId(BarakoaVillagePieces.StakePiece::new, MowziesMobs.MODID + "barakoa_village_stake");
    public static StructurePieceType BARAKOA_VILLAGE_ALTAR = StructurePieceType.setPieceId(BarakoaVillagePieces.AltarPiece::new, MowziesMobs.MODID + "barakoa_village_altar");

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> FROSTMAW = registerStructure("frostmaw_spawn", () -> (new FrostmawStructure(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType FROSTMAW_PIECE = StructurePieceType.setPieceId(FrostmawPieces.Piece::new, MowziesMobs.MODID + "frostmaw_template");

    private static <T extends StructureFeature<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return REG.register(name, structure);
    }

    public static void setupStructures() {
        setupMapSpacingAndLand(WROUGHTNAUT_CHAMBER.get(), new StructureFeatureConfiguration(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get(), ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationSeparation.get(),123555789), false);
        setupMapSpacingAndLand(BARAKOA_VILLAGE.get(), new StructureFeatureConfiguration(ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.generationDistance.get(), ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.generationSeparation.get(),123444789), false);
        setupMapSpacingAndLand(FROSTMAW.get(), new StructureFeatureConfiguration(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get(), ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationSeparation.get(),1237654789), true);
    }

    public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(F structure, StructureFeatureConfiguration structureSeparationSettings, boolean transformSurroundingLand) {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            StructureFeature.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<StructureFeature<?>>builder()
                            .addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        StructureSettings.DEFAULTS =
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();

        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();
            if (structureMap instanceof ImmutableMap) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else {
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }

    private static Method GETCODEC_METHOD;
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerLevel){
            ServerLevel serverWorld = (ServerLevel)event.getWorld();

            // Skip Terraforged worlds
            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch (Exception e) {
                MowziesMobs.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            if(serverWorld.getChunkSource().getGenerator() instanceof FlatLevelSource &&
                    serverWorld.dimension().equals(Level.OVERWORLD)){
                return;
            }

            // Put structure spacing
            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            addStructureSpacing(WROUGHTNAUT_CHAMBER.get(), tempMap, serverWorld, ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig);
            addStructureSpacing(BARAKOA_VILLAGE.get(), tempMap, serverWorld, ConfigHandler.COMMON.MOBS.BARAKO.generationConfig);
            addStructureSpacing(FROSTMAW.get(), tempMap, serverWorld, ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig);
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }

    private static void addStructureSpacing(StructureFeature<NoneFeatureConfiguration> structure, Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap, ServerLevel world, ConfigHandler.GenerationConfig generationConfig) {
        List<? extends String> dimensionNames = generationConfig.dimensions.get();
        ResourceLocation currDimensionName = world.dimension().location();
        if (dimensionNames.contains(currDimensionName.toString())) {
            tempMap.putIfAbsent(structure, StructureSettings.DEFAULTS.get(structure));
        }
        else {
            tempMap.remove(structure);
        }
    }
}
