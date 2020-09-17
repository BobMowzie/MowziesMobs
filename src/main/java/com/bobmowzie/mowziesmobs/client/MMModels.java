package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleTextureStitcher;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.server.block.BlockGrottol;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT, modid = MowziesMobs.MODID)
public final class MMModels {
    private MMModels() {}

    @SubscribeEvent
    public static void register(ModelRegistryEvent event) {
        TabulaModelHandler.INSTANCE.addDomain(MowziesMobs.MODID);

        registerBlockModel(BlockHandler.PAINTED_ACACIA, "painted_acacia");
        registerBlockModel(BlockHandler.PAINTED_ACACIA_SLAB, "painted_acacia_slab");

        ModelLoader.setCustomStateMapper(BlockHandler.GROTTOL, new StateMap.Builder()
            .withName(BlockGrottol.VARIANT)
            .withSuffix("_grottol")
            .build()
        );

        registerItemModel(ItemHandler.FOLIAATH_SEED, "foliaath_seed");
        registerItemModel(ItemHandler.MOB_REMOVER, "mob_remover");
        registerItemModel(ItemHandler.WROUGHT_AXE, "wrought_axe.tbl");
        registerItemModel(ItemHandler.WROUGHT_HELMET, "wrought_helmet.tbl");
        registerItemModel(ItemHandler.DART, "dart");
        registerItemModel(ItemHandler.SPEAR, "spear");
        registerItemModel(ItemHandler.BLOWGUN, "blowgun");
        registerItemModel(ItemHandler.ICE_CRYSTAL, "ice_crystal");
        registerItemModel(ItemHandler.EARTH_TALISMAN, "earth_talisman");
        registerItemModel(ItemHandler.SPAWN_EGG, "spawn_egg");
        registerItemModel(ItemHandler.GRANT_SUNS_BLESSING, "grant_suns_blessing");
        registerItemModel(ItemHandler.BARAKOA_MASK_BLISS, "barakoa_mask_bliss");
        registerItemModel(ItemHandler.BARAKOA_MASK_FEAR, "barakoa_mask_fear");
        registerItemModel(ItemHandler.BARAKOA_MASK_FURY, "barakoa_mask_fury");
        registerItemModel(ItemHandler.BARAKOA_MASK_MISERY, "barakoa_mask_misery");
        registerItemModel(ItemHandler.BARAKOA_MASK_RAGE, "barakoa_mask_rage");
        registerItemModel(ItemHandler.CAPTURED_GROTTOL, "captured_grottol");
        registerItemModel(ItemHandler.GLOWING_JELLY, "glowing_jelly");
        registerItemModel(ItemHandler.NAGA_FANG, "naga_fang");
        registerItemModel(ItemHandler.NAGA_FANG_DAGGER, "naga_fang_dagger");
        registerItemModel(ItemHandler.LOGO, "logo");

        registerItemModel(ItemHandler.BARAKO_MASK, "barako_mask.tbl");

        ModelLoader.setCustomModelResourceLocation(ItemHandler.TEST_STRUCTURE, 0, new ModelResourceLocation("apple"));

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ParticleTextureStitcher.Stitcher.INSTANCE);
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
        ModelLoader.setCustomModelResourceLocation(item, id, resource);
        return resource;
    }
}