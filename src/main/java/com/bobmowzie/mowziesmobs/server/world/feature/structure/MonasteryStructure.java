package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.datagen.StructureSetHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw.MowzieJigsawManager;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.common.util.Lazy;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

// Based on Telepathicgrunt's tutorial class: https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.18.0-Forge-Jigsaw/src/main/java/com/telepathicgrunt/structuretutorial/structures/RunDownHouseStructure.java
public class MonasteryStructure extends MowzieStructure {

    public static final Set<String> MUST_CONNECT_POOLS = Set.of(MowziesMobs.MODID + ":monastery/path_pool", MowziesMobs.MODID + ":monastery/path_connector_pool");
    public static final Set<String> REPLACE_POOLS = Set.of(MowziesMobs.MODID + ":monastery/path_pool");
    public static final String STRAIGHT_POOL = MowziesMobs.MODID + ":monastery/dead_end_connect_pool";

    public static final Codec<MonasteryStructure> CODEC = simpleCodec(MonasteryStructure::new);

    public MonasteryStructure(StructureSettings settings)
    {
        // Create the pieces layout of the structure and give it to the game
        super(settings, ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig, StructureTypeHandler.SCULPTOR_BIOMES, true, true, true);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_DECORATION;
    }
    
    public static Optional<Structure.GenerationStub> createPiecesGenerator(Predicate<Structure.GenerationContext> canGeneratePredicate, Structure.GenerationContext context) {

        if (!canGeneratePredicate.test(context)) {
            return Optional.empty();
        }

        Structure.GenerationContext newContext = new Structure.GenerationContext(
                context.registryAccess(),
                context.chunkGenerator(),
                context.biomeSource(),
                context.randomState(),
                context.structureTemplateManager(),
                context.random(),
                context.seed(),
                context.chunkPos(),
                context.heightAccessor(),
                context.validBiome()
        );

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);

        Optional<Structure.GenerationStub> structurePiecesGenerator =
                MowzieJigsawManager.addPieces(
                        newContext, // Used for JigsawPlacement to get all the proper behaviors done.
                        Holder.direct(context.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL)
                                .get(new ResourceLocation(MowziesMobs.MODID, "monastery/start_pool"))), blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
                        false,  // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                        // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                        true, // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                        // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                        140,
                        "mowziesmobs:monastery/path",
                        "mowziesmobs:monastery/interior",
                        MUST_CONNECT_POOLS, REPLACE_POOLS, STRAIGHT_POOL, 23
                );

        if(structurePiecesGenerator.isPresent()) {
            MowziesMobs.LOGGER.log(Level.DEBUG, "Monastery at " + blockpos);
        }
        return structurePiecesGenerator;
    }
    
    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
    	return createPiecesGenerator(t -> checkLocation(t), context);
    }

	@Override
	public StructureType<?> type() {
		return StructureTypeHandler.MONASTERY.get();
	}
}
