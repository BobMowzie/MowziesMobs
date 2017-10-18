package com.bobmowzie.mowziesmobs.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum BlockHandler {
	INSTANCE;

	public Block paintedAcacia;
	public BlockSlab paintedAcaciaSlab;
	public BlockSlab paintedAcaciaDoubleSlab;
	public Block campfire;

	public void onInit() {
		paintedAcacia = new BlockPaintedAcacia();
		paintedAcaciaSlab = new BlockPaintedAcaciaSlab.Half();
		paintedAcaciaDoubleSlab = new BlockPaintedAcaciaSlab.Double();
		campfire = new BlockCampfire();

		GameRegistry.register(paintedAcacia);
		GameRegistry.register(new ItemBlock(paintedAcacia).setRegistryName(paintedAcacia.getRegistryName()));
		GameRegistry.register(paintedAcaciaSlab);
		GameRegistry.register(
				new ItemSlab(paintedAcaciaSlab, paintedAcaciaSlab, paintedAcaciaDoubleSlab)
						.setRegistryName(paintedAcaciaSlab.getRegistryName())
		);
		GameRegistry.register(paintedAcaciaDoubleSlab);
		GameRegistry.register(campfire);
	}
}
