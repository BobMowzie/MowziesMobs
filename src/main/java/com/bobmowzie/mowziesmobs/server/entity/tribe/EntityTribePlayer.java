package com.bobmowzie.mowziesmobs.server.entity.tribe;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.ai.BarakoaAttackTargetAI;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.google.common.base.Optional;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
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

import java.util.List;
import java.util.UUID;

public class EntityTribePlayer extends EntityTribesman {
    public static final Animation BLOCK_ANIMATION = Animation.create(10);

    private static final Optional<UUID> ABSENT_LEADER = Optional.absent();

    private static final DataParameter<Optional<UUID>> LEADER = EntityDataManager.createKey(EntityTribePlayer.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    public int index;
    private EntityPlayer leader;

    public EntityTribePlayer(World world) {
        super(world);
        experienceValue = 0;
        this.leader = null;
        if (getMask() == 1) this.setSize(0.7f, 2f);
    }

    public EntityTribePlayer(World world, EntityPlayer leader) {
        super(world);
        experienceValue = 0;
        this.leader = leader;
        if (getMask() == 1) this.setSize(0.7f, 2f);
    }

    @Override
    public int getAttack() {
        if (getMask() == 1) return 6;
        else return super.getAttack();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(LEADER, ABSENT_LEADER);
    }

    @Override
    public void onUpdate() {
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(leader, MowziePlayerProperties.class);

        super.onUpdate();
        if (!worldObj.isRemote) {
            if (leader == null && getLeaderUUID().isPresent()) {
                leader = getLeader();
                if (leader != null) {
                    property.addPackMember(this);
                }
            }
        }
        if (!worldObj.isRemote && getAttackTarget() != null && getAttackTarget().isDead) setAttackTarget(null);
    }

    @Override
    protected void updateCircling() {
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(leader, MowziePlayerProperties.class);
        if (leader != null) {
            if (!attacking && targetDistance < 5) {
                this.circleEntity(getAttackTarget(), 7, 0.3f, true, property.tribeCircleTick, (float) ((index + 1) * (Math.PI * 2) / (property.getPackSize() + 1)), 1.75f);
            } else {
                this.circleEntity(getAttackTarget(), 7, 0.3f, true, property.tribeCircleTick, (float) ((index + 1) * (Math.PI * 2) / (property.getPackSize() + 1)), 1);
            }
        } else {
            super.updateCircling();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        return super.attackEntityFrom(source, damage);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        MowziePlayerProperties property = EntityPropertiesHandler.INSTANCE.getProperties(leader, MowziePlayerProperties.class);
        if (leader != null) {
            property.removePackMember(this);
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

    public EntityPlayer getLeader() {
        try {
            Optional<UUID> uuid = getLeaderUUID();
            if (uuid.isPresent()) {
                List<EntityPlayer> potentialLeaders = worldObj.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().expand(32, 32, 32));
                for (EntityPlayer player : potentialLeaders) {
                    if (uuid.get().equals(player.getUniqueID())) {
                        return player;
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
