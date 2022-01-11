package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import net.minecraft.world.entity.ai.goal.LookAtGoal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class EntityAIBarakoayaTradeLook extends LookAtGoal {
    private final EntityBarakoaVillager barakoaya;

    public EntityAIBarakoayaTradeLook(EntityBarakoaVillager barakoaya) {
        super(barakoaya, Player.class, 8);
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
