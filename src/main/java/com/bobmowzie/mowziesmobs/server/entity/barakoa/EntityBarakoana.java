package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaHurtByTargetAI;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.merchant.villager.VillagerEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityBarakoana extends EntityBarakoa implements LeaderSunstrikeImmune, Enemy {
    private final List<EntityBarakoanToBarakoana> pack = new ArrayList<>();

    private final int packRadius = 3;

    public EntityBarakoana(EntityType<? extends EntityBarakoana> type, Level world) {
        super(type, world);
        this.setMask(MaskType.FURY);
        this.xpReward = 8;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new BarakoaHurtByTargetAI(this, true));
    }

    @Override
    protected void registerTargetGoals() {
        registerHuntingTargetGoals();
    }

    @Override
    protected boolean canHoldVaryingWeapons() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 6 * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.attackMultiplier.get())
                .add(Attributes.MAX_HEALTH, 10 * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.healthMultiplier.get());
    }

    @Override
    public void tick() {
        super.tick();

        for (int i = 0; i < pack.size(); i++) {
            pack.get(i).index = i;
        }

        if (!level.isClientSide && pack != null) {
            float theta = (2 * (float) Math.PI / pack.size());
            for (int i = 0; i < pack.size(); i++) {
                EntityBarakoanToBarakoana hunter = pack.get(i);
                if (hunter.getTarget() == null) {
                    hunter.getNavigation().moveTo(getX() + packRadius * Mth.cos(theta * i), getY(), getZ() + packRadius * Mth.sin(theta * i), 0.45);
                    if (getDistance(hunter) > 20 && onGround) {
                        hunter.setPos(getX() + packRadius * Mth.cos(theta * i), getY(), getZ() + packRadius * Mth.sin(theta * i));
                    }
                }
            }
        }

        if (!this.level.isClientSide && this.world.getDifficulty() == Difficulty.PEACEFUL)
        {
            this.remove();
        }
    }

    @Override
    public void remove() {
        if (tickCount == 0) {
            pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
        }
        super.remove();
    }

    @Override
    public boolean isNotColliding(WorldGenLevelReader worldReader) {
        if (tickCount == 0) {
            return !worldReader.containsAnyLiquid(this.getBoundingBox()) && worldReader.noCollision(this);
        }
        else {
            return !worldReader.containsAnyLiquid(this.getBoundingBox()) && worldReader.noCollision(this) && this.world.isUnobstructed(this);
        }
    }

    public void removePackMember(EntityBarakoanToBarakoana tribeHunter) {
        pack.remove(tribeHunter);
        sortPackMembers();
    }

    public void addPackMember(EntityBarakoanToBarakoana tribeHunter) {
        pack.add(tribeHunter);
        sortPackMembers();
    }

    private void sortPackMembers() {
        double theta = 2 * Math.PI / pack.size();
        for (int i = 0; i < pack.size(); i++) {
            int nearestIndex = -1;
            double smallestDiffSq = Double.MAX_VALUE;
            double targetTheta = theta * i;
            double x = getX() + packRadius * Math.cos(targetTheta);
            double z = getZ() + packRadius * Math.sin(targetTheta);
            for (int n = 0; n < pack.size(); n++) {
                EntityBarakoanToBarakoana tribeHunter = pack.get(n);
                double diffSq = (x - tribeHunter.getX()) * (x - tribeHunter.getX()) + (z - tribeHunter.getZ()) * (z - tribeHunter.getZ());
                if (diffSq < smallestDiffSq) {
                    smallestDiffSq = diffSq;
                    nearestIndex = n;
                }
            }
            if (nearestIndex == -1) {
                throw new ArithmeticException("All pack members have NaN x and z?");
            }
            pack.add(i, pack.remove(nearestIndex));
        }
    }

    public int getPackSize() {
        return pack.size();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(IServerLevel world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag compound) {
        int size = random.nextInt(2) + 3;
        float theta = (2 * (float) Math.PI / size);
        for (int i = 0; i <= size; i++) {
            EntityBarakoanToBarakoana tribeHunter = new EntityBarakoanToBarakoana(EntityHandler.BARAKOAN_TO_BARAKOANA, this.world, this);
            tribeHunter.setPos(getX() + 0.1 * Mth.cos(theta * i), getY(), getZ() + 0.1 * Mth.sin(theta * i));
            int weapon = random.nextInt(3) == 0 ? 1 : 0;
            tribeHunter.setWeapon(weapon);
            level.addFreshEntity(tribeHunter);
        }
        return super.finalizeSpawn(world, difficulty, reason, livingData, compound);
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        pack.forEach(EntityBarakoanToBarakoana::removeLeader);
    }

    @Override
    public void remove(boolean keepData) {
        super.remove(keepData);
        pack.forEach(EntityBarakoanToBarakoana::removeLeader);
    }

    @Override
    protected ConfigHandler.SpawnConfig getSpawnConfig() {
        return ConfigHandler.COMMON.MOBS.BARAKOA.spawnConfig;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, MobSpawnType reason) {
        List<LivingEntity> nearby = getEntityLivingBaseNearby(30, 10, 30, 30);
        for (LivingEntity nearbyEntity : nearby) {
            if (nearbyEntity instanceof EntityBarakoana || nearbyEntity instanceof VillagerEntity || nearbyEntity instanceof EntityBarako || nearbyEntity instanceof Animal) {
                return false;
            }
        }
        return super.checkSpawnRules(world, reason) && world.getDifficulty() != Difficulty.PEACEFUL;
    }

    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    @Override
    public void checkDespawn() {
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDespawnPeaceful()) {
            this.remove();
        } else if (!this.isNoDespawnRequired() && !this.requiresCustomPersistence()) {
            Entity entity = this.world.getClosestPlayer(this, -1.0D);
            net.minecraftforge.eventbus.api.Event.Result result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this);
            if (result == net.minecraftforge.eventbus.api.Event.Result.DENY) {
                idleTime = 0;
                entity = null;
            } else if (result == net.minecraftforge.eventbus.api.Event.Result.ALLOW) {
                this.remove();
                entity = null;
            }
            if (entity != null) {
                double d0 = entity.getDistanceSq(this);
                if (d0 > 16384.0D && this.canDespawn(d0)) {
                    pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
                    this.remove();
                }

                if (this.idleTime > 600 && this.random.nextInt(800) == 0 && d0 > 1024.0D && this.canDespawn(d0)) {
                    pack.forEach(EntityBarakoanToBarakoana::setShouldSetDead);
                    this.remove();
                } else if (d0 < 1024.0D) {
                    this.idleTime = 0;
                }
            }

        } else {
            this.idleTime = 0;
        }
    }
}