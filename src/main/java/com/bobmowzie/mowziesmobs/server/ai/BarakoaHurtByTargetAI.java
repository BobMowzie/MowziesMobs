package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarakoa;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class BarakoaHurtByTargetAI extends EntityAITarget
{
    private final boolean entityCallsForHelp;
    /** Store the previous revengeTimer value */
    private int revengeTimerOld;
    private final Class<?>[] excludedReinforcementTypes;

    public BarakoaHurtByTargetAI(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?>... excludedReinforcementTypes)
    {
        super(creatureIn, true);
        this.entityCallsForHelp = entityCallsForHelpIn;
        this.excludedReinforcementTypes = excludedReinforcementTypes;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        int i = this.taskOwner.getRevengeTimer();
        EntityLivingBase entitylivingbase = this.taskOwner.getRevengeTarget();
        return i != this.revengeTimerOld && entitylivingbase != null && this.isSuitableTarget(entitylivingbase, false);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
        this.target = this.taskOwner.getAttackTarget();
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();
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

        List<EntityCreature> nearby = this.taskOwner.world.getEntitiesWithinAABB(EntityBarakoa.class, (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).grow(d0, 10.0D, d0), e ->
                ((EntityBarakoa)e).isBarakoDevoted());
        nearby.addAll(this.taskOwner.world.getEntitiesWithinAABB(EntityBarako.class, (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).grow(d0, 10.0D, d0)));
        for (EntityCreature entitycreature : nearby)
        {
            if (this.taskOwner != entitycreature && !(entitycreature.getAttackTarget() instanceof EntityPlayer) && (!(this.taskOwner instanceof EntityTameable) || ((EntityTameable)this.taskOwner).getOwner() == ((EntityTameable)entitycreature).getOwner()) && !entitycreature.isOnSameTeam(this.taskOwner.getRevengeTarget()))
            {
                    this.setEntityAttackTarget(entitycreature, this.taskOwner.getRevengeTarget());
            }
        }
    }

    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
    {
        creatureIn.setAttackTarget(entityLivingBaseIn);
    }
}