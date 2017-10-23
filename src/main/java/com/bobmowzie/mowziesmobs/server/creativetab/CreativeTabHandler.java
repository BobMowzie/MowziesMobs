package com.bobmowzie.mowziesmobs.server.creativetab;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum CreativeTabHandler {
	INSTANCE;

	public CreativeTabs creativeTab;

	public void onInit() {
		creativeTab = new CreativeTabs("mowziesmobs.creativeTab") {
			@Override
			@SideOnly(Side.CLIENT)
			public ItemStack getTabIconItem() {
				return new ItemStack(ItemHandler.INSTANCE.barakoaMasks.get(MaskType.FURY));
			}
		};
	}
}
