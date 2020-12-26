package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class EntityBarakoan<L extends LivingEntity> extends EntityBarakoa {
    protected static final Optional<UUID> ABSENT_LEADER = Optional.empty();

    private static final DataParameter<Optional<UUID>> LEADER = EntityDataManager.createKey(EntityBarakoan.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    private final Class<L> leaderClass;

    public int index;

    protected L leader;

    public boolean shouldSetDead;

    public EntityBarakoan(EntityType<? extends EntityBarakoan> type, World world, Class<L> leaderClass) {
        this(type, world, leaderClass, null);
    }

    public EntityBarakoan(EntityType<? extends EntityBarakoan> type, World world, Class<L> leaderClass, L leader) {
        super(type, world);
        this.leaderClass = leaderClass;
        if (leader != null) {
            setLeaderUUID(leader.getUniqueID());
        }
        shouldSetDead = false;
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(LEADER, ABSENT_LEADER);
    }

    public Optional<UUID> getLeaderUUID() {
        return getDataManager().get(LEADER);
    }

    public void setLeaderUUID(UUID uuid) {
        setLeaderUUID(Optional.of(uuid));
    }

    public void setLeaderUUID(Optional<UUID> uuid) {
        getDataManager().set(LEADER, uuid);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(ItemHandler.BARAKOA_SPAWN_EGG);
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
        if (shouldSetDead) remove();
    }

    @Override
    protected void updateCircling() {
        LivingEntity target = getAttackTarget();
        if (leader != null && target != null) {
            if (!attacking && targetDistance < 5) {
                this.circleEntity(target, 7, 0.3f, true, getTribeCircleTick(), (float) ((index + 1) * (Math.PI * 2) / (getPackSize() + 1)), 1.75f);
            } else {
                this.circleEntity(target, 7, 0.3f, true, getTribeCircleTick(), (float) ((index + 1) * (Math.PI * 2) / (getPackSize() + 1)), 1);
            }
        } else {
            super.updateCircling();
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (leader != null) {
            removeAsPackMember();
        }
    }

    public void setShouldSetDead() {
        shouldSetDead = true;
    }

    @Override
    public void remove() {
        if (leader != null) {
            removeAsPackMember();
        }
        super.remove();
    }

    public L getLeader() {
        Optional<UUID> uuid = getLeaderUUID();
        if (uuid.isPresent()) {
            List<L> potentialLeaders = world.getEntitiesWithinAABB(leaderClass, getBoundingBox().grow(32, 32, 32));
            for (L entity : potentialLeaders) {
                if (uuid.get().equals(entity.getUniqueID())) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    public boolean preventDespawn() {
        return leader != null;
    }

    protected abstract int getTribeCircleTick();

    protected abstract int getPackSize();

    protected abstract void addAsPackMember();

    protected abstract void removeAsPackMember();

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        Optional<UUID> leader = getLeaderUUID();
        if (leader.isPresent()) {
            compound.putString("leaderUUID", leader.get().toString());
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        String uuid = compound.getString("leaderUUID");
        if (uuid.isEmpty()) {
            setLeaderUUID(ABSENT_LEADER);
        } else {
            setLeaderUUID(UUID.fromString(uuid));
        }
    }
}
