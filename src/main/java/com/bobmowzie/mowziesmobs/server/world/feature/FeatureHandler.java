package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.resources.RegistryKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.registry.DynamicRegistries;
import net.minecraft.resources.registry.MutableRegistry;
import net.minecraft.resources.registry.Registry;
import net.minecraft.resources.registry.WorldGenRegistries;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

// Adapted from TelepathicGrunt's structure tutorial repo: https://github.com/TelepathicGrunt/StructureTutorialMod
public class FeatureHandler {
    public static final DeferredRegister<Structure<?>> REG = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MowziesMobs.MODID);

    public static RegistryObject<Structure<NoFeatureConfig>> WROUGHTNAUT_CHAMBER = registerStructure("wrought_chamber", () -> (new WroughtnautChamberStructure(NoFeatureConfig.CODEC)));
    public static IStructurePieceType WROUGHTNAUT_CHAMBER_PIECE = IStructurePieceType.register(WroughtnautChamberPieces.Piece::new, MowziesMobs.MODID + "wrought_chamber_template");

    public static RegistryObject<Structure<NoFeatureConfig>> BARAKOA_VILLAGE = registerStructure("barakoa_village", () -> (new BarakoaVillageStructure(NoFeatureConfig.CODEC)));
    public static IStructurePieceType BARAKOA_VILLAGE_PIECE = IStructurePieceType.register(BarakoaVillagePieces.Piece::new, MowziesMobs.MODID + "barakoa_village_template");
    public static IStructurePieceType BARAKOA_VILLAGE_HOUSE = IStructurePieceType.register(BarakoaVillagePieces.HousePiece::new, MowziesMobs.MODID + "barakoa_village_house");
    public static IStructurePieceType BARAKOA_VILLAGE_FIREPIT = IStructurePieceType.register(BarakoaVillagePieces.FirepitPiece::new, MowziesMobs.MODID + "barakoa_village_firepit");
    public static IStructurePieceType BARAKOA_VILLAGE_STAKE = IStructurePieceType.register(BarakoaVillagePieces.StakePiece::new, MowziesMobs.MODID + "barakoa_village_stake");
    public static IStructurePieceType BARAKOA_VILLAGE_ALTAR = IStructurePieceType.register(BarakoaVillagePieces.AltarPiece::new, MowziesMobs.MODID + "barakoa_village_altar");

    public static RegistryObject<Structure<NoFeatureConfig>> FROSTMAW = registerStructure("frostmaw_spawn", () -> (new FrostmawStructure(NoFeatureConfig.CODEC)));
    public static IStructurePieceType FROSTMAW_PIECE = IStructurePieceType.register(FrostmawPieces.Piece::new, MowziesMobs.MODID + "frostmaw_template");

    private static <T extends Structure<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return REG.register(name, structure);
    }

    public static void setupStructures() {
        setupMapSpacingAndLand(WROUGHTNAUT_CHAMBER.get(), new StructureSeparationSettings(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get(), ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationSeparation.get(),123555789), false);
        setupMapSpacingAndLand(BARAKOA_VILLAGE.get(), new StructureSeparationSettings(ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.generationDistance.get(), ConfigHandler.COMMON.MOBS.BARAKO.generationConfig.generationSeparation.get(),123444789), false);
        setupMapSpacingAndLand(FROSTMAW.get(), new StructureSeparationSettings(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get(), ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationSeparation.get(),1237654789), false);
    }

    public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformSurroundingLand) {
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            Structure.field_236384_t_ =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.field_236384_t_)
                            .add(structure)
                            .build();
        }

        DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.field_236191_b_)
                        .put(structure, structureSeparationSettings)
                        .build();

        WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().getStructures().func_236195_a_();
            if (structureMap instanceof ImmutableMap) {
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().getStructures().field_236193_d_ = tempMap;
            }
            else {
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }

    private static Method GETCODEC_METHOD;
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)event.getWorld();

            // Skip Terraforged worlds
            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                MowziesMobs.LOGGER.error("Was unable to check if " + serverWorld.getDimensionKey().getLocation() + " is using Terraforged's ChunkGenerator.");
            }

            if(serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator &&
                    serverWorld.getDimensionKey().equals(World.OVERWORLD)){
                return;
            }

            // Put structure spacing
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            addStructureSpacing(WROUGHTNAUT_CHAMBER.get(), tempMap, serverWorld, ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig);
            addStructureSpacing(BARAKOA_VILLAGE.get(), tempMap, serverWorld, ConfigHandler.COMMON.MOBS.BARAKO.generationConfig);
            addStructureSpacing(FROSTMAW.get(), tempMap, serverWorld, ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig);
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }

    private static void addStructureSpacing(Structure<NoFeatureConfig> structure, Map<Structure<?>, StructureSeparationSettings> tempMap, ServerWorld world, ConfigHandler.GenerationConfig generationConfig) {
        List<? extends String> dimensionNames = generationConfig.dimensions.get();
        ResourceLocation currDimensionName = world.getDimensionKey().getLocation();
        if (dimensionNames.contains(currDimensionName.toString())) {
            tempMap.putIfAbsent(structure, DimensionStructuresSettings.field_236191_b_.get(structure));
        }
        else {
            tempMap.remove(structure);
        }
    }
}
