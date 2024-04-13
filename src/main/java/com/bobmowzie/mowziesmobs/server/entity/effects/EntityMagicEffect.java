package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.ILinkedEntity;
import com.bobmowzie.mowziesmobs.server.message.MessageLinkEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by BobMowzie on 9/2/2018.
 */
public abstract class EntityMagicEffect extends Entity implements ILinkedEntity {
    public LivingEntity caster;
    protected boolean hasSyncedCaster = false;
    private static final EntityDataAccessor<Optional<UUID>> CASTER = SynchedEntityData.defineId(EntityMagicEffect.class, EntityDataSerializers.OPTIONAL_UUID);

    public EntityMagicEffect(EntityType<? extends EntityMagicEffect> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityMagicEffect(EntityType<? extends EntityMagicEffect> type, Level world, LivingEntity caster) {
        super(type, world);
        if (!world.isClientSide && caster != null) {
            this.setCasterID(caster.getUUID());
        }
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(CASTER, Optional.empty());
    }

    public Optional<UUID> getCasterID() {
        return getEntityData().get(CASTER);
    }

    public void setCasterID(UUID id) {
        getEntityData().set(CASTER, Optional.of(id));
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!level().isClientSide() && getCasterID().isPresent() && caster == null) {
            Entity casterEntity = ((ServerLevel)this.level()).getEntity(getCasterID().get());
            if (casterEntity instanceof LivingEntity) {
                caster = (LivingEntity) casterEntity;
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this), new MessageLinkEntities(this, caster));
            }
            hasSyncedCaster = true;
        }
    }

    @Override
    public void link(Entity entity) {
        if (entity instanceof LivingEntity) {
            caster = (LivingEntity) entity;
        }
        hasSyncedCaster = true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        setCasterID(compound.getUUID("caster"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (getCasterID().isPresent()) {
            compound.putUUID("caster", getCasterID().get());
        }
    }

    public List<LivingEntity> getEntityLivingBaseNearby(double radius) {
        return getEntitiesNearby(LivingEntity.class, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return level().getEntitiesOfClass(entityClass, getBoundingBox().inflate(r, r, r), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearbyCube(Class<T> entityClass, double r) {
        return level().getEntitiesOfClass(entityClass, getBoundingBox().inflate(r, r, r), e -> e != this);
    }

    public boolean raytraceCheckEntity(Entity entity) {
        Vec3 from = this.position();
        int numChecks = 3;
        for (int i = 0; i < numChecks; i++) {
            float increment = entity.getBbHeight() / (numChecks + 1);
            Vec3 to = entity.position().add(0, increment * (i + 1), 0);
            BlockHitResult result = level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (result.getType() != HitResult.Type.BLOCK) {
                return true;
            }
        }
        return false;
    }
}
