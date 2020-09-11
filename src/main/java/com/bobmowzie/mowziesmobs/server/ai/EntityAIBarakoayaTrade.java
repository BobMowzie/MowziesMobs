package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public final class EntityAIBarakoayaTrade extends Goal {
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
            PlayerEntity plyr = barakoaya.getCustomer();
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
