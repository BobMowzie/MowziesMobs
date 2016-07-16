package com.bobmowzie.mowziesmobs.server.ai;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.google.common.base.Predicate;

public class BarakoaAttackTargetAI extends EntityAITarget {
    private Class<? extends Entity> targetClass;
    private int targetChance;
    private EntityAINearestAttackableTarget.Sorter attackableTargetSorter;
    private Predicate<Entity> targetEntitySelector;
    private EntityLivingBase targetEntity;

    public BarakoaAttackTargetAI(EntityCreature entity, Class<? extends Entity> targetClass, int targetChance, boolean shouldCheckSight) {
        super(entity, shouldCheckSight, false);
        this.targetClass = targetClass;
        this.targetChance = targetChance;
        this.attackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(entity);
        this.setMutexBits(1);
        this.targetEntitySelector = target -> {
            if (target instanceof EntityPlayer) {
                ItemStack headArmorStack = ((EntityPlayer) target).inventory.armorItemInSlot(3);
                if (headArmorStack != null) {
                    Item headItem = headArmorStack.getItem();
                    if (headItem instanceof ItemBarakoaMask) {
                        return false;
                    }
                }
            }
            return target instanceof EntityLivingBase && BarakoaAttackTargetAI.this.isSuitableTarget((EntityLivingBase) target, false);
        };
    }

    @Override
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        } else {
            double targetDistance = this.getTargetDistance();
            List list = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance), this.targetEntitySelector);
            Collections.sort(list, this.attackableTargetSorter);

            if (list.isEmpty()) {
                return false;
            } else {
                this.targetEntity = (EntityLivingBase) list.get(0);
                return true;
            }
        }
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
}
