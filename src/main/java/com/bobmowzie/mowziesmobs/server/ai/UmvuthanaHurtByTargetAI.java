package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthana;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UmvuthanaHurtByTargetAI extends HurtByTargetGoal
{
    private boolean checkSight;
    private int unseenTicks;

    public UmvuthanaHurtByTargetAI(PathfinderMob entity, boolean checkSight, Class<?>... p_26040_) {
        super(entity, p_26040_);
        setAlertOthers();
        this.checkSight = checkSight;
    }

    @Override
    protected void alertOthers()
    {
        double d0 = this.getFollowDistance();
        AABB aabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d0, 10.0D, d0);
        List<? extends PathfinderMob> listUmvuthana = this.mob.level().getEntitiesOfClass(EntityUmvuthana.class, aabb, EntitySelector.NO_SPECTATORS.and(e ->
                ((EntityUmvuthana)e).isUmvuthiDevoted()));
        List<? extends PathfinderMob> listUmvuthi = this.mob.level().getEntitiesOfClass(EntityUmvuthi.class, aabb, EntitySelector.NO_SPECTATORS);
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

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            livingentity = this.targetMob;
        }

        if (livingentity == null) {
            return false;
        } else if (!this.mob.canAttack(livingentity)) {
            return false;
        } else {
            Team team = this.mob.getTeam();
            Team team1 = livingentity.getTeam();
            if (team != null && team1 == team) {
                return false;
            } else {
                double d0 = this.getFollowDistance();
                if (this.mob.distanceToSqr(livingentity) > d0 * d0) {
                    return false;
                } else {
                    if (this.checkSight) {
                        if (this.mob.getSensing().hasLineOfSight(livingentity)) {
                            this.unseenTicks = 0;
                        } else if (++this.unseenTicks > reducedTickDelay(this.unseenMemoryTicks)) {
                            return false;
                        }
                    }

                    this.mob.setTarget(livingentity);
                    return true;
                }
            }
        }
    }

    public void start() {
        super.start();
        this.unseenTicks = 0;
    }
}