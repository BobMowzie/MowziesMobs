package com.bobmowzie.mowziesmobs.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.item.Item;

import com.bobmowzie.mowziesmobs.MMTabs;

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
