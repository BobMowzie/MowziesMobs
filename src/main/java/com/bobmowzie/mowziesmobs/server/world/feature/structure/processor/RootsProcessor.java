package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
        if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos))) {
            return blockInfoGlobal;
        }
        Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);
        if (random.nextFloat() < 0.15) {
            if (
                    blockInfoGlobal.state.is(Blocks.DARK_OAK_PLANKS) ||
                            blockInfoGlobal.state.is(Blocks.DARK_OAK_SLAB) && blockInfoGlobal.state.getValue(SlabBlock.TYPE) != SlabType.TOP
            ) {


                BlockPos pos = blockInfoGlobal.pos.below();
                BlockState belowState = levelReader.getBlockState(pos);
                if (belowState.isAir()) {
                    levelReader.getChunk(pos).setBlockState(pos, Blocks.HANGING_ROOTS.defaultBlockState(), false);
                }
            } else if (
                    blockInfoGlobal.state.is(Blocks.DARK_OAK_TRAPDOOR) &&
                            blockInfoGlobal.state.getValue(TrapDoorBlock.HALF) == Half.TOP &&
                            !blockInfoGlobal.state.getValue(TrapDoorBlock.OPEN)
            ) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.HANGING_ROOTS.defaultBlockState(), blockInfoGlobal.nbt);
            }
        }
        return blockInfoGlobal;
    }

}
