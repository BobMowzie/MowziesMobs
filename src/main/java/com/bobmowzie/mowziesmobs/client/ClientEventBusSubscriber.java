package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.gui.GuiSculptorTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiUmvuthanaTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiUmvuthiTrade;
import com.bobmowzie.mowziesmobs.client.render.block.GongRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityHandler.BABY_FOLIAATH.get(), RenderFoliaathBaby::new);
        EntityRenderers.register(EntityHandler.FOLIAATH.get(), RenderFoliaath::new);
        EntityRenderers.register(EntityHandler.WROUGHTNAUT.get(), RenderWroughtnaut::new);
        EntityRenderers.register(EntityHandler.UMVUTHI.get(), RenderUmvuthi::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_RAPTOR.get(), RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR.get(), RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_MINION.get(), RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER.get(), RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER.get(), RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_CRANE.get(), RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.FROSTMAW.get(), RenderFrostmaw::new);
        EntityRenderers.register(EntityHandler.GROTTOL.get(), RenderGrottol::new);
        EntityRenderers.register(EntityHandler.LANTERN.get(), RenderLantern::new);
        EntityRenderers.register(EntityHandler.NAGA.get(), RenderNaga::new);
        EntityRenderers.register(EntityHandler.SCULPTOR.get(), RenderSculptor::new);

        EntityRenderers.register(EntityHandler.DART.get(), RenderDart::new);
        EntityRenderers.register(EntityHandler.SUNSTRIKE.get(), RenderSunstrike::new);
        EntityRenderers.register(EntityHandler.SOLAR_BEAM.get(), RenderSolarBeam::new);
        EntityRenderers.register(EntityHandler.BOULDER_PROJECTILE.get(), RenderBoulder::new);
        EntityRenderers.register(EntityHandler.BOULDER_SCULPTOR.get(), RenderBoulder::new);
        EntityRenderers.register(EntityHandler.PILLAR.get(), RenderPillar::new);
        EntityRenderers.register(EntityHandler.PILLAR_SCULPTOR.get(), RenderPillar::new);
        EntityRenderers.register(EntityHandler.PILLAR_PIECE.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.AXE_ATTACK.get(), RenderAxeAttack::new);
        EntityRenderers.register(EntityHandler.POISON_BALL.get(), RenderPoisonBall::new);
        EntityRenderers.register(EntityHandler.ICE_BALL.get(), RenderIceBall::new);
        EntityRenderers.register(EntityHandler.ICE_BREATH.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.FROZEN_CONTROLLER.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.SUPER_NOVA.get(), RenderSuperNova::new);
        EntityRenderers.register(EntityHandler.FALLING_BLOCK.get(), RenderFallingBlock::new);
        EntityRenderers.register(EntityHandler.BLOCK_SWAPPER.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.BLOCK_SWAPPER_TUNNELING.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.CAMERA_SHAKE.get(), RenderNothing::new);
//        EntityRenderers.register(EntityHandler.TEST_ENTITY.get(), RenderNothing::new);

        BlockEntityRenderers.register(BlockEntityHandler.GONG_BLOCK_ENTITY.get(), GongRenderer::new);
        EntityRenderers.register(EntityHandler.ROCK_SLING.get(), RenderRockSling::new);

        MenuScreens.register(ContainerHandler.CONTAINER_UMVUTHANA_TRADE.get(), GuiUmvuthanaTrade::new);
        MenuScreens.register(ContainerHandler.CONTAINER_UMVUTHI_TRADE.get(), GuiUmvuthiTrade::new);
        MenuScreens.register(ContainerHandler.CONTAINER_SCULPTOR_TRADE.get(), GuiSculptorTrade::new);
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelEvent.RegisterAdditional modelRegistryEvent) {
        for (String item : MMModels.HAND_MODEL_ITEMS) {
        	modelRegistryEvent.register(new ModelResourceLocation(new ResourceLocation(MowziesMobs.MODID, item + "_in_hand"), "inventory"));
        }
        for (MaskType type : MaskType.values()) {
        	modelRegistryEvent.register(new ModelResourceLocation(new ResourceLocation(MowziesMobs.MODID,"umvuthana_mask_" + type.name + "_frame"), "inventory"));
        }
        modelRegistryEvent.register(new ModelResourceLocation(new ResourceLocation(MowziesMobs.MODID, "sol_visage_frame"), "inventory"));
    }
}