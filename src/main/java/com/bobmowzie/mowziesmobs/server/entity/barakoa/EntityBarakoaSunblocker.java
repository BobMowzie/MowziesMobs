package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.EnumSet;

public class EntityBarakoaSunblocker extends EntityBarakoaya {

    public EntityBarakoaSunblocker(EntityType<? extends EntityBarakoaya> type, World world) {
        super(type, world);
        setWeapon(3);
        setMask(MaskType.FAITH);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new HealTargetGoal(this));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, PlayerEntity.class, 7.0F, 0.8D, 0.6D, target -> {
            if (target instanceof PlayerEntity) {
                if (this.world.getDifficulty() == Difficulty.PEACEFUL) return false;
                if (getAttackTarget() == target) return true;
                if (getAttackTarget() instanceof EntityBarako) return false;
                if (getAnimation() != NO_ANIMATION) return false;
                ItemStack headArmorStack = ((PlayerEntity) target).inventory.armorInventory.get(3);
                return !(headArmorStack.getItem() instanceof BarakoaMask);
            }
            return true;
        }));
    }

    @Override
    protected void registerTargetGoals() {
        super.registerTargetGoals();
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<EntityBarako>(this, EntityBarako.class, 0, false, false, target -> {
            if (target instanceof MobEntity) {
                return ((MobEntity) target).getAttackTarget() != null || target.getHealth() < target.getMaxHealth();
            }
            return false;
        }) {
            @Override
            public boolean shouldContinueExecuting() {
                LivingEntity livingentity = this.goalOwner.getAttackTarget();
                if (livingentity == null) {
                    livingentity = this.target;
                }
                boolean targetHasTarget = false;
                if (livingentity instanceof MobEntity) targetHasTarget = ((MobEntity)livingentity).getAttackTarget() != null;
                return super.shouldContinueExecuting() && (livingentity.getHealth() < livingentity.getMaxHealth() || targetHasTarget) && canHeal(target);
            }

            @Override
            protected double getTargetDistance() {
                return super.getTargetDistance() * 2;
            }
        });
    }

    @Override
    public void tick() {
        super.tick();

//        if (getAnimation() == NO_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(this, HEAL_START_ANIMATION);
    }

    @Override
    protected void updateAttackAI() {

    }

    @Override
    public boolean canHeal(LivingEntity entity) {
        return entity instanceof EntityBarako;
    }

    public class TeleportToSafeSpotGoal extends Goal {
        private final EntityBarakoaSunblocker entity;

        public TeleportToSafeSpotGoal(EntityBarakoaSunblocker entityIn) {
            this.entity = entityIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
        }
    }

    public class HealTargetGoal extends Goal {
        private final EntityBarakoaSunblocker entity;

        public HealTargetGoal(EntityBarakoaSunblocker entityIn) {
            this.entity = entityIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldContinueExecuting() {
            return entity.canHeal(entity.getAttackTarget());
        }

        @Override
        public boolean shouldExecute() {
            return entity.canHeal(entity.getAttackTarget());
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
            AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityBarakoa.HEAL_START_ANIMATION);
        }
    }
}
