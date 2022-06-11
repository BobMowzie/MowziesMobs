package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class BarakoaHurtByTargetAI extends HurtByTargetGoal
{
    public BarakoaHurtByTargetAI(PathfinderMob entity, Class<?>... p_26040_) {
        super(entity, p_26040_);
    }

    @Override
    protected void alertOthers()
    {
        double d0 = this.getFollowDistance();
        AABB aabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d0, 10.0D, d0);
        List<? extends PathfinderMob> listBarakoa = this.mob.level.getEntitiesOfClass(EntityBarakoa.class, aabb, EntitySelector.NO_SPECTATORS.and(e ->
                ((EntityBarakoa)e).isBarakoDevoted()));
        List<? extends PathfinderMob> listBarako = this.mob.level.getEntitiesOfClass(EntityBarako.class, aabb, EntitySelector.NO_SPECTATORS);
        List<PathfinderMob> list = new ArrayList<>();
        list.addAll(listBarakoa);
        list.addAll(listBarako);
        Iterator iterator = list.iterator();

        while(true) {
            Mob mob;
            while(true) {
                if (!iterator.hasNext()) {
                    return;
                }

                mob = (Mob)iterator.next();
                if (this.mob != mob && mob.getTarget() == null && (!(this.mob instanceof TamableAnimal) || ((TamableAnimal)this.mob).getOwner() == ((TamableAnimal)mob).getOwner()) && !mob.isAlliedTo(this.mob.getLastHurtByMob())) {
                    if (this.toIgnoreAlert == null) {
                        break;
                    }

                    boolean flag = false;

                    for(Class<?> oclass : this.toIgnoreAlert) {
                        if (mob.getClass() == oclass) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        break;
                    }
                }
            }

            this.alertOther(mob, this.mob.getLastHurtByMob());
        }
    }

    @Override
    protected double getFollowDistance() {
        return super.getFollowDistance() * 1.7;
    }
}