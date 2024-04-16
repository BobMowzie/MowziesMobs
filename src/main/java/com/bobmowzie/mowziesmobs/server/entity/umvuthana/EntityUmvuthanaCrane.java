package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ai.AvoidProjectilesGoal;
import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.ai.UmvuthanaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.item.UmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class EntityUmvuthanaCrane extends EntityUmvuthanaMinion {
    public boolean hasTriedOrSucceededTeleport = true;
    private int teleportAttempts = 0;

    public EntityUmvuthanaCrane(EntityType<? extends EntityUmvuthanaMinion> type, Level world) {
        super(type, world);
        setWeapon(3);
        setMask(MaskType.FAITH);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new TeleportToSafeSpotGoal(this));
        this.goalSelector.addGoal(1, new AvoidProjectilesGoal(this, Projectile.class, target -> getActiveAbilityType() == HEAL_ABILITY, 5.0F, 0.8D, 0.6D));
        this.goalSelector.addGoal(4, new HealTargetGoal(this));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<Player>(this, Player.class, 7.0F, 0.8D, 0.6D, target -> {
            if (target instanceof Player) {
                if (this.level().getDifficulty() == Difficulty.PEACEFUL) return false;
                if (getTarget() == target) return true;
                if (getTarget() instanceof EntityUmvuthi) return false;
                if (getActiveAbilityType() != null) return false;
                ItemStack headArmorStack = ((Player) target).getInventory().armor.get(3);
                return !(headArmorStack.getItem() instanceof UmvuthanaMask) || target == getMisbehavedPlayer();
            }
            return true;
        }){
            @Override
            public void stop() {
                super.stop();
                setMisbehavedPlayerId(null);
            }
        });
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Zombie.class, 7.0F, 0.8D, 0.6D));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, AbstractSkeleton.class, 7.0F, 0.8D, 0.6D));
    }

    @Override
    protected void registerTargetGoals() {
        this.targetSelector.addGoal(3, new UmvuthanaHurtByTargetAI(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetPredicateGoal<EntityUmvuthi>(this, EntityUmvuthi.class, 0, false, false, TargetingConditions.forNonCombat().range(getAttributeValue(Attributes.FOLLOW_RANGE) * 2).selector(target -> {
            if (!active) return false;
            if (target instanceof Mob) {
                return ((Mob) target).getTarget() != null || target.getHealth() < target.getMaxHealth();
            }
            return false;
        }).ignoreLineOfSight().ignoreInvisibilityTesting()) {
            @Override
            public boolean canContinueToUse() {
                LivingEntity livingentity = this.mob.getTarget();
                if (livingentity == null) {
                    livingentity = this.targetMob;
                }
                boolean targetHasTarget = false;
                if (livingentity instanceof Mob) targetHasTarget = ((Mob)livingentity).getTarget() != null;
                boolean canHeal = true;
                if (this.mob instanceof EntityUmvuthana) canHeal = ((EntityUmvuthana)this.mob).canHeal(livingentity);
                return super.canContinueToUse() && (livingentity.getHealth() < livingentity.getMaxHealth() || targetHasTarget) && canHeal;
            }

            @Override
            protected double getFollowDistance() {
                return super.getFollowDistance() * 2;
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (active && teleportAttempts > 3 && (getTarget() == null || !getTarget().isAlive())) hasTriedOrSucceededTeleport = true;
//        if (getActiveAbilityType() == HEAL_ABILITY && !canHeal(getTarget())) AbilityHandler.INSTANCE.sendInterruptAbilityMessage(this, HEAL_ABILITY);

//        if (getActiveAbility() == null) AbilityHandler.INSTANCE.sendAbilityMessage(this, HEAL_ABILITY);
    }

    @Override
    public boolean canHeal(LivingEntity entity) {
        return entity instanceof EntityUmvuthi;
    }

    @Override
    public void die(DamageSource cause) {
        // If healing Umvuthi, set the attack target of any mob targeting the crane to Umvuthi
        if (this.getTarget() instanceof EntityUmvuthi) {
            List<Mob> targetingMobs = level().getEntitiesOfClass(Mob.class, getBoundingBox().inflate(30), (e) -> e.getTarget() == this);
            if (cause.getEntity() instanceof Mob) {
                Mob damagingMob = (Mob) cause.getEntity();
                if (damagingMob.getTarget() == this && !targetingMobs.contains(damagingMob)) {
                    targetingMobs.add(damagingMob);
                }
            }
            for (Mob mob : targetingMobs) {
                mob.setTarget(this.getTarget());
            }
        }
        super.die(cause);
    }

    public class TeleportToSafeSpotGoal extends Goal {
        private final EntityUmvuthanaCrane entity;

        public TeleportToSafeSpotGoal(EntityUmvuthanaCrane entityIn) {
            this.entity = entityIn;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!entity.active) return false;
            if (entity.getActiveAbilityType() == TELEPORT_ABILITY) return false;
            if (entity.getTarget() != null && entity.canHeal(entity.getTarget()) && (
                    (entity.targetDistance >= 0 && entity.targetDistance < 11) || !hasTriedOrSucceededTeleport
            )) {
                return findTeleportLocation();
            }
            return false;
        }

        @Override
        public void start() {
            super.start();
            hasTriedOrSucceededTeleport = true;
            AbilityHandler.INSTANCE.sendAbilityMessage(entity, TELEPORT_ABILITY);
        }

        private boolean findTeleportLocation() {
            int i;
            int j;
            int k;
            if (entity.getRestrictRadius() > -1) {
                i = Mth.floor(entity.getRestrictCenter().getX());
                j = Mth.floor(entity.getRestrictCenter().getY());
                k = Mth.floor(entity.getRestrictCenter().getZ());
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
                int j1 = j + Mth.nextInt(entity.random, 0, 15) * Mth.nextInt(entity.random, -1, 1);
                int k1 = k + (int)(Math.sin(angle) * radius);
                BlockPos blockpos = new BlockPos(i1, j1, k1);
                Vec3 newPos = new Vec3(i1, j1, k1);
                Vec3 offset = newPos.subtract(entity.position());
                AABB newBB = entity.getBoundingBox().move(offset);
                if (testBlock(blockpos, newBB) && entity.level().getEntitiesOfClass(EntityUmvuthi.class, newBB.inflate(7)).isEmpty()) {
                    entity.teleportDestination = newPos.add(0, 0, 0);
                    if (entity.teleportAttempts >= 3) foundPosition = true;
                    if (entity.level().getEntitiesOfClass(EntityUmvuthanaCrane.class, newBB.inflate(5)).isEmpty()) {
                        if (entity.teleportAttempts >= 2) foundPosition = true;
                        if (!entity.level().hasNearbyAlivePlayer(i1, j1, k1, 5) && !entity.level().containsAnyLiquid(newBB)) {
                            if (entity.teleportAttempts >= 1) foundPosition = true;
                            LivingEntity target = getTarget();
                            if (target instanceof Mob && ((Mob) target).getTarget() != null) {
                                if (!canEntityBeSeenFromLocation(((Mob) target).getTarget(), newPos)) {
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
            Vec3 vector3d1 = new Vec3(entityIn.getX(), entityIn.getEyeY(), entityIn.getZ());
            return entity.level().clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() != HitResult.Type.BLOCK;
        }

        public boolean testBlock(BlockPos blockpos, AABB aabb) {
            Level world = entity.level();
            if (world.hasChunkAt(blockpos)) {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = world.getBlockState(blockpos1);
                return blockstate.getMaterial().isSolid() && blockstate.getMaterial().blocksMotion() && world.noCollision(aabb);
            }
            return false;
        }
    }

    public static class HealTargetGoal extends Goal {
        private final EntityUmvuthana entity;

        public HealTargetGoal(EntityUmvuthana entityIn) {
            this.entity = entityIn;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canContinueToUse() {
            return entity.canHeal(entity.getTarget());
        }

        @Override
        public boolean canUse() {
            if (!entity.active) return false;
            return entity.canHeal(entity.getTarget());
        }

        @Override
        public void start() {
            super.start();
            AbilityHandler.INSTANCE.sendAbilityMessage(entity, EntityUmvuthana.HEAL_ABILITY);
        }
    }

    @Override
    protected void sunBlockTarget() {
        LivingEntity target = getTarget();
        if (target != null) {
            EffectHandler.addOrCombineEffect(target, EffectHandler.SUNBLOCK.get(), 20, 0, true, false);
            if (target.tickCount % 20 == 0) target.heal(0.15f);
        }
    }
    
    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        boolean teleporting = getActiveAbilityType() == TELEPORT_ABILITY && getAnimationTick() <= 16;
        return super.isInvulnerableTo(source) || ((!active || teleporting || !hasTriedOrSucceededTeleport) && source != DamageSource.OUT_OF_WORLD && timeUntilDeath != 0);
    }

    @Override
    public void setSecondsOnFire(int seconds) {
        boolean teleporting = getActiveAbilityType() == TELEPORT_ABILITY && getAnimationTick() <= 16;
        if (!active || teleporting || !hasTriedOrSucceededTeleport) return;
        super.setSecondsOnFire(seconds);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData livingData, CompoundTag compound) {
        setMask(MaskType.FAITH);
        setWeapon(3);
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }
}
