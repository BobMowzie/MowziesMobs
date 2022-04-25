package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BarakoaHurtByTargetAI extends TargetGoal
{
    private final boolean entityCallsForHelp;
    /** Store the previous revengeTimer value */
    private int revengeTimerOld;
    private final Class<?>[] excludedReinforcementTypes;

    public BarakoaHurtByTargetAI(PathfinderMob creatureIn, boolean entityCallsForHelpIn, Class<?>... excludedReinforcementTypes)
    {
        super(creatureIn, true);
        this.entityCallsForHelp = entityCallsForHelpIn;
        this.excludedReinforcementTypes = excludedReinforcementTypes;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse()
    {
        int i = this.mob.getLastHurtByMobTimestamp();
        LivingEntity entitylivingbase = this.mob.getLastHurtByMob();
        return i != this.revengeTimerOld && entitylivingbase != null && this.canAttack(entitylivingbase, TargetingConditions.DEFAULT);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start()
    {
        this.mob.setTarget(this.mob.getLastHurtByMob());
        this.targetMob = this.mob.getTarget();
        this.revengeTimerOld = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;

        if (this.entityCallsForHelp)
        {
            this.alertOthers();
        }

        super.start();
    }

    protected void alertOthers()
    {
        double d0 = this.getFollowDistance();

        List<PathfinderMob> nearby = this.mob.level.getLoadedEntitiesOfClass(EntityBarakoa.class, (new AABB(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getX() + 1.0D, this.mob.getY() + 1.0D, this.mob.getZ() + 1.0D)).inflate(d0, 10.0D, d0), e ->
                ((EntityBarakoa)e).isBarakoDevoted());
        nearby.addAll(this.mob.level.getLoadedEntitiesOfClass(EntityBarako.class, (new AABB(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getX() + 1.0D, this.mob.getY() + 1.0D, this.mob.getZ() + 1.0D)).inflate(d0, 10.0D, d0)));
        for (PathfinderMob entitycreature : nearby)
        {
            if (this.mob != entitycreature && !(entitycreature.getTarget() instanceof Player) && (!(this.mob instanceof TamableAnimal) || ((TamableAnimal)this.mob).getOwner() == ((TamableAnimal)entitycreature).getOwner()) && this.mob.getLastHurtByMob() != null && !entitycreature.isAlliedTo(this.mob.getLastHurtByMob()))
            {
                this.setEntityAttackTarget(entitycreature, this.mob.getLastHurtByMob());
            }
        }
    }

    protected void setEntityAttackTarget(PathfinderMob creatureIn, LivingEntity entityLivingBaseIn)
    {
        creatureIn.setTarget(entityLivingBaseIn);
    }

    @Override
    protected double getFollowDistance() {
        return super.getFollowDistance() * 1.7;
    }
}