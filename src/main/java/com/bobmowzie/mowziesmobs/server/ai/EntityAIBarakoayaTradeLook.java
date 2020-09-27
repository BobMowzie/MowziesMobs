package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaya;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class EntityAIBarakoayaTradeLook extends LookAtGoal {
    private final EntityBarakoaya barakoaya;

    public EntityAIBarakoayaTradeLook(EntityBarakoaya barakoaya) {
        super(barakoaya, PlayerEntity.class, 8);
        this.barakoaya = barakoaya;
        setMutexFlags(EnumSet.of(Flag.LOOK));
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
