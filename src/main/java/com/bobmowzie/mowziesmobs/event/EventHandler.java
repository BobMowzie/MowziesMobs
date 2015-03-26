package com.bobmowzie.mowziesmobs.event;

import com.bobmowzie.mowziesmobs.entity.EntityFoliaath;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventHandler
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
}
