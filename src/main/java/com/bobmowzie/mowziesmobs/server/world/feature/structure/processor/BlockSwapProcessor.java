package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Random;

public class BlockSwapProcessor extends StructureProcessor {
    public static final Codec<BlockSwapProcessor> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BlockState.CODEC.listOf().fieldOf("to_replace").forGetter(config -> config.toReplace),
                    BlockStateRandomizer.CODEC.fieldOf("replace_with").forGetter(config -> config.replaceWith))
            .apply(instance, instance.stable(BlockSwapProcessor::new)));

    List<BlockState> toReplace;
    BlockStateRandomizer replaceWith;

    public BlockSwapProcessor(List<BlockState> toReplace, BlockStateRandomizer replaceWith) {
        this.toReplace = toReplace;
        this.replaceWith = replaceWith;
    }

    protected StructureProcessorType<?> getType() {
        return ProcessorHandler.BASE_PROCESSOR;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData, StructureTemplate template) {
        for (BlockState toReplaceState : toReplace) {
            if (blockInfoGlobal.state.is(toReplaceState.getBlock())) {
                if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos))) {
                    return blockInfoGlobal;
                }
                Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, replaceWith.chooseRandomState(random), blockInfoGlobal.nbt);
                break;
            }
        }
        return blockInfoGlobal;
    }

    public BlockState chooseRandomState(Random random) {
        float v = random.nextFloat();
        if (v < 0.333) return Blocks.DIORITE.defaultBlockState();
        else if (v < 0.666) return Blocks.CALCITE.defaultBlockState();
        else return Blocks.WHITE_CONCRETE.defaultBlockState();
    }

}