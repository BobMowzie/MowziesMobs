package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class StructureTypeHandler {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_REG = DeferredRegister.create(Registries.STRUCTURE_TYPE, MowziesMobs.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPE_REG = DeferredRegister.create(Registries.STRUCTURE_PIECE, MowziesMobs.MODID);

    public static RegistryObject<StructureType<WroughtnautChamberStructure>> WROUGHTNAUT_CHAMBER = registerStructureType("wrought_chamber", () -> () -> WroughtnautChamberStructure.CODEC);
    public static RegistryObject<StructurePieceType> WROUGHTNAUT_CHAMBER_PIECE = registerStructurePieceType("wrought_chamber_template", WroughtnautChamberPieces.Piece::new);

    public static RegistryObject<StructureType<UmvuthanaGroveStructure>> UMVUTHANA_GROVE = registerStructureType("umvuthana_grove", () -> () -> UmvuthanaGroveStructure.CODEC);
    public static RegistryObject<StructurePieceType> UMVUTHANA_GROVE_PIECE = registerStructurePieceType("umvuthana_grove_template", UmvuthanaGrovePieces.Piece::new);
    public static RegistryObject<StructurePieceType> UMVUTHANA_FIREPIT = registerStructurePieceType("umvuthana_firepit", UmvuthanaGrovePieces.FirepitPiece::new);

    public static RegistryObject<StructureType<FrostmawStructure>> FROSTMAW = registerStructureType("frostmaw_spawn", () -> () -> FrostmawStructure.CODEC);
    public static RegistryObject<StructurePieceType> FROSTMAW_PIECE = registerStructurePieceType("frostmaw_template", FrostmawPieces.FrostmawPiece::new);

    public static RegistryObject<StructureType<MonasteryStructure>> MONASTERY = registerStructureType("monastery", () -> () -> MonasteryStructure.CODEC);

    public static BiomeChecker FERROUS_WROUGHTNAUT_BIOME_CHECKER;
    public static final Set<Holder<Biome>> FERROUS_WROUGHTNAUT_BIOMES = new HashSet<>();
    public static BiomeChecker UMVUTHI_BIOME_CHECKER;
    public static final Set<Holder<Biome>> UMVUTHI_BIOMES = new HashSet<>();
    public static BiomeChecker FROSTMAW_BIOME_CHECKER;
    public static final Set<Holder<Biome>> FROSTMAW_BIOMES = new HashSet<>();
    public static BiomeChecker SCULPTOR_BIOME_CHECKER;
    public static final Set<Holder<Biome>> SCULPTOR_BIOMES = new HashSet<>();

    private static <T extends Structure> RegistryObject<StructureType<T>> registerStructureType(String name, Supplier<StructureType<T>> structure) {
        return STRUCTURE_TYPE_REG.register(name, structure);
    }

    private static <T extends Structure> RegistryObject<StructurePieceType> registerStructurePieceType(String name, StructurePieceType structurePieceType) {
        return STRUCTURE_PIECE_TYPE_REG.register(name, () -> structurePieceType);
    }

    public static void addBiomeSpawns(Holder<Biome> biomeKey) {
        if (FERROUS_WROUGHTNAUT_BIOME_CHECKER == null) FERROUS_WROUGHTNAUT_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get() >= 0 && FERROUS_WROUGHTNAUT_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
            //System.out.println("Added Ferrous Wroughtnaut biome: " + biomeName.toString());
            FERROUS_WROUGHTNAUT_BIOMES.add(biomeKey);
        }

        if (UMVUTHI_BIOME_CHECKER == null) UMVUTHI_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.generationDistance.get() >= 0 && UMVUTHI_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
            //System.out.println("Added Barako biome: " + biomeName.toString());
            UMVUTHI_BIOMES.add(biomeKey);
        }

        if (FROSTMAW_BIOME_CHECKER == null) FROSTMAW_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get() >= 0 && FROSTMAW_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//            System.out.println("Added frostmaw biome: " + biomeKey.toString());
            FROSTMAW_BIOMES.add(biomeKey);
        }

        /*if (SCULPTOR_BIOME_CHECKER == null) SCULPTOR_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.generationDistance.get() >= 0 && BiomeChecker.isBiomeInConfig(ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.biomeConfig, biomeKey)) {
            //System.out.println("Added frostmaw biome: " + biomeName.toString());
            SCULPTOR_BIOMES.add(biomeKey.get());
        }*/
    }
}
