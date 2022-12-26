package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FeatureHandler {
    public static final DeferredRegister<StructureFeature<?>> REG = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MowziesMobs.MODID);

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MowziesMobs.MODID);

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> WROUGHTNAUT_CHAMBER = registerStructure("wrought_chamber", () -> (new WroughtnautChamberStructure(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType WROUGHTNAUT_CHAMBER_PIECE;

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> BARAKOA_VILLAGE = registerStructure("barakoa_village", () -> (new BarakoaVillageStructure(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType BARAKOA_VILLAGE_PIECE;
    public static StructurePieceType BARAKOA_VILLAGE_HOUSE;
    public static StructurePieceType BARAKOA_VILLAGE_FIREPIT;
    public static StructurePieceType BARAKOA_VILLAGE_STAKE;
    public static StructurePieceType BARAKOA_VILLAGE_ALTAR;

    public static RegistryObject<StructureFeature<NoneFeatureConfiguration>> FROSTMAW = registerStructure("frostmaw_spawn", () -> (new FrostmawStructure(NoneFeatureConfiguration.CODEC)));
    public static StructurePieceType FROSTMAW_PIECE;

    public static RegistryObject<StructureFeature<JigsawConfiguration>> MONASTERY = registerStructure("monastery", () -> (new MonasteryStructure(JigsawConfiguration.CODEC)));

    private static <T extends StructureFeature<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return REG.register(name, structure);
    }

    public static void registerStructurePieces() {
        WROUGHTNAUT_CHAMBER_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "wrought_chamber_template"), WroughtnautChamberPieces.Piece::new);
        BARAKOA_VILLAGE_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "barakoa_village_template"), BarakoaVillagePieces.Piece::new);
        BARAKOA_VILLAGE_HOUSE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "barakoa_village_house"), BarakoaVillagePieces.HousePiece::new);
        BARAKOA_VILLAGE_FIREPIT = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "barakoa_village_firepit"), BarakoaVillagePieces.FirepitPiece::new);
        BARAKOA_VILLAGE_STAKE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "barakoa_village_stake"), BarakoaVillagePieces.StakePiece::new);
        BARAKOA_VILLAGE_ALTAR = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "barakoa_village_altar"), BarakoaVillagePieces.AltarPiece::new);
        FROSTMAW_PIECE = Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MowziesMobs.MODID, "frostmaw_template"), FrostmawPieces.FrostmawPiece::new);
    }
}
