package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class EntityBarakoayaToPlayer extends EntityBarakoanToPlayer {

    public EntityBarakoayaToPlayer(EntityType<? extends EntityBarakoayaToPlayer> type, Level world) {
        this(type, world, null);
    }

    public EntityBarakoayaToPlayer(EntityType<? extends EntityBarakoayaToPlayer> type, Level world, Player leader) {
        super(type, world, leader);
        setMask(MaskType.FAITH);
        setWeapon(3);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new EntityBarakoaya.HealTargetGoal(this));
    }

    @Override
    protected void registerTargetGoals() {
        super.registerTargetGoals();
        this.targetSelector.addGoal(2, new NearestAttackableTargetPredicateGoal<Player>(this, Player.class, 0, true, true, TargetingConditions.forNonCombat().range(getAttributeValue(Attributes.FOLLOW_RANGE)).selector(target -> {
            if (!active) return false;
            if (target != getLeader()) return false;
            return healAICheckTarget(target);
        }).ignoreInvisibilityTesting()) {
            @Override
            public boolean canContinueToUse() {
                LivingEntity livingentity = this.mob.getTarget();
                if (livingentity == null) {
                    livingentity = this.targetMob;
                }
                return super.canContinueToUse() && this.mob instanceof EntityBarakoayaToPlayer && ((EntityBarakoayaToPlayer)this.mob).healAICheckTarget(livingentity);
            }
        });
    }

    private boolean healAICheckTarget(LivingEntity livingentity) {
        if (livingentity != getLeader()) return false;
        boolean targetHasTarget = livingentity.getLastHurtMob() != null && (livingentity.tickCount - livingentity.getLastHurtMobTimestamp() < 120 || livingentity.distanceToSqr(livingentity.getLastHurtMob()) < 256);
        if (livingentity.getLastHurtMob() instanceof EntityBarakoanToPlayer) targetHasTarget = false;
        boolean canHeal = this.canHeal(livingentity);
        boolean survivalMode = !livingentity.isSpectator() && !((Player)livingentity).isCreative();
        return (livingentity.getHealth() < livingentity.getMaxHealth() || targetHasTarget) && canHeal && survivalMode;
    }

    public boolean canHeal(LivingEntity entity) {
        return entity == leader && entity != null && distanceToSqr(entity) < 256.0;
    }

    @Override
    protected void sunBlockTarget() {
        LivingEntity target = getTarget();
        if (target != null && target == getLeader()) {
            EffectHandler.addOrCombineEffect(target, EffectHandler.SUNBLOCK, 20, 0, true, false);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData livingData, CompoundTag compound) {
        setMask(MaskType.FAITH);
        setWeapon(3);
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }
}
