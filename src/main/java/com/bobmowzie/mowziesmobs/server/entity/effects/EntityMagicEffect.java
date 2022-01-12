package com.bobmowzie.mowziesmobs.server.entity.effects;

import net.minecraft.world.level.block.material.PushReaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.resources.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by BobMowzie on 9/2/2018.
 */
public abstract class EntityMagicEffect extends Entity {
    public LivingEntity caster;
    private static final EntityDataAccessor<Integer> CASTER = EntityDataManager.createKey(EntityMagicEffect.class, EntityDataSerializers.VARINT);

    public EntityMagicEffect(EntityType<? extends EntityMagicEffect> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public PushReaction getPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    protected void registerData() {
        getDataManager().register(CASTER, -1);
    }

    public int getCasterID() {
        return getDataManager().get(CASTER);
    }

    public void setCasterID(int id) {
        getDataManager().set(CASTER, id);
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
        if (ticksExisted == 1) {
            caster = (LivingEntity) world.getEntityByID(getCasterID());
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    public List<LivingEntity> getEntityLivingBaseNearby(double radius) {
        return getEntitiesNearby(LivingEntity.class, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(r, r, r), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearbyCube(Class<T> entityClass, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(r, r, r), e -> e != this);
    }
}
