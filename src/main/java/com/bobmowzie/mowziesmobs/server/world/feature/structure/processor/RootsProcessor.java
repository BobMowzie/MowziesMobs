package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public class RootsProcessor extends StructureProcessor {
    public static final RootsProcessor INSTANCE = new RootsProcessor();
    public static final Codec<RootsProcessor> CODEC = Codec.unit(() -> INSTANCE);

    protected StructureProcessorType<?> getType() {
        return ProcessorHandler.BASE_PROCESSOR;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData, StructureTemplate template) {
        if (
                blockInfoGlobal.state.is(Blocks.DARK_OAK_PLANKS) ||
                blockInfoGlobal.state.is(Blocks.DARK_OAK_SLAB) && blockInfoGlobal.state.getValue(SlabBlock.TYPE) != SlabType.TOP
        ) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos))) {
                return blockInfoGlobal;
            }

            BlockPos pos = blockInfoGlobal.pos.below();
            BlockState belowState = levelReader.getBlockState(pos);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
            if ((belowState.isAir() || belowState.is(Blocks.DARK_OAK_TRAPDOOR)) && random.nextFloat() < 0.5) {
                levelReader.getChunk(pos).setBlockState(pos, Blocks.HANGING_ROOTS.defaultBlockState(), false);
            }
        }
        return blockInfoGlobal;
    }

}
