package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by jnad325 on 7/9/15.
 */
public class EntityTribeLeader extends MMEntityBase {
    public EntityTribeLeader(World world) {
        super(world);
        tasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
    }
}
