package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class EntityUmvuthanaFollower<L extends LivingEntity> extends EntityUmvuthana {
    protected static final Optional<UUID> ABSENT_LEADER = Optional.empty();

    private static final EntityDataAccessor<Optional<UUID>> LEADER = SynchedEntityData.defineId(EntityUmvuthanaFollower.class, EntityDataSerializers.OPTIONAL_UUID);

    private final Class<L> leaderClass;

    public int index;

    protected L leader;

    public boolean shouldSetDead;

    public EntityUmvuthanaFollower(EntityType<? extends EntityUmvuthanaFollower> type, Level world, Class<L> leaderClass) {
        this(type, world, leaderClass, null);
    }

    public EntityUmvuthanaFollower(EntityType<? extends EntityUmvuthanaFollower> type, Level world, Class<L> leaderClass, L leader) {
        super(type, world);
        this.leaderClass = leaderClass;
        if (leader != null) {
            setLeaderUUID(leader.getUUID());
        }
        shouldSetDead = false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(LEADER, ABSENT_LEADER);
    }

    public Optional<UUID> getLeaderUUID() {
        return getEntityData().get(LEADER);
    }

    public void setLeaderUUID(UUID uuid) {
        setLeaderUUID(Optional.of(uuid));
    }

    public void setLeaderUUID(Optional<UUID> uuid) {
        getEntityData().set(LEADER, uuid);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ItemHandler.UMVUTHANA_SPAWN_EGG);
    }

    @Override
    public void tick() {
        super.tick();
        if (leader == null && getLeaderUUID().isPresent()) {
            leader = getLeader();
            if (leader != null) {
                addAsPackMember();
            }
        }
        if (shouldSetDead) discard();
    }

    @Override
    protected Vec3 updateCirclingPosition(float radius, float speed) {
        LivingEntity target = getTarget();
        if (leader != null && target != null) {
            return this.circleEntityPosition(target, radius, speed, true, getGroupCircleTick(), (float) ((index + 1) * (Math.PI * 2) / (getPackSize() + 1)));
        } else {
            return super.updateCirclingPosition(radius, speed);
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (leader != null) {
            removeAsPackMember();
        }
    }

    public void setShouldSetDead() {
        shouldSetDead = true;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (leader != null) {
            removeAsPackMember();
        }
        super.remove(reason);
    }

    public L getLeader() {
        Optional<UUID> uuid = getLeaderUUID();
        if (uuid.isPresent()) {
            List<L> potentialLeaders = level.getEntitiesOfClass(leaderClass, getBoundingBox().inflate(32, 32, 32));
            for (L entity : potentialLeaders) {
                if (uuid.get().equals(entity.getUUID())) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return leader != null;
    }

    protected abstract int getGroupCircleTick();

    protected abstract int getPackSize();

    protected abstract void addAsPackMember();

    protected abstract void removeAsPackMember();

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        Optional<UUID> leader = getLeaderUUID();
        if (leader.isPresent()) {
            compound.putString("leaderUUID", leader.get().toString());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        String uuid = compound.getString("leaderUUID");
        if (uuid.isEmpty()) {
            setLeaderUUID(ABSENT_LEADER);
        } else {
            setLeaderUUID(UUID.fromString(uuid));
        }
    }
}
