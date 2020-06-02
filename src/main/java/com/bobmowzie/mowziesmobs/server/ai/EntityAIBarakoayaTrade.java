package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public final class EntityAIBarakoayaTrade extends EntityAIBase {
    private final EntityBarakoaya barakoaya;

    public EntityAIBarakoayaTrade(EntityBarakoaya barakoaya) {
        this.barakoaya = barakoaya;
        setMutexBits(5);
    }

    @Override
    public boolean shouldExecute() {
        if (!barakoaya.isEntityAlive() || barakoaya.isInWater() || !barakoaya.onGround || barakoaya.velocityChanged) {
            return false;
        } else {
            EntityPlayer plyr = barakoaya.getCustomer();
            return plyr != null && barakoaya.getDistanceSq(plyr) <= 16 && plyr.openContainer instanceof Container;
        }
    }

    @Override
    public void startExecuting() {
        barakoaya.getNavigator().clearPath();
    }

    @Override
    public void resetTask() {
        barakoaya.setCustomer(null);
    }
}
