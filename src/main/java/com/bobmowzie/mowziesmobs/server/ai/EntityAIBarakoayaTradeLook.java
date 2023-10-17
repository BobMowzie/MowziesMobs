package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaVillager;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class EntityAIBarakoayaTradeLook extends LookAtPlayerGoal {
    private final EntityBarakoaVillager barakoaya;

    public EntityAIBarakoayaTradeLook(EntityBarakoaVillager barakoaya) {
        super(barakoaya, Player.class, 8);
        this.barakoaya = barakoaya;
        setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (barakoaya.isTrading()) {
            this.lookAt = barakoaya.getCustomer();
            return true;
        }
        return false;
    }
}
