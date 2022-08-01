package com.bobmowzie.mowziesmobs.server.block;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class BlockGrottol extends HorizontalDirectionalBlock {
    public static final EnumProperty<Variant> VARIANT = EnumProperty.create("variant",Variant.class);

    private static final VoxelShape BOUNDS = Shapes.box(
            0.0625F, 0.0F, 0.0625F,
            0.9375F, 0.9375F, 0.9375F
    );

    public BlockGrottol(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
            .setValue(FACING, Direction.NORTH)
            .setValue(VARIANT, Variant.DIAMOND)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(VARIANT);
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BOUNDS;
    }

    @Override
    @Deprecated
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return super.canSurvive(state, world, pos) && hasSupport(world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (hasSupport(world, pos)) {
            world.removeBlock(pos, false);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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

    private static boolean hasSupport(BlockGetter world, BlockPos pos) {
        return world.getBlockState(pos.below()).getMaterial().isSolid();
    }

    public enum Variant implements StringRepresentable {
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
        public final String getSerializedName() {
            return name;
        }

        public static Variant valueOf(int index) {
            return LOOKUP.get(index);
        }
    }
}
