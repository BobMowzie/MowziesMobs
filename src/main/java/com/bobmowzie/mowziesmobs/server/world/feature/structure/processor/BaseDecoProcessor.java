package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public class BaseDecoProcessor extends StructureProcessor {
    public static final BaseDecoProcessor INSTANCE = new BaseDecoProcessor();
    public static final Codec<BaseDecoProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockState trapDoorBottom = Blocks.DARK_OAK_TRAPDOOR.defaultBlockState();
    private static final BlockState trapDoorTop = Blocks.DARK_OAK_TRAPDOOR.defaultBlockState().setValue(TrapDoorBlock.HALF, Half.TOP);
    private static final BlockState slabBottom = Blocks.DARK_OAK_SLAB.defaultBlockState();
    private static final BlockState slabTop = Blocks.DARK_OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP);
    private static final BlockState woodStairs = Blocks.DARK_OAK_STAIRS.defaultBlockState().setValue(StairBlock.HALF, Half.TOP);
    private static final BlockState wall = Blocks.DEEPSLATE_BRICK_WALL.defaultBlockState();
    private static final BlockState button = Blocks.DARK_OAK_BUTTON.defaultBlockState();
    private static final BlockState stoneStairs = Blocks.COBBLED_DEEPSLATE_STAIRS.defaultBlockState();

    private static final BlockState[][] DECO = {
            {trapDoorBottom, slabBottom, trapDoorBottom, slabBottom, trapDoorBottom, slabBottom, trapDoorBottom},
            {woodStairs, trapDoorTop, slabTop, trapDoorTop, slabTop, trapDoorTop, woodStairs},
            {wall, button, null, null, null, button, wall},
            {wall, stoneStairs, stoneStairs, stoneStairs, stoneStairs, stoneStairs, wall}
    };

    protected StructureProcessorType<?> getType() {
        return ProcessorHandler.BASE_DECO_PROCESSOR;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, StructureTemplate.StructureBlockInfo blockInfoLocal, StructureTemplate.StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData, StructureTemplate template) {
        if (blockInfoGlobal.state.is(Blocks.PURPUR_STAIRS)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos))) {
                return blockInfoGlobal;
            }

            Direction facing = blockInfoGlobal.state.getValue(StairBlock.FACING).getOpposite();
            facing = structurePlacementData.getRotation().rotate(facing);
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.RED_TERRACOTTA.defaultBlockState(), blockInfoGlobal.nbt);
            for (int x = 0; x < 7; x++) {
                for (int y = 0; y < 4; y++) {
                    BlockState state = DECO[y][x];
                    if (state == null) continue;
                    if (state.getBlock() == Blocks.COBBLED_DEEPSLATE_STAIRS) {
                        state = chooseRandomState(random);
                    }

                    BlockPos pos = blockInfoGlobal.pos.relative(facing);
                    pos = pos.relative(facing.getClockWise(), x - 3);
                    pos = pos.relative(Direction.UP, 1 - y);

                    if (levelReader.getBlockState(pos).getMaterial().isSolid()) continue;
                    if (levelReader.getBlockState(pos.below()).getBlock() == Blocks.DARK_OAK_PLANKS) continue;

                    if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
                        if (state.getBlock() instanceof StairBlock) {
                            state = state.setValue(HorizontalDirectionalBlock.FACING, facing.getOpposite());
                        }
                        else {
                            state = state.setValue(HorizontalDirectionalBlock.FACING, facing);
                        }
                    }
                    levelReader.getChunk(pos).setBlockState(pos, state, false);
                }
            }
        }
        return blockInfoGlobal;
    }

    public BlockState chooseRandomState(Random random) {
        float v = random.nextFloat();
        if (v > 0.7) return Blocks.POLISHED_DEEPSLATE_STAIRS.defaultBlockState();
        else return Blocks.COBBLED_DEEPSLATE_STAIRS.defaultBlockState();
    }

}
