package com.bobmowzie.mowziesmobs.server.recipe;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.IRecipe;
import net.minecraft.world.item.crafting.IRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public final class RecipeHandler {
    private RecipeHandler() {}

//    public static final DeferredRegister<IRecipeSerializer<?>> REG = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, MowziesMobs.MODID);

//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<IRecipe> event) {
//        GameRegistry.addShapedRecipe(new ResourceLocation("Painted Acacia Slab"), new ResourceLocation("recipes"), new ItemStack(BlockHandler.PAINTED_ACACIA_SLAB, 6),
//                "AAA",'A', BlockHandler.PAINTED_ACACIA
//        );
//    }
}
