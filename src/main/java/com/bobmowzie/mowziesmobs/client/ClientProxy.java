package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiBarakoayaTrade;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.client.sound.IceBreathSound;
import com.bobmowzie.mowziesmobs.client.sound.NagaSwoopSound;
import com.bobmowzie.mowziesmobs.client.sound.SpawnBoulderChargeSound;
import com.bobmowzie.mowziesmobs.client.sound.SunstrikeSound;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityDynamicsTester;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Timer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ClientProxy extends ServerProxy {

    private Entity referencedMob = null;

    @Override
    public void init(final IEventBus modbus) {
        super.init(modbus);

        modbus.register(MMModels.class);
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(FrozenRenderHandler.INSTANCE);

        RenderingRegistry.registerEntityRenderingHandler(EntityBabyFoliaath.class, RenderFoliaathBaby::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFoliaath.class, RenderFoliaath::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWroughtnaut.class, RenderWroughtnaut::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarako.class, RenderBarako::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarakoana.class, RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarakoanToBarakoana.class, RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarakoaya.class, RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBarakoanToPlayer.class, RenderBarakoa::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFrostmaw.class, RenderFrostmaw::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGrottol.class, RenderGrottol::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLantern.class, RenderLantern::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityNaga.class, RenderNaga::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityDynamicsTester.class, RenderDynamicsTester::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, RenderDart::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySunstrike.class, RenderSunstrike::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySolarBeam.class, RenderSolarBeam::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBoulder.class, RenderBoulder::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityAxeAttack.class, RenderAxeAttack::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPoisonBall.class, RenderPoisonBall::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityIceBall.class, RenderIceBall::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySuperNova.class, RenderSuperNova::new);

        ScreenManager.registerFactory(ContainerHandler.CONTAINER_BARAKOAYA_TRADE, GuiBarakoayaTrade::new);
        ScreenManager.registerFactory(ContainerHandler.CONTAINER_BARAKO_TRADE, GuiBarakoTrade::new);

        ModelLoader.addSpecialModel(new ModelResourceLocation(MowziesMobs.MODID + ":wrought_axe_in_hand", "inventory"));
        for (MaskType type : MaskType.values()) {
            ModelLoader.addSpecialModel(new ModelResourceLocation(MowziesMobs.MODID + ":barakoa_mask_" + type.name + "_frame", "inventory"));
        }
    }

    @Override
    public void onLateInit(final IEventBus modbus) {

    }

    @Override
    public void playSunstrikeSound(EntitySunstrike strike) {
        Minecraft.getInstance().getSoundHandler().play(new SunstrikeSound(strike));
    }

    @Override
    public void playIceBreathSound(Entity entity) {
        Minecraft.getInstance().getSoundHandler().play(new IceBreathSound(entity));
    }

    @Override
    public void playBoulderChargeSound(PlayerEntity player) {
        Minecraft.getInstance().getSoundHandler().play(new SpawnBoulderChargeSound(player));
    }

    @Override
    public void playNagaSwoopSound(EntityNaga naga) {
        Minecraft.getInstance().getSoundHandler().play(new NagaSwoopSound(naga));
    }

    @Override
    public void solarBeamHitWroughtnaught(LivingEntity caster) {
        if (caster == Minecraft.getInstance().player) {
            long now = System.currentTimeMillis();
            if (now - ClientEventHandler.INSTANCE.lastWroughtnautHitTime > 500) {
                ClientEventHandler.INSTANCE.startWroughtnautHitTime = now;
            }
            ClientEventHandler.INSTANCE.lastWroughtnautHitTime = now;
        }
    }

    public Entity getReferencedMob() {
        return referencedMob;
    }

    public void setReferencedMob(Entity referencedMob) {
        this.referencedMob = referencedMob;
    }
}
