package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.item.ItemTest;
import net.ilexiconn.llib.content.IContentProvider;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class MMItems implements IContentProvider
{
	public static Item itemTest;

	public void init()
	{
		registerItems();
	}

	public void registerItems()
	{
		itemTest = new ItemTest();
		GameRegistry.registerItem(itemTest, "itemTest");
	}
}
