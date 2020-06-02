package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockPaintedAcaciaSlab extends BlockSlab {
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    public BlockPaintedAcaciaSlab() {
        super(Material.WOOD);
        IBlockState state = blockState.getBaseState();
        if (!isDouble()) {
            state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
        }
        setDefaultState(state.withProperty(VARIANT, Variant.DEFAULT));
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.WOOD);
        setTranslationKey("paintedAcaciaSlab");
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        useNeighborBrightness = true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rng, int fortune) {
        return Item.getItemFromBlock(BlockHandler.PAINTED_ACACIA_SLAB);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(BlockHandler.PAINTED_ACACIA_SLAB);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = getDefaultState().withProperty(VARIANT, Variant.DEFAULT);
        if (!isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return !isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return isDouble() ? new BlockStateContainer(this, VARIANT): new BlockStateContainer(this, HALF, VARIANT);
    }

    @Override
    public String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return Variant.DEFAULT;
    }

    public static class Double extends BlockPaintedAcaciaSlab {
        public Double() {
            setRegistryName("painted_acacia_double_slab");
        }

        @Override
        public boolean isDouble() {
            return true;
        }
    }

    public static class Half extends BlockPaintedAcaciaSlab {
        public Half() {
            setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
            setRegistryName("painted_acacia_slab");
        }

        @Override
        public boolean isDouble() {
            return false;
        }
    }

    public static enum Variant implements IStringSerializable {
        DEFAULT;

        @Override
        public String getName() {
            return "default";
        }
    }
}
