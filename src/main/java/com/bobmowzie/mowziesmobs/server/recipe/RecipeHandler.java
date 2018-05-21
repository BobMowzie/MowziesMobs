package com.bobmowzie.mowziesmobs.server.recipe;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class RecipeHandler {
    private RecipeHandler() {}

    @SubscribeEvent
    public static void register(RegistryEvent.Register<IRecipe> event) {
        GameRegistry.addShapedRecipe(new ResourceLocation("Painted Acacia Slab"), new ResourceLocation("recipes"), new ItemStack(BlockHandler.PAINTED_ACACIA_SLAB, 6),
                "AAA",'A', BlockHandler.PAINTED_ACACIA
        );
    }
}
