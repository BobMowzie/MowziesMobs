package com.bobmowzie.mowziesmobs.server.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;

public class BlockPaintedAcaciaSlab extends BlockSlab {
    public BlockPaintedAcaciaSlab() {
        super(Material.WOOD);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.WOOD);
        setRegistryName("paintedAcaciaSlab");
//        setBlockTextureName(new ResourceLocation(MowziesMobs.MODID, "texturePaintedAcacia").toString());
    }

    @Override
    public String func_150002_b(int p_150002_1_) {
        return null;
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return null;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return null;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return null;
    }
}
