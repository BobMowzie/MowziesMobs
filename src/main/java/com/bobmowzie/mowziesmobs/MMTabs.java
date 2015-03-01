package com.bobmowzie.mowziesmobs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llib.content.IContentProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MMTabs implements IContentProvider
{
    public static CreativeTabs generic;

    public void init()
    {
        generic = new CreativeTabs("mowziesmobs.generic")
        {
            @SideOnly(Side.CLIENT)
            public Item getTabIconItem()
            {
                return MMItems.itemFoliaathSeed;
            }
        };
    }
}
