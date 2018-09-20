package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.client.sound.IceBreathSound;
import com.bobmowzie.mowziesmobs.client.sound.SpawnBoulderChargeSound;
import com.bobmowzie.mowziesmobs.server.entity.EntityDynamicsTester;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.client.sound.SunstrikeSound;
import com.bobmowzie.mowziesmobs.server.ServerProxy;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntityEggInfo;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
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
        RenderingRegistry.registerEntityRenderingHandler(EntityRing.class, RenderRing::new);
    }

    @Override
    public void onLateInit() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                MowzieEntityEggInfo info = EntityHandler.INSTANCE.getEntityEggInfo(ItemSpawnEgg.getEntityIdFromItem(stack));
                return info == null ? -1 : (tintIndex == 0 ? info.primaryColor : info.secondaryColor);
            }
        }, ItemHandler.SPAWN_EGG);
    }

    @Override
    public void playSunstrikeSound(EntitySunstrike strike) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new SunstrikeSound(strike));
    }

    @Override
    public void playIceBreathSound(Entity entity) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new IceBreathSound(entity));
    }

    @Override
    public void playBoulderChargeSound(EntityPlayer player) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new SpawnBoulderChargeSound(player));
    }

    @Override
    public void solarBeamHitWroughtnaught(EntityLivingBase caster) {
        if (caster == Minecraft.getMinecraft().player) {
            long now = System.currentTimeMillis();
            if (now - ClientEventHandler.INSTANCE.lastWroughtnautHitTime > 500) {
                ClientEventHandler.INSTANCE.startWroughtnautHitTime = now;
            }
            ClientEventHandler.INSTANCE.lastWroughtnautHitTime = now;
        }
    }
}
