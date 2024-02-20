package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UmvuthanaHurtByTargetAI extends HurtByTargetGoal
{
    public UmvuthanaHurtByTargetAI(PathfinderMob entity, Class<?>... p_26040_) {
        super(entity, p_26040_);
        setAlertOthers();
    }

    @Override
    protected void alertOthers()
    {
        double d0 = this.getFollowDistance();
        AABB aabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d0, 10.0D, d0);
        List<? extends PathfinderMob> listUmvuthana = this.mob.level.getEntitiesOfClass(EntityUmvuthana.class, aabb, EntitySelector.NO_SPECTATORS.and(e ->
                ((EntityUmvuthana)e).isUmvuthiDevoted()));
        List<? extends PathfinderMob> listUmvuthi = this.mob.level.getEntitiesOfClass(EntityUmvuthi.class, aabb, EntitySelector.NO_SPECTATORS);
        List<PathfinderMob> list = new ArrayList<>();
        list.addAll(listUmvuthana);
        list.addAll(listUmvuthi);
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