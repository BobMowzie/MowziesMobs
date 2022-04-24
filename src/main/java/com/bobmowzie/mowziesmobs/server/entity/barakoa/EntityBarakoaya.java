package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.ai.AvoidProjectilesGoal;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.*;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerLevel;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class EntityBarakoaya extends EntityBarakoaVillager {
    public boolean hasTriedOrSucceededTeleport = true;
    private int teleportAttempts = 0;

    public EntityBarakoaya(EntityType<? extends EntityBarakoaVillager> type, Level world) {
        super(type, world);
        setWeapon(3);
        setMask(MaskType.FAITH);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new HealTargetGoal(this));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<Player>(this, Player.class, 50.0F, 0.8D, 0.6D, target -> {
            if (target instanceof Player) {
                if (this.world.getDifficulty() == Difficulty.PEACEFUL) return false;
                if (getTarget() == target) return true;
                if (getTarget() instanceof EntityBarako) return false;
                if (getAnimation() != NO_ANIMATION) return false;
                ItemStack headArmorStack = ((Player) target).inventory.armorInventory.get(3);
                return !(headArmorStack.getItem() instanceof BarakoaMask) || target == getMisbehavedPlayer();
            }
            return true;
        }){
            @Override
            public void resetTask() {
                super.resetTask();
                setMisbehavedPlayerId(null);
            }
        });
        this.goalSelector.addGoal(1, new TeleportToSafeSpotGoal(this));
        this.goalSelector.addGoal(1, new AvoidProjectilesGoal(this, Projectile.class, target -> {
            return getAnimation() == HEAL_LOOP_ANIMATION || getAnimation() == HEAL_START_ANIMATION;
        }, 3.0F, 0.8D, 0.6D));
    }

    @Override
    protected void registerTargetGoals() {
        super.registerTargetGoals();
        this.targetSelector.addGoal(2, new NearestAttackableTargetPredicateGoal<EntityBarako>(this, EntityBarako.class, 0, false, false, (new EntityPredicate()).setDistance(getAttributeValue(Attributes.FOLLOW_RANGE) * 2).setCustomPredicate(target -> {
            if (!active) return false;
            if (target instanceof MobEntity) {
                return ((MobEntity) target).getTarget() != null || target.getHealth() < target.getMaxHealth();
            }
            return false;
        }).allowFriendlyFire().allowInvulnerable().setSkipAttackChecks().setIgnoresLineOfSight().setUseInvisibilityCheck()) {
            @Override
            public boolean shouldContinueExecuting() {
                LivingEntity livingentity = this.goalOwner.getTarget();
                if (livingentity == null) {
                    livingentity = this.target;
                }
                boolean targetHasTarget = false;
                if (livingentity instanceof MobEntity) targetHasTarget = ((MobEntity)livingentity).getTarget() != null;
                boolean canHeal = true;
                if (this.goalOwner instanceof EntityBarakoa) canHeal = ((EntityBarakoa)this.goalOwner).canHeal(livingentity);
                return super.shouldContinueExecuting() && (livingentity.getHealth() < livingentity.getMaxHealth() || targetHasTarget) && canHeal;
            }

            @Override
            protected double getTargetDistance() {
                return super.getTargetDistance() * 2;
            }

            @Override
            public void startExecuting() {
                targetEntitySelector.setIgnoresLineOfSight().allowInvulnerable().allowFriendlyFire().setSkipAttackChecks().setUseInvisibilityCheck();
                super.startExecuting();
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (active && teleportAttempts > 3 && (getTarget() == null || !getTarget().isAlive())) hasTriedOrSucceededTeleport = true;
        if (getAnimation() == HEAL_LOOP_ANIMATION && !canHeal(getTarget())) AnimationHandler.INSTANCE.sendAnimationMessage(this, HEAL_STOP_ANIMATION);

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
        private final EntityBarakoaya entity;

        public TeleportToSafeSpotGoal(EntityBarakoaya entityIn) {
            this.entity = entityIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            if (!entity.active) return false;
            if (entity.getAnimation() == TELEPORT_ANIMATION) return false;
            if (entity.getTarget() != null && entity.canHeal(entity.getTarget()) && (
                    (entity.targetDistance >= 0 && entity.targetDistance < 7) || !hasTriedOrSucceededTeleport
            )) {
                return findTeleportLocation();
            }
            return false;
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
            hasTriedOrSucceededTeleport = true;
            AnimationHandler.INSTANCE.sendAnimationMessage(entity, TELEPORT_ANIMATION);
        }

        private boolean findTeleportLocation() {
            int i;
            int j;
            int k;
            if (entity.getMaximumHomeDistance() > -1) {
                i = Mth.floor(entity.getHomePosition().x());
                j = Mth.floor(entity.getHomePosition().y());
                k = Mth.floor(entity.getHomePosition().z());
            }
            else if (entity.getTarget() != null) {
                i = Mth.floor(entity.getTarget().getX());
                j = Mth.floor(entity.getTarget().getY());
                k = Mth.floor(entity.getTarget().getZ());
            }
            else {
                i = Mth.floor(entity.getX());
                j = Mth.floor(entity.getY());
                k = Mth.floor(entity.getZ());
            }
            boolean foundPosition = false;
            for(int l = 0; l < 50; ++l) {
                double radius = Math.pow(random.nextFloat(), 1.35) * 25;
                double angle = random.nextFloat() * Math.PI * 2;
                int i1 = i + (int)(Math.cos(angle) * radius);
                int j1 = j + Mth.nextInt(entity.rand, 0, 15) * Mth.nextInt(entity.rand, -1, 1);
                int k1 = k + (int)(Math.sin(angle) * radius);
                BlockPos blockpos = new BlockPos(i1, j1, k1);
                Vec3 newPos = new Vec3(i1, j1, k1);
                Vec3 offset = newPos.subtract(entity.position());
                AxisAlignedBB newBB = entity.getBoundingBox().offset(offset);
                if (testBlock(blockpos, newBB) && entity.world.getEntitiesWithinAABB(EntityBarako.class, newBB.grow(7)).isEmpty()) {
                    entity.teleportDestination = newPos.add(0, 0, 0);
                    if (entity.teleportAttempts >= 3) foundPosition = true;
                    if (entity.world.getEntitiesWithinAABB(EntityBarakoaya.class, newBB.grow(5)).isEmpty()) {
                        if (entity.teleportAttempts >= 2) foundPosition = true;
                        if (!entity.world.isPlayerWithin(i1, j1, k1, 5) && !entity.world.containsAnyLiquid(newBB)) {
                            if (entity.teleportAttempts >= 1) foundPosition = true;
                            LivingEntity target = getTarget();
                            if (target instanceof MobEntity && ((MobEntity) target).getTarget() != null) {
                                if (!canEntityBeSeenFromLocation(((MobEntity) target).getTarget(), newPos)) {
                                    return true;
                                }
                            } else return true;
                        }
                    }
                }
            }
            entity.teleportAttempts++;
            if (entity.teleportAttempts > 3) hasTriedOrSucceededTeleport = true;
            return foundPosition;
        }

        public boolean canEntityBeSeenFromLocation(Entity entityIn, Vec3 location) {
            Vec3 vector3d = new Vec3(location.x(), location.y() + entity.getEyeHeight(), location.z());
            Vec3 vector3d1 = new Vec3(entityIn.getX(), entityIn.getPosYEye(), entityIn.getZ());
            return entity.world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity)).getType() != HitResult.Type.BLOCK;
        }

        public boolean testBlock(BlockPos blockpos, AxisAlignedBB aabb) {
            Level world = entity.world;
            if (world.isBlockLoaded(blockpos)) {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = level.getBlockState(blockpos1);
                return blockstate.getMaterial().isSolid() && blockstate.getMaterial().blocksMotion() && world.noCollision(aabb);
            }
            return false;
        }
    }

    public static class HealTargetGoal extends Goal {
        private final EntityBarakoa entity;

        public HealTargetGoal(EntityBarakoa entityIn) {
            this.entity = entityIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean shouldContinueExecuting() {
            return entity.canHeal(entity.getTarget());
        }

        @Override
        public boolean shouldExecute() {
            if (!entity.active) return false;
            return entity.canHeal(entity.getTarget());
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
            AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityBarakoa.HEAL_START_ANIMATION);
        }

        @Override
        public void resetTask() {
            super.resetTask();
//            if (entity.getAnimation() == HEAL_LOOP_ANIMATION || entity.getAnimation() == HEAL_START_ANIMATION) AnimationHandler.INSTANCE.sendAnimationMessage(entity, EntityBarakoa.HEAL_STOP_ANIMATION);
        }
    }

    @Override
    protected void sunBlockTarget() {
        LivingEntity target = getTarget();
        if (target != null) {
            EffectHandler.addOrCombineEffect(target, EffectHandler.SUNBLOCK, 20, 0, true, false);
            if (target.tickCount % 20 == 0) target.heal(0.15f);
        }
    }
    
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        boolean teleporting = getAnimation() == TELEPORT_ANIMATION && getAnimationTick() <= 16;
        return super.isInvulnerableTo(source) || ((!active || teleporting || !hasTriedOrSucceededTeleport) && source != DamageSource.OUT_OF_WORLD);
    }

    @Override
    public void setFire(int seconds) {
        boolean teleporting = getAnimation() == TELEPORT_ANIMATION && getAnimationTick() <= 16;
        if (!active || teleporting || !hasTriedOrSucceededTeleport) return;
        super.setFire(seconds);
    }

    @Override
    public SpawnGroupData finalizeSpawn(IServerLevel world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData livingData, CompoundTag compound) {
        setMask(MaskType.FAITH);
        setWeapon(3);
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }
}
