package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIBarakoayaTradeLook extends EntityAIWatchClosest {
    private final EntityBarakoaya barakoaya;

    public EntityAIBarakoayaTradeLook(EntityBarakoaya barakoaya) {
        super(barakoaya, EntityPlayer.class, 8);
        this.barakoaya = barakoaya;
    }

    @Override
    public boolean shouldExecute() {
        if (barakoaya.isTrading()) {
            this.closestEntity = barakoaya.getCustomer();
            return true;
        }
        return false;
    }
}
