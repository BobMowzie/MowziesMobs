package com.bobmowzie.mowziesmobs.block;

import com.bobmowzie.mowziesmobs.MMTabs;
import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.tile.TileBabyFoliaath;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBabyFoliaath extends BlockContainer
{
    public BlockBabyFoliaath()
    {
        super(Material.plants);
        setBlockName("Baby Foliaath");
        setBlockTextureName(MowziesMobs.getModID() + "test");
        setHardness(0F);
        setResistance(0F);
        setCreativeTab(MMTabs.generic);
    }

    public int getRenderType()
    {
        return -1;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileBabyFoliaath();
    }
}
