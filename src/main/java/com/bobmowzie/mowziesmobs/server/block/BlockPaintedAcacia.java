package com.bobmowzie.mowziesmobs.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;

public class BlockPaintedAcacia extends Block {
    private IIcon[] icons;

    public BlockPaintedAcacia() {
        super(Material.WOOD);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
//        setBlockTextureName(new ResourceLocation(MowziesMobs.MODID, "texturePaintedAcacia").toString());
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.WOOD);
        setRegistryName("paintedAcacia");
    }
}
