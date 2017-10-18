package com.bobmowzie.mowziesmobs.server.recipe;

import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Josh on 11/7/2016.
 */
public enum RecipeHandler {
	INSTANCE;

	public void onInit() {
		GameRegistry.addRecipe(new ItemStack(BlockHandler.INSTANCE.paintedAcaciaSlab, 6),
				"AAA",
				'A', BlockHandler.INSTANCE.paintedAcacia
		);
	}
}
