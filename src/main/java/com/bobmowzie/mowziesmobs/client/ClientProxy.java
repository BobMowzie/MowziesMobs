package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.server.entity.*;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.*;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleTextureStitcher;
import com.bobmowzie.mowziesmobs.client.sound.SunstrikeSound;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemSpawnEgg;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    @Override
    public void onInit() {
        super.onInit();
        RenderingRegistry.registerEntityRenderingHandler(EntityBabyFoliaath.class, RenderFoliaathBaby::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, RenderFoliaath::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWroughtnaut.class, RenderWroughtnaut::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarako.class, RenderBarako::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarakoana.class, RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarakoanToBarakoana.class, RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarakoaya.class, RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarakoanToPlayer.class, RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, RenderDart::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySunstrike.class, RenderSunstrike::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySolarBeam.class, RenderSolarBeam::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRing.class, RenderRing::new);

        TabulaModelHandler.INSTANCE.addDomain(MowziesMobs.MODID);

        registerBlockModel(BlockHandler.INSTANCE.paintedAcacia, "painted_acacia");
        registerBlockModel(BlockHandler.INSTANCE.paintedAcaciaSlab, "painted_acacia_slab");

        registerItemModel(ItemHandler.INSTANCE.foliaathSeed, "foliaath_seed");
        registerItemModel(ItemHandler.INSTANCE.mobRemover, "mob_remover");
        registerItemModel(ItemHandler.INSTANCE.wroughtAxe, "wrought_axe.tbl");
        registerItemModel(ItemHandler.INSTANCE.wroughtHelmet, "wrought_helmet.tbl");
        registerItemModel(ItemHandler.INSTANCE.dart, "dart");
        registerItemModel(ItemHandler.INSTANCE.spear, "spear");
        registerItemModel(ItemHandler.INSTANCE.blowgun, "blowgun");
        registerItemModel(ItemHandler.INSTANCE.spawnEgg, "spawn_egg");
        registerItemModel(ItemHandler.INSTANCE.grantSunsBlessing, "grant_suns_blessing");

        for (ItemBarakoaMask mask : ItemHandler.INSTANCE.barakoaMasks.values()) {
            registerItemModel(mask, mask.getRegistryName().getResourcePath());
        }
        registerItemModel(ItemHandler.INSTANCE.barakoMask, "barako_mask.tbl");

        ModelLoader.setCustomModelResourceLocation(ItemHandler.INSTANCE.testStructure, 0, new ModelResourceLocation("apple"));

        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ParticleTextureStitcher.Stitcher.INSTANCE);
    }

    @Override
    public void onLateInit() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                MowzieEntityEggInfo info = EntityHandler.INSTANCE.getEntityEggInfo(ItemSpawnEgg.getEntityIdFromItem(stack));
                return info == null ? -1 : (tintIndex == 0 ? info.primaryColor : info.secondaryColor);
            }
        }, ItemHandler.INSTANCE.spawnEgg);
    }

    @Override
    public void playSunstrikeSound(EntitySunstrike strike) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new SunstrikeSound(strike));
    }

    private ModelResourceLocation registerBlockModel(Block block, String name) {
        return registerItemModel(Item.getItemFromBlock(block), 0, name);
    }

    private ModelResourceLocation registerItemModel(Item item, String name) {
        return registerItemModel(item, 0, name);
    }

    private ModelResourceLocation registerItemModel(Item item, int id, String name) {
        ModelResourceLocation resource = new ModelResourceLocation(MowziesMobs.MODID + ':' + name, "inventory");
        ModelLoader.setCustomModelResourceLocation(item, id, resource);
        return resource;
    }
}
