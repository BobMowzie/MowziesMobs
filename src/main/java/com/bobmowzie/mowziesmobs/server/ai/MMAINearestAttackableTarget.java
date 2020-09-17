package com.bobmowzie.mowziesmobs.server.ai;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MMAINearestAttackableTarget<T extends LivingEntity> extends TargetGoal
{
    protected final Class<T> targetClass;
    private final int targetChance;
    /** Instance of EntityAINearestAttackableTargetSorter. */
    protected final MMAINearestAttackableTarget.Sorter sorter;
    protected final Predicate <? super T > targetEntitySelector;
    protected T targetEntity;
    protected boolean useVerticalDistance;

    public MMAINearestAttackableTarget(CreatureEntity creature, Class<T> classTarget, boolean checkSight)
    {
        this(creature, classTarget, checkSight, false, false);
    }

    public MMAINearestAttackableTarget(CreatureEntity creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby, boolean useVerticalDistance)
    {
        this(creature, classTarget, 10, checkSight, onlyNearby, useVerticalDistance, (Predicate)null);
    }

    public MMAINearestAttackableTarget(CreatureEntity creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, boolean useVerticalDistance, @Nullable final Predicate <? super T > targetSelector)
    {
        super(creature, checkSight, onlyNearby);
        this.targetClass = classTarget;
        this.targetChance = chance;
        this.useVerticalDistance = useVerticalDistance;
        this.sorter = new MMAINearestAttackableTarget.Sorter(creature);
        this.setMutexBits(1);
        this.targetEntitySelector = new Predicate<T>()
        {
            public boolean apply(@Nullable T p_apply_1_)
            {
                if (p_apply_1_ == null)
                {
                    return false;
                }
                else if (targetSelector != null && !targetSelector.apply(p_apply_1_))
                {
                    return false;
                }
                else
                {
                    return !EntityPredicates.NOT_SPECTATING.apply(p_apply_1_) ? false : MMAINearestAttackableTarget.this.isSuitableTarget(p_apply_1_, false);
                }
            }
        };
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else if (this.targetClass != PlayerEntity.class && this.targetClass != ServerPlayerEntity.class)
        {
            List<T> list = this.taskOwner.world.<T>getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                Collections.sort(list, this.sorter);
                this.targetEntity = list.get(0);
                return true;
            }
        }
        else
        {
            this.targetEntity = (T)this.taskOwner.world.getNearestAttackablePlayer(this.taskOwner.posX, this.taskOwner.posY + (double)this.taskOwner.getEyeHeight(), this.taskOwner.posZ, this.getTargetDistance(), this.getTargetDistance(), new Function<PlayerEntity, Double>()
            {
                @Nullable
                public Double apply(@Nullable PlayerEntity p_apply_1_)
                {
                    ItemStack itemstack = p_apply_1_.getItemStackFromSlot(EquipmentSlotType.HEAD);

                    if (itemstack.getItem() == Items.SKULL)
                    {
                        int i = itemstack.getItemDamage();
                        boolean flag = MMAINearestAttackableTarget.this.taskOwner instanceof SkeletonEntity && i == 0;
                        boolean flag1 = MMAINearestAttackableTarget.this.taskOwner instanceof ZombieEntity && i == 2;
                        boolean flag2 = MMAINearestAttackableTarget.this.taskOwner instanceof CreeperEntity && i == 4;

                        if (flag || flag1 || flag2)
                        {
                            return 0.5D;
                        }
                    }

                    return 1.0D;
                }
            }, (Predicate<PlayerEntity>)this.targetEntitySelector);
            return this.targetEntity != null;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance)
    {
        double verticalDistance = 4.0D;
        if (useVerticalDistance) verticalDistance = targetDistance;
        return this.taskOwner.getBoundingBox().grow(targetDistance, verticalDistance, targetDistance);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    public static class Sorter implements Comparator<Entity>
    {
        private final Entity entity;

        public Sorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double d0 = this.entity.getDistanceSq(p_compare_1_);
            double d1 = this.entity.getDistanceSq(p_compare_2_);

            if (d0 < d1)
            {
                return -1;
            }
            else
            {
                return d0 > d1 ? 1 : 0;
            }
        }
    }
}
