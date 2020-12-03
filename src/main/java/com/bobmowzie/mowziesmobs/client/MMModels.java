package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = MowziesMobs.MODID)
public final class MMModels {
    private MMModels() {}

    @SubscribeEvent
    public static void register(ModelRegistryEvent event) {
        TabulaModelHandler.INSTANCE.addDomain(MowziesMobs.MODID);

//        registerBlockModel(BlockHandler.PAINTED_ACACIA, "painted_acacia");
//        registerBlockModel(BlockHandler.PAINTED_ACACIA_SLAB, "painted_acacia_slab"); // TODO

//        ModelLoader.addSpecialModel(BlockHandler.GROTTOL, new State.Builder() // TODO
//            .withName(BlockGrottol.VARIANT)
//            .withSuffix("_grottol")
//            .build()
//        );

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(FrozenRenderHandler.INSTANCE);
    }

    private static ModelResourceLocation registerBlockModel(Block block, String name) {
        return registerItemModel(Item.getItemFromBlock(block), 0, name);
    }

    private static ModelResourceLocation registerItemModel(Item item, String name) {
        return registerItemModel(item, 0, name);
    }

    private static ModelResourceLocation registerItemModel(Item item, int id, String name) {
        ModelResourceLocation resource = new ModelResourceLocation(MowziesMobs.MODID + ':' + name, "inventory");
//        ModelLoader.setCustomModelResourceLocation(item, id, resource);
        return resource;
    }
}