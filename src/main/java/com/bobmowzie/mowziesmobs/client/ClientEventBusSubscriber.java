package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoayaTrade;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.MaskType;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBoulder;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import com.bobmowzie.mowziesmobs.server.item.ModSpawnEggItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MowziesMobs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.BABY_FOLIAATH.get(), RenderFoliaathBaby::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.FOLIAATH.get(), RenderFoliaath::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.WROUGHTNAUT.get(), RenderWroughtnaut::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.BARAKO.get(), RenderBarako::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.BARAKOANA.get(), RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.BARAKOAN_TO_BARAKOANA.get(), RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.BARAKOA_VILLAGER.get(), RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.BARAKOAN_TO_PLAYER.get(), RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.BARAKOAYA_TO_PLAYER.get(), RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.BARAKOAYA.get(), RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.FROSTMAW.get(), RenderFrostmaw::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.GROTTOL.get(), RenderGrottol::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.LANTERN.get(), RenderLantern::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.NAGA.get(), RenderNaga::new);
//        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.SCULPTOR.get(), RenderSculptor::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.DART.get(), RenderDart::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.SUNSTRIKE.get(), RenderSunstrike::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.SOLAR_BEAM.get(), RenderSolarBeam::new);
        for (RegistryObject<EntityType<EntityBoulder>> boulderType : EntityHandler.BOULDERS) {
            RenderingRegistry.registerEntityRenderingHandler(boulderType.get(), RenderBoulder::new);
        }
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.AXE_ATTACK.get(), RenderAxeAttack::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.POISON_BALL.get(), RenderPoisonBall::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.ICE_BALL.get(), RenderIceBall::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.ICE_BREATH.get(), RenderNothing::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.FROZEN_CONTROLLER.get(), RenderNothing::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.SUPER_NOVA.get(), RenderSuperNova::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.FALLING_BLOCK.get(), RenderFallingBlock::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.BLOCK_SWAPPER.get(), RenderNothing::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandler.CAMERA_SHAKE.get(), RenderNothing::new);

        MenuScreens.register(ContainerHandler.CONTAINER_BARAKOAYA_TRADE, GuiBarakoayaTrade::new);
        MenuScreens.register(ContainerHandler.CONTAINER_BARAKO_TRADE, GuiBarakoTrade::new);
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent modelRegistryEvent) {
        for (String item : MMModels.HAND_MODEL_ITEMS) {
            ModelLoader.addSpecialModel(new ModelResourceLocation(MowziesMobs.MODID + ":" + item + "_in_hand", "inventory"));
        }
        for (MaskType type : MaskType.values()) {
            ModelLoader.addSpecialModel(new ModelResourceLocation(MowziesMobs.MODID + ":barakoa_mask_" + type.name + "_frame", "inventory"));
        }
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        ModSpawnEggItem.initSpawnEggs();
    }
}