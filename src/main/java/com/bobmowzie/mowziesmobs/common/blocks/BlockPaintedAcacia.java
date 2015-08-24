package com.bobmowzie.mowziesmobs.common.blocks;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.common.creativetab.MMTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by jnad325 on 8/20/15.
 */
public class BlockPaintedAcacia extends Block {
    private IIcon[] icons;

    public BlockPaintedAcacia() {
        super(Material.wood);
        setCreativeTab(MMTabs.generic);
        setBlockTextureName(new ResourceLocation(MowziesMobs.MODID, "texturePaintedAcacia").toString());
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeWood);
        setBlockName("paintedAcacia");
    }
}
