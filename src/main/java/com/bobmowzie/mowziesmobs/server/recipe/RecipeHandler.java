package com.bobmowzie.mowziesmobs.server.recipe;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraftforge.fml.common.Mod;

//TODO Delete?
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
