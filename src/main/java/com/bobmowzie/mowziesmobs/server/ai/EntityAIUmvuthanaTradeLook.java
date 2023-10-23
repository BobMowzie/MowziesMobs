package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthanaMinion;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class EntityAIUmvuthanaTradeLook extends LookAtPlayerGoal {
    private final EntityUmvuthanaMinion umvuthana;

    public EntityAIUmvuthanaTradeLook(EntityUmvuthanaMinion umvuthana) {
        super(umvuthana, Player.class, 8);
        this.umvuthana = umvuthana;
        setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (umvuthana.isTrading()) {
            this.lookAt = umvuthana.getCustomer();
            return true;
        }
        return false;
    }
}
