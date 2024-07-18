package com.bobmowzie.mowziesmobs.server.entity.effects;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.entity.ILinkedEntity;
import com.bobmowzie.mowziesmobs.server.message.MessageLinkEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
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
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by BobMowzie on 9/2/2018.
 */
public abstract class EntityMagicEffect extends Entity implements ILinkedEntity {
    private LivingEntity cachedCaster;
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

    public LivingEntity getCaster() {
        if (this.cachedCaster != null && !this.cachedCaster.isRemoved()) {
            return this.cachedCaster;
        } else if (this.getCasterID().isPresent() && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.getCasterID().get());
            if (entity instanceof LivingEntity) {
                cachedCaster = (LivingEntity) entity;
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this), new MessageLinkEntities(this, cachedCaster));
            }
            return this.cachedCaster;
        } else {
            return null;
        }
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
//        if (!level().isClientSide() && getCasterID().isPresent() && cachedCaster == null) {
//            Entity casterEntity = ((ServerLevel)this.level()).getEntity(getCasterID().get());
//            if (casterEntity instanceof LivingEntity) {
//                cachedCaster = (LivingEntity) casterEntity;
//                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this), new MessageLinkEntities(this, cachedCaster));
//            }
//            hasSyncedCaster = true;
//        }
    }

    @Override
    public void link(Entity entity) {
        if (entity instanceof LivingEntity) {
            cachedCaster = (LivingEntity) entity;
        }
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

    public List<Entity> getEntitiesNearby(double radius) {
        return getEntitiesNearby(Entity.class, radius);
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

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        LivingEntity entity = this.cachedCaster;
        return new ClientboundAddEntityPacket(this, entity == null ? 0 : entity.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Entity entity = this.level().getEntity(packet.getData());
        if (entity instanceof LivingEntity) {
            cachedCaster = (LivingEntity) entity;
        }
    }
}
