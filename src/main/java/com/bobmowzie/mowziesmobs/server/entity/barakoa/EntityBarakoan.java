package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import java.util.List;
import java.util.UUID;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.google.common.base.Optional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityBarakoan<L extends EntityLivingBase> extends EntityBarakoa {
    protected static final Optional<UUID> ABSENT_LEADER = Optional.absent();

    private static final DataParameter<Optional<UUID>> LEADER = EntityDataManager.createKey(EntityBarakoanToBarakoana.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    private final Class<L> leaderClass;

    public int index;

    protected L leader;

    public EntityBarakoan(World world, Class<L> leaderClass) {
        this(world, leaderClass, null);
    }

    public EntityBarakoan(World world, Class<L> leaderClass, L leader) {
        super(world);
        this.leaderClass = leaderClass;
        this.leader = leader;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
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
    protected String getPickedEntityId() {
        return MowziesMobs.MODID + "." + EntityHandler.BARAKOAYA_ID;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!worldObj.isRemote) {
            if (leader == null && getLeaderUUID().isPresent()) {
                leader = getLeader();
                if (leader != null) {
                    addAsPackMember();
                }
            }
        }
    }

    @Override
    protected void updateCircling() {
        if (leader != null) {
            if (!attacking && targetDistance < 5) {
                this.circleEntity(getAttackTarget(), 7, 0.3f, true, getTribeCircleTick(), (float) ((index + 1) * (Math.PI * 2) / (getPackSize() + 1)), 1.75f);
            } else {
                this.circleEntity(getAttackTarget(), 7, 0.3f, true, getTribeCircleTick(), (float) ((index + 1) * (Math.PI * 2) / (getPackSize() + 1)), 1);
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

    public L getLeader() {
        Optional<UUID> uuid = getLeaderUUID();
        if (uuid.isPresent()) {
            List<L> potentialLeaders = worldObj.getEntitiesWithinAABB(leaderClass, getEntityBoundingBox().expand(32, 32, 32));
            for (L entity : potentialLeaders) {
                if (uuid.get().equals(entity.getUniqueID())) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    protected boolean canDespawn() {
        return leader == null;
    }

    protected abstract int getTribeCircleTick();

    protected abstract int getPackSize();

    protected abstract void addAsPackMember();

    protected abstract void removeAsPackMember();

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        Optional<UUID> leader = getLeaderUUID();
        if (leader.isPresent()) {
            compound.setString("leaderUUID", leader.get().toString());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        String uuid = compound.getString("leaderUUID");
        if (uuid.isEmpty()) {
            setLeaderUUID(ABSENT_LEADER);
        } else {
            setLeaderUUID(UUID.fromString(uuid));
        }
    }
}
