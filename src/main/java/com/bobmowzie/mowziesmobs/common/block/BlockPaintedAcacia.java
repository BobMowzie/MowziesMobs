package com.bobmowzie.mowziesmobs.common.block;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.creativetab.CreativeTabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class BlockPaintedAcacia extends Block {
    private IIcon[] icons;

    public BlockPaintedAcacia() {
        super(Material.wood);
        setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
        setBlockTextureName(new ResourceLocation(MowziesMobs.MODID, "texturePaintedAcacia").toString());
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeWood);
        setBlockName("paintedAcacia");
    }
}
