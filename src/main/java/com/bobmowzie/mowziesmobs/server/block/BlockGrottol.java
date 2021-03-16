package com.bobmowzie.mowziesmobs.server.block;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class BlockGrottol extends HorizontalBlock {
    public static final EnumProperty<Variant> VARIANT = EnumProperty.create("variant",Variant.class);

    private static final VoxelShape BOUNDS = VoxelShapes.create(
            0.0625F, 0.0F, 0.0625F,
            0.9375F, 0.9375F, 0.9375F
    );

    public BlockGrottol(Properties properties) {
        super(properties);
        setDefaultState(getStateContainer().getBaseState()
            .with(HORIZONTAL_FACING, Direction.NORTH)
            .with(VARIANT, Variant.DIAMOND)
        );
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(HORIZONTAL_FACING);
        builder.add(VARIANT);
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOUNDS;
    }

    @Override
    @Deprecated
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return super.isValidPosition(state, world, pos) && hasSupport(world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (hasSupport(world, pos)) {
            world.removeBlock(pos, false);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    /*@Override
    public int getMetaFromState(BlockState state) {
        return ((state.getValue(VARIANT).getIndex() << 2) & 0b1100) |
            (state.getValue(FACING).getHorizontalIndex() & 0b0011);
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return getDefaultState()
            .withProperty(VARIANT, Variant.valueOf((meta & 0b1100) >> 2))
            .withProperty(FACING, Direction.byHorizontalIndex(meta & 0b0011));
    }*/

    private static boolean hasSupport(IBlockReader world, BlockPos pos) {
        return world.getBlockState(pos.down()).getMaterial().isSolid();
    }

    public enum Variant implements IStringSerializable {
        DIAMOND(0, "diamond"),
        BLACK_PINK(1, "black_pink");

        private static final Int2ObjectMap<Variant> LOOKUP = Stream.of(values())
            .collect(Collector.<Variant, Int2ObjectOpenHashMap<Variant>, Int2ObjectMap<Variant>>of(Int2ObjectOpenHashMap::new,
                (map, variant) -> map.put(variant.getIndex(), variant),
                (left, right) -> {
                    throw new IllegalStateException();
                },
                map -> {
                    map.defaultReturnValue(DIAMOND);
                    return Int2ObjectMaps.unmodifiable(map);
                }
            ));

        private final int index;

        private final String name;

        Variant(int index, String name) {
            this.index = index;
            this.name = name;
        }

        public final int getIndex() {
            return index;
        }

        @Override
        public final String getString() {
            return name;
        }

        public static Variant valueOf(int index) {
            return LOOKUP.get(index);
        }
    }
}
