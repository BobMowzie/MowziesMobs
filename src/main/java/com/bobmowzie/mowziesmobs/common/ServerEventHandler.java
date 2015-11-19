package com.bobmowzie.mowziesmobs.common;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import com.bobmowzie.mowziesmobs.common.entity.EntityFoliaath;
import com.bobmowzie.mowziesmobs.common.item.ItemBarakoaMask;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;

public class ServerEventHandler
{
    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event)
    {
        if (event.world.isRemote)
        {
            return;
        }
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
        if (event.side == Side.CLIENT || event.phase == Phase.END)
        {
            return;
        }
        ItemStack headArmorStack = event.player.getEquipmentInSlot(4);
        if (headArmorStack == null)
        {
            return;
        }
        Item headItemStack = headArmorStack.getItem();
        if (headItemStack instanceof ItemBarakoaMask)
        {
            ItemBarakoaMask mask = (ItemBarakoaMask) headItemStack;
            event.player.addPotionEffect(new PotionEffect(mask.getPotionEffectId(), 0, 0));
        }
    }
}
