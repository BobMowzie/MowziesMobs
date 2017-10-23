package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.server.creativetab.CreativeTabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockPaintedAcacia extends Block {
	public BlockPaintedAcacia() {
		super(Material.WOOD);
		setCreativeTab(CreativeTabHandler.INSTANCE.creativeTab);
		setHardness(2.0F);
		setResistance(5.0F);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("paintedAcacia");
		setRegistryName("painted_acacia");
	}
}
