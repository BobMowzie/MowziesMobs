package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.common.ai.AINearestAttackableTargetBarakoa;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by jnad325 on 7/23/15.
 */
public class EntityTribeVillager extends EntityTribesman {
    public EntityTribeVillager(World world) {
        super(world);
        targetTasks.addTask(3, new AINearestAttackableTargetBarakoa(this, EntityPlayer.class, 0, true));
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
