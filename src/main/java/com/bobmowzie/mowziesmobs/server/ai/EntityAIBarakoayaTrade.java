package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

import java.util.EnumSet;

public final class EntityAIBarakoayaTrade extends Goal {
    private final EntityBarakoaya barakoaya;

    public EntityAIBarakoayaTrade(EntityBarakoaya barakoaya) {
        this.barakoaya = barakoaya;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.TARGET));
    }

    @Override
    public boolean shouldExecute() {
        if (!barakoaya.isAlive() || barakoaya.isInWater() || !barakoaya.isOnGround() || barakoaya.velocityChanged) {
            return false;
        } else {
            PlayerEntity plyr = barakoaya.getCustomer();
            return plyr != null && barakoaya.getDistanceSq(plyr) <= 16 && plyr.openContainer != null;
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
