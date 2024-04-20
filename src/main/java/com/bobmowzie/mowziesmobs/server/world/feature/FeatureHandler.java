package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FeatureHandler {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_REG = DeferredRegister.create(Registries.STRUCTURE_TYPE, MowziesMobs.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPE_REG = DeferredRegister.create(Registries.STRUCTURE_PIECE, MowziesMobs.MODID);

    public static RegistryObject<StructureType<WroughtnautChamberStructure>> WROUGHTNAUT_CHAMBER = registerStructureType("wrought_chamber", () -> () -> WroughtnautChamberStructure.CODEC);
    public static RegistryObject<StructurePieceType> WROUGHTNAUT_CHAMBER_PIECE = registerStructurePieceType("wrought_chamber_template", WroughtnautChamberPieces.Piece::new);

    public static RegistryObject<StructureType<UmvuthanaGroveStructure>> UMVUTHANA_GROVE = registerStructureType("umvuthana_grove", () -> () -> UmvuthanaGroveStructure.CODEC);
    public static RegistryObject<StructurePieceType> UMVUTHANA_GROVE_PIECE = registerStructurePieceType("umvuthana_grove_template", UmvuthanaGrovePieces.Piece::new);
    public static RegistryObject<StructurePieceType> UMVUTHANA_FIREPIT = registerStructurePieceType("umvuthana_firepit", UmvuthanaGrovePieces.FirepitPiece::new);

    public static RegistryObject<StructureType<FrostmawStructure>> FROSTMAW = registerStructureType("frostmaw_spawn", () -> () -> FrostmawStructure.CODEC);
    public static RegistryObject<StructurePieceType> FROSTMAW_PIECE = registerStructurePieceType("frostmaw_template", FrostmawPieces.FrostmawPiece::new);

//    public static RegistryObject<StructureFeature<JigsawConfiguration>> MONASTERY = registerStructure("monastery", () -> (new MonasteryStructure(JigsawConfiguration.CODEC)));

    private static <T extends Structure> RegistryObject<StructureType<T>> registerStructureType(String name, Supplier<StructureType<T>> structure) {
        return STRUCTURE_TYPE_REG.register(name, structure);
    }

    private static <T extends Structure> RegistryObject<StructurePieceType> registerStructurePieceType(String name, StructurePieceType structurePieceType) {
        return STRUCTURE_PIECE_TYPE_REG.register(name, () -> structurePieceType);
    }
}
