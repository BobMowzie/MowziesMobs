package com.bobmowzie.mowziesmobs.server.world.feature;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FeatureHandler {
    public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, MowziesMobs.MODID);

    public static RegistryObject<Structure<NoFeatureConfig>> WROUGHTNAUT_CHAMBER = registerStructure("wrought_chamber", () -> (new WroughtnautChamberStructure(NoFeatureConfig.CODEC)));
    public static IStructurePieceType WROUGHTNAUT_CHAMBER_PIECE = WroughtnautChamberPieces.Piece::new;

    public static RegistryObject<Structure<NoFeatureConfig>> BARAKOA_VILLAGE = registerStructure("barakoa_village", () -> (new WroughtnautChamberStructure(NoFeatureConfig.CODEC)));
    public static IStructurePieceType BARAKOA_VILLAGE_PIECE = BarakoaVillagePieces.Piece::new;
    public static IStructurePieceType BARAKOA_VILLAGE_FIREPIT = BarakoaVillagePieces.FirepitPiece::new;
    public static IStructurePieceType BARAKOA_VILLAGE_STAKE = BarakoaVillagePieces.StakePiece::new;
    public static IStructurePieceType BARAKOA_VILLAGE_ALTAR = BarakoaVillagePieces.AltarPiece::new;

    public static RegistryObject<Structure<NoFeatureConfig>> FROSTMAW = registerStructure("frostmaw_spawn", () -> (new WroughtnautChamberStructure(NoFeatureConfig.CODEC)));
    public static IStructurePieceType FROSTMAW_PIECE = FrostmawPieces.Piece::new;

    private static <T extends Structure<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return DEFERRED_REGISTRY_STRUCTURE.register(name, structure);
    }

    /*@SubscribeEvent
    public static void register(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().registerAll(
                WROUGHTNAUT_CHAMBER,
                BARAKOA_VILLAGE,
                FROSTMAW
        );
        registerPiece(WROUGHTNAUT_CHAMBER_PIECE, "WROUGHTNAUT_CHAMBER_PIECE");
        registerPiece(BARAKOA_VILLAGE_PIECE, "BARAKOA_VILLAGE_PIECE");
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
    }*/ // TODO
}
