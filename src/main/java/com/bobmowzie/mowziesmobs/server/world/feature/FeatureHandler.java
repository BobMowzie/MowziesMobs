package com.bobmowzie.mowziesmobs.server.world.feature;

import java.util.function.Supplier;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.FrostmawPieces;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.FrostmawStructure;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.UmvuthanaGrovePieces;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.UmvuthanaGroveStructure;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.WroughtnautChamberPieces;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.WroughtnautChamberStructure;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FeatureHandler {
    public static final DeferredRegister<Feature<?>> REG = DeferredRegister.create(ForgeRegistries.FEATURES, MowziesMobs.MODID);

    public static RegistryObject<Feature<NoneFeatureConfiguration>> WROUGHTNAUT_CHAMBER = registerStructure("wrought_chamber", () -> (new WroughtnautChamberStructure(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType WROUGHTNAUT_CHAMBER_PIECE;

    public static RegistryObject<Feature<NoneFeatureConfiguration>> UMVUTHANA_GROVE = registerStructure("umvuthana_grove", () -> (new UmvuthanaGroveStructure(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType UMVUTHANA_GROVE_PIECE;
    public static StructurePieceType UMVUTHANA_FIREPIT;

    public static RegistryObject<Feature<NoneFeatureConfiguration>> FROSTMAW = registerStructure("frostmaw_spawn", () -> (new FrostmawStructure(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType FROSTMAW_PIECE;

//    public static RegistryObject<StructureFeature<JigsawConfiguration>> MONASTERY = registerStructure("monastery", () -> (new MonasteryStructure(JigsawConfiguration.CODEC)));

    private static <T extends Feature<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return REG.register(name, structure);
    }

    public static void registerStructurePieces() {
        WROUGHTNAUT_CHAMBER_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "wrought_chamber_template"), WroughtnautChamberPieces.Piece::new);
        UMVUTHANA_GROVE_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "umvuthana_grove_template"), UmvuthanaGrovePieces.Piece::new);
        UMVUTHANA_FIREPIT = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "umvuthana_firepit"), UmvuthanaGrovePieces.FirepitPiece::new);
        FROSTMAW_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "frostmaw_template"), FrostmawPieces.FrostmawPiece::new);
    }
}
