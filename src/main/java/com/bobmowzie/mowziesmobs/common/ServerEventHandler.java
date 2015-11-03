package com.bobmowzie.mowziesmobs.common;

import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.common.item.ItemBarakoaMask;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class ServerEventHandler
{
    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityZombie)
        {
            ((EntityCreature) event.entity).tasks.addTask(2, new EntityAIAttackOnCollide((EntityCreature) event.entity, EntityFoliaath.class, 1.0D, false));
            ((EntityCreature) event.entity).targetTasks.addTask(2, new EntityAINearestAttackableTarget((EntityCreature) event.entity, EntityFoliaath.class, 0, true));
        }
        if (event.entity instanceof EntityOcelot)
        {
            ((EntityCreature) event.entity).tasks.addTask(3, new EntityAIAvoidEntity((EntityCreature) event.entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.getEquipmentInSlot(4) != null) {
            if (event.player.getEquipmentInSlot(4).getItem() instanceof ItemBarakoaMask)
            {
                ItemBarakoaMask mask = (ItemBarakoaMask) event.player.getEquipmentInSlot(4).getItem();
                if (mask.maskType == 1) event.player.addPotionEffect(new PotionEffect(3, 0, 0));
                if (mask.maskType == 2) event.player.addPotionEffect(new PotionEffect(1, 0, 0));
                if (mask.maskType == 3) event.player.addPotionEffect(new PotionEffect(5, 0, 0));
                if (mask.maskType == 4) event.player.addPotionEffect(new PotionEffect(8, 0, 0));
                if (mask.maskType == 5) event.player.addPotionEffect(new PotionEffect(11, 0, 0));
            }
        }
    }
}
