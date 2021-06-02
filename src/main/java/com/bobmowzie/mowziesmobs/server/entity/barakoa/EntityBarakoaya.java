package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.ai.AvoidProjectilesGoal;
import com.bobmowzie.mowziesmobs.server.item.BarakoaMask;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import java.util.EnumSet;

public class EntityBarakoaya extends EntityBarakoaVillager {
    public boolean hasTriedOrSucceededTeleport = true;
    private int teleportAttempts = 0;

    public EntityBarakoaya(EntityType<? extends EntityBarakoaVillager> type, World world) {
        super(type, world);
        setWeapon(3);
        setMask(MaskType.FAITH);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new HealTargetGoal(this));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, PlayerEntity.class, 50.0F, 0.8D, 0.6D, target -> {
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
        this.goalSelector.addGoal(1, new TeleportToSafeSpotGoal(this));
        this.goalSelector.addGoal(1, new AvoidProjectilesGoal(this, ProjectileEntity.class, target -> {
            return getAnimation() == HEAL_LOOP_ANIMATION || getAnimation() == HEAL_START_ANIMATION;
        }, 3.0F, 0.8D, 0.6D));
    }

    @Override
    protected void registerTargetGoals() {
        super.registerTargetGoals();
        this.targetSelector.addGoal(2, new NearestAttackableTargetPredicateGoal<EntityBarako>(this, EntityBarako.class, 0, false, false, (new EntityPredicate()).setDistance(getAttributeValue(Attributes.FOLLOW_RANGE) * 2).setCustomPredicate(target -> {
            if (!active) return false;
            if (target instanceof MobEntity) {
                return ((MobEntity) target).getAttackTarget() != null || target.getHealth() < target.getMaxHealth();
            }
            return false;
        }).allowFriendlyFire().allowInvulnerable().setSkipAttackChecks().setIgnoresLineOfSight()) {
            @Override
            public boolean shouldContinueExecuting() {
                LivingEntity livingentity = this.goalOwner.getAttackTarget();
                if (livingentity == null) {
                    livingentity = this.target;
                }
                boolean targetHasTarget = false;
                if (livingentity instanceof MobEntity) targetHasTarget = ((MobEntity)livingentity).getAttackTarget() != null;
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
                targetEntitySelector.setIgnoresLineOfSight().allowInvulnerable().allowFriendlyFire().setSkipAttackChecks();
                super.startExecuting();
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (active && teleportAttempts > 3 && (getAttackTarget() == null || !getAttackTarget().isAlive())) hasTriedOrSucceededTeleport = true;

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
            if (entity.getAttackTarget() != null && entity.canHeal(entity.getAttackTarget()) && (
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
                i = MathHelper.floor(entity.getHomePosition().getX());
                j = MathHelper.floor(entity.getHomePosition().getY());
                k = MathHelper.floor(entity.getHomePosition().getZ());
            }
            else if (entity.getAttackTarget() != null) {
                i = MathHelper.floor(entity.getAttackTarget().getPosX());
                j = MathHelper.floor(entity.getAttackTarget().getPosY());
                k = MathHelper.floor(entity.getAttackTarget().getPosZ());
            }
            else {
                i = MathHelper.floor(entity.getPosX());
                j = MathHelper.floor(entity.getPosY());
                k = MathHelper.floor(entity.getPosZ());
            }
            boolean foundPosition = false;
            for(int l = 0; l < 50; ++l) {
                double radius = Math.pow(rand.nextFloat(), 1.35) * 25;
                double angle = rand.nextFloat() * Math.PI * 2;
                int i1 = i + (int)(Math.cos(angle) * radius);
                int j1 = j + MathHelper.nextInt(entity.rand, 0, 15) * MathHelper.nextInt(entity.rand, -1, 1);
                int k1 = k + (int)(Math.sin(angle) * radius);
                BlockPos blockpos = new BlockPos(i1, j1, k1);
                Vector3d newPos = new Vector3d(i1, j1, k1);
                Vector3d offset = newPos.subtract(entity.getPositionVec());
                AxisAlignedBB newBB = entity.getBoundingBox().offset(offset);
                if (testBlock(blockpos, newBB) && entity.world.getEntitiesWithinAABB(EntityBarako.class, newBB.grow(7)).isEmpty()) {
                    entity.teleportDestination = newPos.add(0, 1, 0);
                    if (entity.teleportAttempts >= 3) foundPosition = true;
                    if (entity.world.getEntitiesWithinAABB(EntityBarakoaya.class, newBB.grow(5)).isEmpty()) {
                        if (entity.teleportAttempts >= 2) foundPosition = true;
                        if (!entity.world.isPlayerWithin(i1, j1, k1, 5) && !entity.world.containsAnyLiquid(newBB)) {
                            if (entity.teleportAttempts >= 1) foundPosition = true;
                            LivingEntity target = getAttackTarget();
                            if (target instanceof MobEntity && ((MobEntity) target).getAttackTarget() != null) {
                                if (!canEntityBeSeenFromLocation(((MobEntity) target).getAttackTarget(), newPos)) {
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

        public boolean canEntityBeSeenFromLocation(Entity entityIn, Vector3d location) {
            Vector3d vector3d = new Vector3d(location.getX(), location.getY() + entity.getEyeHeight(), location.getZ());
            Vector3d vector3d1 = new Vector3d(entityIn.getPosX(), entityIn.getPosYEye(), entityIn.getPosZ());
            return entity.world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity)).getType() != RayTraceResult.Type.BLOCK;
        }

        public boolean testBlock(BlockPos blockpos, AxisAlignedBB aabb) {
            World world = entity.world;
            if (world.isBlockLoaded(blockpos)) {
                BlockPos blockpos1 = blockpos.down();
                BlockState blockstate = world.getBlockState(blockpos1);
                return blockstate.getMaterial().isSolid() && blockstate.getMaterial().blocksMovement() && world.hasNoCollisions(aabb);
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
            return entity.canHeal(entity.getAttackTarget());
        }

        @Override
        public boolean shouldExecute() {
            if (!entity.active) return false;
            return entity.canHeal(entity.getAttackTarget());
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
        LivingEntity target = getAttackTarget();
        if (target != null) {
            EffectHandler.addOrCombineEffect(target, EffectHandler.SUNBLOCK, 20, 0, true, false);
            if (target.ticksExisted % 20 == 0) target.heal(0.15f);
        }
    }
    
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        boolean teleporting = getAnimation() == TELEPORT_ANIMATION && getAnimationTick() <= 16;
        return super.isInvulnerableTo(source) || ((!active || teleporting || !hasTriedOrSucceededTeleport) && source != DamageSource.OUT_OF_WORLD && !source.isCreativePlayer());
    }

    @Override
    public void setFire(int seconds) {
        boolean teleporting = getAnimation() == TELEPORT_ANIMATION && getAnimationTick() <= 16;
        if (!active || teleporting || !hasTriedOrSucceededTeleport) return;
        super.setFire(seconds);
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData livingData, CompoundNBT compound) {
        setMask(MaskType.FAITH);
        setWeapon(3);
        return super.onInitialSpawn(world, difficulty, reason, livingData, compound);
    }
}
