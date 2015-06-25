package com.bobmowzie.mowziesmobs.common.item;

import net.ilexiconn.llibrary.common.structure.util.Structure;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

import java.util.Random;
import com.bobmowzie.mowziesmobs.common.gen.MMStructureGenerator;

/**
 * Created by jnad325 on 6/24/15.
 */
public class TestItem extends ItemSword {

    public TestItem() {
        super(Item.ToolMaterial.IRON);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack1, ItemStack itemStack2) {
        return Items.iron_ingot == itemStack2.getItem() || super.getIsRepairable(itemStack1, itemStack2);
    }

    // Remove onItemUse method completely after testing is over!!!!
    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Random rand = new Random();
        if( !world.isRemote && player.isSneaking() ) {
            Structure structure = MMStructureGenerator.gen.structures.get(1);

// Add player facing to rotation calculations so your structure faces you when generated
            YourMod.gen.setPlayerFacing(player);

// Sets the structure to the structure at index '1' in the List; i.e. the Tutorial Home
            YourMod.gen.setStructure(structure);

// Optionally, set your structure's offset coordinates
            YourMod.gen.setDefaultOffset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());

// Now generate the structure at the x/y/z provided by the block's method parameters
            YourMod.gen.generate(world, world.rand, x, y, z);
            return true
        }
        return false;
    }
}