package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;
import java.util.UUID;

public class EntityPillarPiece extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> PILLAR = SynchedEntityData.defineId(EntityPillarPiece.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> TIER = SynchedEntityData.defineId(EntityPillarPiece.class, EntityDataSerializers.INT);

    private EntityPillar pillar;

    public EntityPillarPiece(EntityType<?> type, Level level) {
        super(type, level);
    }

    public EntityPillarPiece(EntityType<?> type, Level level, EntityPillar pillar, Vec3 position) {
        super(type, level);
        this.pillar = pillar;
        setTier(pillar.getTier());
        this.absMoveTo(position.x, position.y, position.z);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        if (entity instanceof EntityPillar || entity instanceof EntityPillarPiece) return false;
        return super.canCollideWith(entity);
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            if (pillar == null) {
                pillar = getPillar();
                if (pillar == null) {
                    remove(RemovalReason.DISCARDED);
                    return;
                }
            }
            if (pillar.isRemoved()) {
                remove(RemovalReason.DISCARDED);
                return;
            }
            setTier(pillar.getTier());
        }

        super.tick();
        setBoundingBox(makeBoundingBox());
    }

    @Override
    protected AABB makeBoundingBox() {
        float f = EntityPillar.SIZE_MAP.get(getTier()) / 2.0F;
        return new AABB(getX() - (double)f, getY(), getZ() - (double)f, getX() + (double)f, getY() + 1, getZ() + (double)f);
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(PILLAR, Optional.empty());
        getEntityData().define(TIER, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        setPillarUUID(compound.getUUID("pillar"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (pillar != null) compound.putUUID("pillar", pillar.getUUID());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.BLOCK;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    public Optional<UUID> getPillarUUID() {
        return getEntityData().get(PILLAR);
    }

    public void setPillarUUID(UUID uuid) {
        getEntityData().set(PILLAR, Optional.of(uuid));
    }

    public EntityPillar getPillar() {
        Optional<UUID> uuid = getPillarUUID();
        if (uuid.isPresent() && !level.isClientSide) {
            return (EntityPillar) ((ServerLevel) level).getEntity(uuid.get());
        }
        return null;
    }

    public EntityGeomancyBase.GeomancyTier getTier() {
        return EntityGeomancyBase.GeomancyTier.values()[entityData.get(TIER)];
    }

    public void setTier(EntityGeomancyBase.GeomancyTier size) {
        entityData.set(TIER, size.ordinal());
    }
}
