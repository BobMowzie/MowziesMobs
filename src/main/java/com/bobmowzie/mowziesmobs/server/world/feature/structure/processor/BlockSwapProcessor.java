package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
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
                    BlockStateRandomizer.CODEC.fieldOf("replace_with").forGetter(config -> config.replaceWith),
                    Codec.BOOL.optionalFieldOf("copy_properties", true).forGetter(config -> config.copyProperties)
    ).apply(instance, instance.stable(BlockSwapProcessor::new)));

    List<BlockState> toReplace;
    BlockStateRandomizer replaceWith;
    boolean copyProperties;

    public BlockSwapProcessor(List<BlockState> toReplace, BlockStateRandomizer replaceWith, boolean copyProperties) {
        this.toReplace = toReplace;
        this.replaceWith = replaceWith;
        this.copyProperties = copyProperties;
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
                BlockState newState = replaceWith.chooseRandomState(random);
                if (copyProperties) {
                    newState = newState.getBlock().withPropertiesOf(blockInfoGlobal.state);
                }
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, newState, blockInfoGlobal.nbt);
                break;
            }
        }
        return blockInfoGlobal;
    }
}