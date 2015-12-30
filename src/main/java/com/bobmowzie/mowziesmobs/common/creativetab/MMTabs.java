package com.bobmowzie.mowziesmobs.common.creativetab;

import com.bobmowzie.mowziesmobs.common.item.MMItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MMTabs implements IContentHandler
{
    public static CreativeTabs generic;

    public void init()
    {
        generic = new CreativeTabs("mowziesmobs.generic")
        {
            @SideOnly(Side.CLIENT)
            public Item getTabIconItem()
            {
                return MMItems.itemBarakoaMasks[0];
            }
        };
    }

    public void gameRegistry() throws Exception
    {

    }
}
