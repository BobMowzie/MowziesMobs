package com.bobmowzie.mowziesmobs.client;

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
import com.bobmowzie.mowziesmobs.client.render.entity.DartRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.FoliaathBabyRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.FoliaathRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.SolarBeamRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.SunstrikeRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.TribeLeaderRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.TribesmanRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.WroughtnautRenderer;
import com.bobmowzie.mowziesmobs.client.sound.SunstrikeSound;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.entity.EntitySunstrike;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntityEggInfo;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeElite;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeHunter;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeLeader;
import com.bobmowzie.mowziesmobs.server.entity.tribe.EntityTribeVillager;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemSpawnEgg;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    @Override
    public void onInit() {
        super.onInit();
        RenderingRegistry.registerEntityRenderingHandler(EntityBabyFoliaath.class, FoliaathBabyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, FoliaathRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWroughtnaut.class, WroughtnautRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeLeader.class, TribeLeaderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeElite.class, TribesmanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeHunter.class, TribesmanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTribeVillager.class, TribesmanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, DartRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySunstrike.class, SunstrikeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySolarBeam.class, SolarBeamRenderer::new);

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

        for (ItemBarakoaMask mask : ItemHandler.INSTANCE.barakoaMasks) {
            registerItemModel(mask, mask.getRegistryName().getResourcePath());
        }

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
