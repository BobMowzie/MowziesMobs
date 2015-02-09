package com.bobmowzie.mowziesmobs.item;

import com.bobmowzie.mowziesmobs.MMTabs;
import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.item.Item;

public class ItemTest extends Item
{
    public ItemTest()
    {
        super();
        setUnlocalizedName("test");
        setTextureName(MowziesMobs.getModID() + "test");
        setCreativeTab(MMTabs.generic);
    }
}
