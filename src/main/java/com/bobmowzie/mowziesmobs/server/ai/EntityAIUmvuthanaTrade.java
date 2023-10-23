package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public final class EntityAIUmvuthanaTrade extends Goal {
    private final EntityUmvuthanaMinion umvuthana;

    public EntityAIUmvuthanaTrade(EntityUmvuthanaMinion umvuthana) {
        this.umvuthana = umvuthana;
        setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (!umvuthana.isAlive() || umvuthana.isInWater() || !umvuthana.isOnGround() || umvuthana.hurtMarked) {
            return false;
        } else {
            Player plyr = umvuthana.getCustomer();
            return plyr != null && umvuthana.distanceToSqr(plyr) <= 16 && plyr.containerMenu != null;
        }
    }

    @Override
    public void start() {
        umvuthana.getNavigation().stop();
    }

    @Override
    public void stop() {
        umvuthana.setCustomer(null);
    }
}
