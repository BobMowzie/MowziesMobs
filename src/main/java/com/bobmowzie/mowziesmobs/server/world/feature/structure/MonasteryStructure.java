package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.ConfiguredFeatureHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.FeatureHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw.MowzieJigsawManager;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

// Based on Telepathicgrunt's tutorial class: https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.18.0-Forge-Jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/structures/RunDownHouseStructure.java
public class MonasteryStructure extends MowzieStructure<JigsawConfiguration> {

    public static final Set<String> MUST_CONNECT_POOLS = Set.of(MowziesMobs.MODID + ":monastery/path_pool", MowziesMobs.MODID + ":monastery/path_connector_pool");
    public static final Set<String> REPLACE_POOLS = Set.of(MowziesMobs.MODID + ":monastery/path_pool");
    public static final String STRAIGHT_POOL = MowziesMobs.MODID + ":monastery/dead_end_connect_pool";

    public MonasteryStructure(Codec<JigsawConfiguration> codec) {
        // Create the pieces layout of the structure and give it to the game
        super(codec, ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig, (context) -> MonasteryStructure.createPiecesGenerator(
                (c) -> MowzieStructure.checkLocation(c, ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig, ConfiguredFeatureHandler.SCULPTOR_BIOMES, true, true, true)
                , context));
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    /**
     * The StructureSpawnListGatherEvent event allows us to have mobs that spawn naturally over time in our structure.
     * No other mobs will spawn in the structure of the same entity classification.
     * The reason you want to match the classifications is so that your structure's mob
     * will contribute to that classification's cap. Otherwise, it may cause a runaway
     * spawning of the mob that will never stop.
     *
     * We use Lazy so that if you classload this class before you register your entities, you will not crash.
     * Instead, the field and the entities inside will only be referenced when StructureSpawnListGatherEvent
     * fires much later after entity registration.
     */
    private static final Lazy<List<MobSpawnSettings.SpawnerData>> STRUCTURE_MONSTERS = Lazy.of(() -> ImmutableList.of(
            new MobSpawnSettings.SpawnerData(EntityType.ILLUSIONER, 100, 4, 9),
            new MobSpawnSettings.SpawnerData(EntityType.VINDICATOR, 100, 4, 9)
    ));
    private static final Lazy<List<MobSpawnSettings.SpawnerData>> STRUCTURE_CREATURES = Lazy.of(() -> ImmutableList.of(
            new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 30, 10, 15),
            new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 100, 1, 2)
    ));

    // Hooked up in StructureTutorialMain. You can move this elsewhere or change it up.
    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if(event.getStructure() == FeatureHandler.MONASTERY.get()) {
            event.addEntitySpawns(MobCategory.MONSTER, STRUCTURE_MONSTERS.get());
            event.addEntitySpawns(MobCategory.CREATURE, STRUCTURE_CREATURES.get());
        }
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(Predicate<PieceGeneratorSupplier.Context<JigsawConfiguration>> canGeneratePredicate, PieceGeneratorSupplier.Context<JigsawConfiguration> context) {

        if (!canGeneratePredicate.test(context)) {
            return Optional.empty();
        }

        JigsawConfiguration newConfig = new JigsawConfiguration(
                Holder.direct(context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                        .get(new ResourceLocation(MowziesMobs.MODID, "monastery/start_pool"))),
                13
        );

        PieceGeneratorSupplier.Context<JigsawConfiguration> newContext = new PieceGeneratorSupplier.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.heightAccessor(),
                context.validBiome(),
                context.structureManager(),
                context.registryAccess()
        );

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);

        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator =
                MowzieJigsawManager.addPieces(
                        newContext, // Used for JigsawPlacement to get all the proper behaviors done.
                        PoolElementStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                        blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
                        false,  // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                        // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                        true, // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                        // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                        MUST_CONNECT_POOLS, REPLACE_POOLS, STRAIGHT_POOL
                );

        if(structurePiecesGenerator.isPresent()) {
            MowziesMobs.LOGGER.log(Level.DEBUG, "Monastery at " + blockpos);
        }
        return structurePiecesGenerator;
    }
}
