package com.bobmowzie.mowziesmobs.server.entity.tribe;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.google.common.base.Optional;

public class EntityTribeHunter extends EntityTribesman {
    private static final Optional<UUID> ABSENT_LEADER = Optional.absent();

    private static final DataParameter<Optional<UUID>> LEADER = EntityDataManager.createKey(EntityTribeHunter.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    public int index;
    private EntityTribeElite leader;

    public EntityTribeHunter(World world) {
        super(world);
        this.leader = null;
    }

    public EntityTribeHunter(World world, EntityTribeElite leader) {
        super(world);
        this.leader = leader;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(LEADER, ABSENT_LEADER);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!worldObj.isRemote) {
            if (leader == null && getLeaderUUID().isPresent()) {
                leader = getLeader();
                if (leader != null) {
                    leader.addPackMember(this);
                }
            }
        }
        if (leader != null) {
            setAttackTarget(leader.getAttackTarget());
        }
    }

    @Override
    protected void updateCircling() {
        if (leader != null) {
            if (!attacking && targetDistance < 5) {
                this.circleEntity(getAttackTarget(), 7, 0.3f, true, leader.circleTick, (float) ((index + 1) * (Math.PI * 2) / (leader.getPackSize() + 1)), 1.75f);
            } else {
                this.circleEntity(getAttackTarget(), 7, 0.3f, true, leader.circleTick, (float) ((index + 1) * (Math.PI * 2) / (leader.getPackSize() + 1)), 1);
            }
        } else {
            super.updateCircling();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        if (entity != null && entity instanceof EntityLivingBase) {
            if (!(entity instanceof EntityPlayer) || !(((EntityPlayer) entity).capabilities.isCreativeMode)) {
                if (leader != null) {
                    leader.setAttackTarget((EntityLivingBase) entity);
                } else {
                    this.setAttackTarget((EntityLivingBase) entity);
                }
            }
        }
        return super.attackEntityFrom(source, damage);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (leader != null) {
            leader.removePackMember(this);
        }
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

    public EntityTribeElite getLeader() {
        try {
            Optional<UUID> uuid = getLeaderUUID();
            if (uuid.isPresent()) {
                List<EntityTribeElite> potentialLeaders = worldObj.getEntitiesWithinAABB(EntityTribeElite.class, getEntityBoundingBox().expand(32, 32, 32));
                for (EntityTribeElite elite : potentialLeaders) {
                    if (uuid.get().equals(elite.getUniqueID())) {
                        return elite;
                    }
                }
            }
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void removeLeader() {
        this.setLeaderUUID(ABSENT_LEADER);
        this.leader = null;
        this.setAttackTarget(null);
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(4, new BarakoaAttackTargetAI(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityCow.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityPig.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntitySheep.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityChicken.class, 0, true, false, null));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityZombie.class, 0, true, false, null));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("mask", getMask());
        compound.setString("leaderUUID", String.valueOf(getLeaderUUID().orNull()));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setMask(compound.getInteger("mask"));
        String uuid = compound.getString("leaderUUID");
        if (uuid.isEmpty()) {
            setLeaderUUID(ABSENT_LEADER);
        } else {
            setLeaderUUID(UUID.fromString(uuid));   
        }
    }

    @Override
    protected boolean canDespawn() {
        return leader == null;
    }
}
