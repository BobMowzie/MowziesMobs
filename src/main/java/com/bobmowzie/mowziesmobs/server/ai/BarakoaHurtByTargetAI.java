package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoaSunblocker;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class BarakoaHurtByTargetAI extends TargetGoal
{
    private final boolean entityCallsForHelp;
    /** Store the previous revengeTimer value */
    private int revengeTimerOld;
    private final Class<?>[] excludedReinforcementTypes;

    public BarakoaHurtByTargetAI(CreatureEntity creatureIn, boolean entityCallsForHelpIn, Class<?>... excludedReinforcementTypes)
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
        this.goalOwner.setAttackTarget(this.goalOwner.getRevengeTarget());
        this.target = this.goalOwner.getAttackTarget();
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

        List<CreatureEntity> nearby = this.goalOwner.world.getLoadedEntitiesWithinAABB(EntityBarakoa.class, (new AxisAlignedBB(this.goalOwner.getPosX(), this.goalOwner.getPosY(), this.goalOwner.getPosZ(), this.goalOwner.getPosX() + 1.0D, this.goalOwner.getPosY() + 1.0D, this.goalOwner.getPosZ() + 1.0D)).grow(d0, 10.0D, d0), e ->
                ((EntityBarakoa)e).isBarakoDevoted());
        nearby.addAll(this.goalOwner.world.getLoadedEntitiesWithinAABB(EntityBarako.class, (new AxisAlignedBB(this.goalOwner.getPosX(), this.goalOwner.getPosY(), this.goalOwner.getPosZ(), this.goalOwner.getPosX() + 1.0D, this.goalOwner.getPosY() + 1.0D, this.goalOwner.getPosZ() + 1.0D)).grow(d0, 10.0D, d0)));
        for (CreatureEntity entitycreature : nearby)
        {
            if (this.goalOwner != entitycreature && !(entitycreature.getAttackTarget() instanceof PlayerEntity) && (!(this.goalOwner instanceof TameableEntity) || ((TameableEntity)this.goalOwner).getOwner() == ((TameableEntity)entitycreature).getOwner()) && !entitycreature.isOnSameTeam(this.goalOwner.getRevengeTarget()))
            {
                this.setEntityAttackTarget(entitycreature, this.goalOwner.getRevengeTarget());
            }
        }
    }

    protected void setEntityAttackTarget(CreatureEntity creatureIn, LivingEntity entityLivingBaseIn)
    {
        creatureIn.setAttackTarget(entityLivingBaseIn);
    }

    @Override
    protected double getTargetDistance() {
        return super.getTargetDistance() * 1.7;
    }
}