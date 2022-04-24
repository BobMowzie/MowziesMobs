package com.bobmowzie.mowziesmobs.server.entity.effects;

import net.minecraft.world.level.block.material.PushReaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by BobMowzie on 9/2/2018.
 */
public abstract class EntityMagicEffect extends Entity {
    public LivingEntity caster;
    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(EntityMagicEffect.class, EntityDataSerializers.INT);

    public EntityMagicEffect(EntityType<? extends EntityMagicEffect> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(CASTER, -1);
    }

    public int getCasterID() {
        return getEntityData().get(CASTER);
    }

    public void setCasterID(int id) {
        getEntityData().set(CASTER, id);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount == 1) {
            caster = (LivingEntity) world.getEntityByID(getCasterID());
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    public List<LivingEntity> getEntityLivingBaseNearby(double radius) {
        return getEntitiesNearby(LivingEntity.class, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(r, r, r), e -> e != this && getDistance(e) <= r + e.getBbWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearbyCube(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(r, r, r), e -> e != this);
    }
}
