package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityPredicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.TargetGoal;
import net.minecraft.world.entity.animal.TameableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.AxisAlignedBB;

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
    public boolean shouldExecute()
    {
        int i = this.goalOwner.getRevengeTimer();
        LivingEntity entitylivingbase = this.goalOwner.getRevengeTarget();
        return i != this.revengeTimerOld && entitylivingbase != null && this.isSuitableTarget(entitylivingbase, EntityPredicate.DEFAULT);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.goalOwner.setTarget(this.goalOwner.getRevengeTarget());
        this.target = this.goalOwner.getTarget();
        this.revengeTimerOld = this.goalOwner.getRevengeTimer();
        this.unseenMemoryTicks = 300;

        if (this.entityCallsForHelp)
        {
            this.alertOthers();
        }

        super.startExecuting();
    }

    protected void alertOthers()
    {
        double d0 = this.getTargetDistance();

        List<PathfinderMob> nearby = this.goalOwner.world.getLoadedEntitiesWithinAABB(EntityBarakoa.class, (new AxisAlignedBB(this.goalOwner.getX(), this.goalOwner.getY(), this.goalOwner.getZ(), this.goalOwner.getX() + 1.0D, this.goalOwner.getY() + 1.0D, this.goalOwner.getZ() + 1.0D)).grow(d0, 10.0D, d0), e ->
                ((EntityBarakoa)e).isBarakoDevoted());
        nearby.addAll(this.goalOwner.world.getLoadedEntitiesWithinAABB(EntityBarako.class, (new AxisAlignedBB(this.goalOwner.getX(), this.goalOwner.getY(), this.goalOwner.getZ(), this.goalOwner.getX() + 1.0D, this.goalOwner.getY() + 1.0D, this.goalOwner.getZ() + 1.0D)).grow(d0, 10.0D, d0)));
        for (PathfinderMob entitycreature : nearby)
        {
            if (this.goalOwner != entitycreature && !(entitycreature.getTarget() instanceof Player) && (!(this.goalOwner instanceof TameableEntity) || ((TameableEntity)this.goalOwner).getOwner() == ((TameableEntity)entitycreature).getOwner()) && this.goalOwner.getRevengeTarget() != null && !entitycreature.isOnSameTeam(this.goalOwner.getRevengeTarget()))
            {
                this.setEntityAttackTarget(entitycreature, this.goalOwner.getRevengeTarget());
            }
        }
    }

    protected void setEntityAttackTarget(PathfinderMob creatureIn, LivingEntity entityLivingBaseIn)
    {
        creatureIn.setTarget(entityLivingBaseIn);
    }

    @Override
    protected double getTargetDistance() {
        return super.getTargetDistance() * 1.7;
    }
}