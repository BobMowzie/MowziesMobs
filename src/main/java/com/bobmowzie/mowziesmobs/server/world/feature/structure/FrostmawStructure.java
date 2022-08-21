package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.tag.TagHandler;
import com.bobmowzie.mowziesmobs.server.world.feature.ConfiguredFeatureHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class FrostmawStructure extends MowzieStructure {
    public FrostmawStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec, ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig, FrostmawStructure::generatePieces);
        /*this.configuredStructureCodec = RecordCodecBuilder.create((p_209759_) -> {
            return p_209759_.group(codec.fieldOf("config").forGetter((p_209786_) -> {
                return (NoneFeatureConfiguration)p_209786_.config;
            }), RegistryCodecs.homogeneousList(Registry.BIOME_REGISTRY).fieldOf("biomes").forGetter(ConfiguredStructureFeature::biomes), Codec.BOOL.optionalFieldOf("adapt_noise", Boolean.valueOf(false)).forGetter((p_209784_) -> {
                return p_209784_.adaptNoise;
            }), Codec.simpleMap(MobCategory.CODEC, StructureSpawnOverride.CODEC, StringRepresentable.keys(MobCategory.values())).fieldOf("spawn_overrides").forGetter((p_209761_) -> {
                return p_209761_.spawnOverrides;
            })).apply(p_209759_, (p_209779_, p_209780_, p_209781_, p_209782_) -> {
//                HolderSet.Named<Biome> biomes = BuiltinRegistries.BIOME.getOrCreateTag(TagHandler.HAS_MOWZIE_STRUCTURE);
//                biomes.contents = ConfiguredFeatureHandler.FROSTMAW_BIOMES.stream().toList();
                HolderSet.Direct<Biome> biomes = HolderSet.direct(ConfiguredFeatureHandler.FROSTMAW_BIOMES.stream().toList());
                return new ConfiguredStructureFeature<>(this, p_209779_, biomes, p_209781_, p_209782_);
            });
        });*/
    }

    private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> pieceGenerator) {
        int x = pieceGenerator.chunkPos().getMinBlockX();
        int z = pieceGenerator.chunkPos().getMinBlockZ();
        int y = pieceGenerator.chunkGenerator().getFirstFreeHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, pieceGenerator.heightAccessor());
        BlockPos blockpos = new BlockPos(x, y, z);
        Rotation rotation = Rotation.getRandom(pieceGenerator.random());
        FrostmawPieces.addPieces(pieceGenerator.structureManager(), blockpos, rotation, builder, pieceGenerator.random());
    }
}
