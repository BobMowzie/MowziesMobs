package com.bobmowzie.mowziesmobs;

import net.ilexiconn.llib.content.IContentProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
				return Items.cooked_beef;
			}
		};
	}
}
