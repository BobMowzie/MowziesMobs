package com.bobmowzie.mowziesmobs.block;

import com.bobmowzie.mowziesmobs.MMTabs;
import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.tile.TileBabyFoliaath;
import net.minecraft.block.BlockBush;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by jnad325 on 3/2/15.
 */
public class BlockBabyFoliaath extends BlockBush implements ITileEntityProvider {
    public BlockBabyFoliaath()
    {
        super(Material.plants);
        setBlockName("Baby Foliaath");
        setBlockTextureName(MowziesMobs.getModID() + "test");
        setHardness(0F);
        setResistance(0F);
        setCreativeTab(MMTabs.generic);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileBabyFoliaath();
    }
}
