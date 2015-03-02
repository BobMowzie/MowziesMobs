package com.bobmowzie.mowziesmobs.item;

import com.bobmowzie.mowziesmobs.MMTabs;
import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.item.Item;

public class ItemFoliaathSeed extends Item {
    public ItemFoliaathSeed() {
        super();
        setUnlocalizedName("foliaathseed");
        setTextureName(MowziesMobs.getModID() + "TextureFoliaathSeed");
        setCreativeTab(MMTabs.generic);
    }

   /* @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int hitX, float hitY, float hitZ, float metadata) {
        if (world.isRemote) {
            return true;
        } else {
            Block block = world.getBlock(x, y, z);
            x += Facing.offsetsXForSide[hitX];
            y += Facing.offsetsYForSide[hitX];
            z += Facing.offsetsZForSide[hitX];

            double yTranslation = 0.0D;

            if (hitX == 1 && block.getRenderType() == 11) {
                yTranslation = 0.5D;
            }

            EntityBabyFoliaath baby = new EntityBabyFoliaath(world);
            baby.posX = x + 0.5D;
            baby.posY = y + yTranslation;
            baby.posZ = z + 0.5D;

            if (baby != null) {
                if (itemStack.hasDisplayName()) {
                    baby.setCustomNameTag(itemStack.getDisplayName());
                }

                if (!player.capabilities.isCreativeMode) {
                    itemStack.stackSize--;
                    if (itemStack.stackSize <= 0) {
                        itemStack = (ItemStack) null;
                    }
                }
                world.spawnEntityInWorld(baby);
            }
            return true;
        }
    }*/
}
