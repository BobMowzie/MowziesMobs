package com.bobmowzie.mowziesmobs.server.ai;

import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.google.common.base.Predicate;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Difficulty;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BarakoaAttackTargetAI extends NearestAttackableTargetGoal {
    private Class<? extends Entity> targetClass;
    private int targetChance;
    private NearestAttackableTargetGoal.Sorter attackableTargetSorter;
    private Predicate<Entity> targetEntitySelector;
    private LivingEntity targetEntity;
    private boolean useVerticalDistance;

    public BarakoaAttackTargetAI(CreatureEntity entity, Class<? extends Entity> targetClass, int targetChance, boolean shouldCheckSight, boolean useVerticalDistance) {
        super(entity, shouldCheckSight, false);
        this.targetClass = targetClass;
        this.targetChance = targetChance;
        this.useVerticalDistance = useVerticalDistance;
        this.attackableTargetSorter = new NearestAttackableTargetGoal.Sorter(entity);
        this.setMutexBits(1);
        this.targetEntitySelector = target -> {
            if (target instanceof PlayerEntity) {
                if (entity.world.getDifficulty() == Difficulty.PEACEFUL) return false;
                ItemStack headArmorStack = ((PlayerEntity) target).inventory.armorInventory.get(3);
                if (headArmorStack.getItem() instanceof BarakoaMask) {
                    return false;
                }
            }
            return target instanceof LivingEntity && BarakoaAttackTargetAI.this.isSuitableTarget((LivingEntity) target, false);
        };
    }

    @Override
    protected boolean isSuitableTarget(@Nullable LivingEntity entity, EntityPredicate predicate) {
        if (target instanceof PlayerEntity) {
            if (entity.world.getDifficulty() == Difficulty.PEACEFUL) return false;
            ItemStack headArmorStack = ((PlayerEntity) target).inventory.armorInventory.get(3);
            if (headArmorStack.getItem() instanceof BarakoaMask) {
                return false;
            }
        }
        return super.isSuitableTarget(entity, predicate);
    }
}
