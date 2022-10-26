package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoayaTrade;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderBase;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderProjectile;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityHandler.BABY_FOLIAATH.get(), RenderFoliaathBaby::new);
        EntityRenderers.register(EntityHandler.FOLIAATH.get(), RenderFoliaath::new);
        EntityRenderers.register(EntityHandler.WROUGHTNAUT.get(), RenderWroughtnaut::new);
        EntityRenderers.register(EntityHandler.BARAKO.get(), RenderBarako::new);
        EntityRenderers.register(EntityHandler.BARAKOANA.get(), RenderBarakoa::new);
        EntityRenderers.register(EntityHandler.BARAKOAN_TO_BARAKOANA.get(), RenderBarakoa::new);
        EntityRenderers.register(EntityHandler.BARAKOA_VILLAGER.get(), RenderBarakoa::new);
        EntityRenderers.register(EntityHandler.BARAKOAN_TO_PLAYER.get(), RenderBarakoa::new);
        EntityRenderers.register(EntityHandler.BARAKOAYA_TO_PLAYER.get(), RenderBarakoa::new);
        EntityRenderers.register(EntityHandler.BARAKOAYA.get(), RenderBarakoa::new);
        EntityRenderers.register(EntityHandler.FROSTMAW.get(), RenderFrostmaw::new);
        EntityRenderers.register(EntityHandler.GROTTOL.get(), RenderGrottol::new);
        EntityRenderers.register(EntityHandler.LANTERN.get(), RenderLantern::new);
        EntityRenderers.register(EntityHandler.NAGA.get(), RenderNaga::new);
        EntityRenderers.register(EntityHandler.SCULPTOR.get(), RenderSculptor::new);

        EntityRenderers.register(EntityHandler.DART.get(), RenderDart::new);
        EntityRenderers.register(EntityHandler.SUNSTRIKE.get(), RenderSunstrike::new);
        EntityRenderers.register(EntityHandler.SOLAR_BEAM.get(), RenderSolarBeam::new);
        EntityRenderers.register(EntityHandler.BOULDER_PROJECTILE.get(), RenderBoulder::new);
        EntityRenderers.register(EntityHandler.PILLAR.get(), RenderPillar::new);
        EntityRenderers.register(EntityHandler.PILLAR_PIECE.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.AXE_ATTACK.get(), RenderAxeAttack::new);
        EntityRenderers.register(EntityHandler.POISON_BALL.get(), RenderPoisonBall::new);
        EntityRenderers.register(EntityHandler.ICE_BALL.get(), RenderIceBall::new);
        EntityRenderers.register(EntityHandler.ICE_BREATH.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.FROZEN_CONTROLLER.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.SUPER_NOVA.get(), RenderSuperNova::new);
        EntityRenderers.register(EntityHandler.FALLING_BLOCK.get(), RenderFallingBlock::new);
        EntityRenderers.register(EntityHandler.BLOCK_SWAPPER.get(), RenderNothing::new);
        EntityRenderers.register(EntityHandler.CAMERA_SHAKE.get(), RenderNothing::new);

        MenuScreens.register(ContainerHandler.CONTAINER_BARAKOAYA_TRADE, GuiBarakoayaTrade::new);
        MenuScreens.register(ContainerHandler.CONTAINER_BARAKO_TRADE, GuiBarakoTrade::new);
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent modelRegistryEvent) {
        for (String item : MMModels.HAND_MODEL_ITEMS) {
            ForgeModelBakery.addSpecialModel(new ModelResourceLocation(MowziesMobs.MODID + ":" + item + "_in_hand", "inventory"));
        }
        for (MaskType type : MaskType.values()) {
            ForgeModelBakery.addSpecialModel(new ModelResourceLocation(MowziesMobs.MODID + ":barakoa_mask_" + type.name + "_frame", "inventory"));
        }
    }
}