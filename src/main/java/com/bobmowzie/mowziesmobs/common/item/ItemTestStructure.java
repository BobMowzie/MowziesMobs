package com.bobmowzie.mowziesmobs.common.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.ilexiconn.llibrary.common.structure.util.Structure;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by jnad325 on 6/24/15.
 */
public class ItemTestStructure extends Item {

    public ItemTestStructure() {
        super();
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Random rand = new Random();
        if( !world.isRemote && player.isSneaking() ) {
            Structure structure = MowziesMobs.gen.structures.get(0);
            MowziesMobs.gen.setStructure(structure);
            MowziesMobs.gen.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
            MowziesMobs.gen.generate(world, world.rand, x, y, z);
            return true;
        }
        return false;
    }
}