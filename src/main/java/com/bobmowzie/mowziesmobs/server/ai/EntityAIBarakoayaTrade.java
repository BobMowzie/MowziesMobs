package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public final class EntityAIBarakoayaTrade extends Goal {
    private final EntityUmvuthanaMinion barakoaya;

    public EntityAIBarakoayaTrade(EntityUmvuthanaMinion barakoaya) {
        this.barakoaya = barakoaya;
        setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (!barakoaya.isAlive() || barakoaya.isInWater() || !barakoaya.isOnGround() || barakoaya.hurtMarked) {
            return false;
        } else {
            Player plyr = barakoaya.getCustomer();
            return plyr != null && barakoaya.distanceToSqr(plyr) <= 16 && plyr.containerMenu != null;
        }
    }

    @Override
    public void start() {
        barakoaya.getNavigation().stop();
    }

    @Override
    public void stop() {
        barakoaya.setCustomer(null);
    }
}
