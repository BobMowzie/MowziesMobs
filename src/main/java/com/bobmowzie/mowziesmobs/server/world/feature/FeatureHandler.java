package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FeatureHandler {
    public static final DeferredRegister<StructureType<?>> REG = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, MowziesMobs.MODID);

    public static RegistryObject<StructureType<WroughtnautChamberStructure>> WROUGHTNAUT_CHAMBER = registerStructure("wrought_chamber", () -> () -> WroughtnautChamberStructure.CODEC);
    public static StructurePieceType WROUGHTNAUT_CHAMBER_PIECE;

    public static RegistryObject<StructureType<UmvuthanaGroveStructure>> UMVUTHANA_GROVE = registerStructure("umvuthana_grove", () -> () -> UmvuthanaGroveStructure.CODEC);
    public static StructurePieceType UMVUTHANA_GROVE_PIECE;
    public static StructurePieceType UMVUTHANA_FIREPIT;

    public static RegistryObject<StructureType<FrostmawStructure>> FROSTMAW = registerStructure("frostmaw_spawn", () -> () -> FrostmawStructure.CODEC);
    public static StructurePieceType FROSTMAW_PIECE;

//    public static RegistryObject<StructureFeature<JigsawConfiguration>> MONASTERY = registerStructure("monastery", () -> (new MonasteryStructure(JigsawConfiguration.CODEC)));

    private static <T extends Structure> RegistryObject<StructureType<T>> registerStructure(String name, Supplier<StructureType<T>> structure) {
        return REG.register(name, structure);
    }

    public static void registerStructurePieces() {
        WROUGHTNAUT_CHAMBER_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "wrought_chamber_template"), WroughtnautChamberPieces.Piece::new);
        UMVUTHANA_GROVE_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "umvuthana_grove_template"), UmvuthanaGrovePieces.Piece::new);
        UMVUTHANA_FIREPIT = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "umvuthana_firepit"), UmvuthanaGrovePieces.FirepitPiece::new);
        FROSTMAW_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "frostmaw_template"), FrostmawPieces.FrostmawPiece::new);
    }
}
