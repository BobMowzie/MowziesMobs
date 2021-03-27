package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.spawn.SpawnHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.Set;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FeatureHandler {
    public static Structure<NoFeatureConfig> WROUGHTNAUT_CHAMBER = (Structure<NoFeatureConfig>) new WroughtnautChamberStructure(NoFeatureConfig::deserialize).setRegistryName(MowziesMobs.MODID, "wroughtnaut_chamber");
    public static IStructurePieceType WROUGHTNAUT_CHAMBER_PIECE = WroughtnautChamberPieces.Piece::new;

    public static Structure<NoFeatureConfig> BARAKOA_VILLAGE = (Structure<NoFeatureConfig>) new BarakoaVillageStructure(NoFeatureConfig::deserialize).setRegistryName(MowziesMobs.MODID, "barakoa_village");
    public static IStructurePieceType BARAKOA_VILLAGE_PIECE = BarakoaVillagePieces.Piece::new;
    public static IStructurePieceType BARAKOA_VILLAGE_HOUSE = BarakoaVillagePieces.HousePiece::new;
    public static IStructurePieceType BARAKOA_VILLAGE_FIREPIT = BarakoaVillagePieces.FirepitPiece::new;
    public static IStructurePieceType BARAKOA_VILLAGE_STAKE = BarakoaVillagePieces.StakePiece::new;
    public static IStructurePieceType BARAKOA_VILLAGE_ALTAR = BarakoaVillagePieces.AltarPiece::new;

    public static Structure<NoFeatureConfig> FROSTMAW = (Structure<NoFeatureConfig>) new FrostmawStructure(NoFeatureConfig::deserialize).setRegistryName(MowziesMobs.MODID, "frostmaw_spawn");
    public static IStructurePieceType FROSTMAW_PIECE = FrostmawPieces.Piece::new;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().registerAll(
                WROUGHTNAUT_CHAMBER,
                BARAKOA_VILLAGE,
                FROSTMAW
        );
        registerPiece(WROUGHTNAUT_CHAMBER_PIECE, "WROUGHTNAUT_CHAMBER_PIECE");
        registerPiece(BARAKOA_VILLAGE_PIECE, "BARAKOA_VILLAGE_PIECE");
        registerPiece(BARAKOA_VILLAGE_HOUSE, "BARAKOA_VILLAGE_HOUSE");
        registerPiece(BARAKOA_VILLAGE_FIREPIT, "BARAKOA_VILLAGE_FIREPIT");
        registerPiece(BARAKOA_VILLAGE_STAKE, "BARAKOA_VILLAGE_STAKE");
        registerPiece(BARAKOA_VILLAGE_ALTAR, "BARAKOA_VILLAGE_ALTAR");
        registerPiece(FROSTMAW_PIECE, "FROSTMAW_PIECE");
    }

    static IStructurePieceType registerPiece(IStructurePieceType structurePiece, String key)
    {
        return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), structurePiece);
    }

    public static void addStructureGeneration() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            ConfiguredFeature wroughtnautChamberFeature = WROUGHTNAUT_CHAMBER.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
            if (SpawnHandler.FERROUS_WROUGHTNAUT_BIOMES.contains(biome)) {
                biome.addStructure(wroughtnautChamberFeature);
            }
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, wroughtnautChamberFeature);

            ConfiguredFeature barakoaVillageFeature = BARAKOA_VILLAGE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
            if (SpawnHandler.BARAKO_BIOMES.contains(biome)) {
                biome.addStructure(barakoaVillageFeature);
            }
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, barakoaVillageFeature);

            ConfiguredFeature frostmawFeature = FROSTMAW.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
            if (SpawnHandler.FROSTMAW_BIOMES.contains(biome)) {
                biome.addStructure(frostmawFeature);
            }
            biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, frostmawFeature);
        }
    }
}
