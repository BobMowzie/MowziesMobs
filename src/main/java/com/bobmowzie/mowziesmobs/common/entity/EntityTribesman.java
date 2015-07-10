package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;

/**
 * Created by jnad325 on 7/9/15.
 */
public class EntityTribesman extends EntityVillager {
    public EntityTribesman(World world) {
        super(world);
//        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 1, true));
//        tasks.addTask(5, new EntityAIWander(this, 1));
//        tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
}
