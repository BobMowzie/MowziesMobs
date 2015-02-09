package com.bobmowzie.mowziesmobs.block;

import com.bobmowzie.mowziesmobs.MMTabs;
import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockTest extends Block
{
    public BlockTest()
    {
        super(Material.ground);
        setBlockName("test");
        setBlockTextureName(MowziesMobs.getModID() + "test");
        setHardness(3.0F);
        setResistance(5.0F);
        setStepSound(Block.soundTypeStone);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(MMTabs.generic);
    }
}
