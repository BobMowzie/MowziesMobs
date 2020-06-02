package com.bobmowzie.mowziesmobs.server.block;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class BlockGrottol extends BlockHorizontal {
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(
        0.0625F, 0.0F, 0.0625F,
        0.9375F, 0.9375F, 0.9375F
    );

    public BlockGrottol() {
        super(Material.ROCK, MapColor.DIAMOND);
        setDefaultState(blockState.getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(VARIANT, Variant.DIAMOND)
        );
        setLightOpacity(0);
        useNeighborBrightness = true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rng, int fortune) {
        return Items.AIR;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && hasSupport(worldIn, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (hasSupport(world, pos)) {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((state.getValue(VARIANT).getIndex() << 2) & 0b1100) |
            (state.getValue(FACING).getHorizontalIndex() & 0b0011);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
            .withProperty(VARIANT, Variant.valueOf((meta & 0b1100) >> 2))
            .withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 0b0011));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, VARIANT);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    private static boolean hasSupport(World world, BlockPos pos) {
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
        public final String getName() {
            return name;
        }

        public static Variant valueOf(int index) {
            return LOOKUP.get(index);
        }
    }
}
