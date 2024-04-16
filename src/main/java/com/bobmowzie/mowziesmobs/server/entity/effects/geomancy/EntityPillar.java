package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityMagicEffect;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;

public class EntityPillar extends EntityGeomancyBase {
    private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(EntityPillar.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> RISING = SynchedEntityData.defineId(EntityPillar.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FALLING = SynchedEntityData.defineId(EntityPillar.class, EntityDataSerializers.BOOLEAN);
    public static final float RISING_SPEED = 0.25f;

    public float prevPrevHeight = 0;
    public float prevHeight = 0;

    public static final HashMap<GeomancyTier, Integer> SIZE_MAP = new HashMap<>();
    static {
        SIZE_MAP.put(GeomancyTier.NONE, 1);
        SIZE_MAP.put(GeomancyTier.SMALL, 2);
        SIZE_MAP.put(GeomancyTier.MEDIUM, 3);
        SIZE_MAP.put(GeomancyTier.LARGE, 4);
        SIZE_MAP.put(GeomancyTier.HUGE, 5);
    }

    private EntityPillarPiece currentPiece;

    public EntityPillar(EntityType<? extends EntityMagicEffect> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityPillar(EntityType<? extends EntityPillar> type, Level world, LivingEntity caster, BlockState blockState, BlockPos pos) {
        super(type, world, caster, blockState, pos);
        setDeathTime(300);
    }

    public boolean checkCanSpawn() {
        if (!level().getEntitiesOfClass(EntityPillar.class, getBoundingBox().deflate(0.01)).isEmpty()) return false;
        return level().noCollision(this, getBoundingBox().deflate(0.01));
    }

    @Override
    public boolean canCollideWith(Entity p_20303_) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void tick() {
        if (caster instanceof EntitySculptor) {
            EntitySculptor sculptor = (EntitySculptor)caster;
            if (sculptor.getPillar() == null) sculptor.setPillar(this);
        }
        prevPrevHeight = prevHeight;
        prevHeight = getHeight();

        if (!level().isClientSide()) {
            if (isRising()) {
                float height = getHeight();

                if (height == 0.0) {
                    currentPiece = new EntityPillarPiece(EntityHandler.PILLAR_PIECE.get(), this.level(), this, new Vec3(this.getX(), this.getY() - 1.0f, this.getZ()));
                    level().addFreshEntity(currentPiece);
                }

                height += RISING_SPEED;
                setHeight(height);

                if (Math.floor(height) > Math.floor(prevHeight)) {
                    currentPiece = new EntityPillarPiece(EntityHandler.PILLAR_PIECE.get(), this.level(), this, new Vec3(this.getX(), this.getY() + Math.floor(height) - 1.0f, this.getZ()));
                    level().addFreshEntity(currentPiece);
                }

                List<EntityBoulderProjectile> boulders = level().getEntitiesOfClass(EntityBoulderProjectile.class, getBoundingBox().deflate(0.1f));
                for (EntityBoulderProjectile boulder : boulders) {
                    if (!boulder.isTravelling() && boulder.getTier().ordinal() > this.getTier().ordinal()) {
                        this.setTier(boulder.getTier());
                        boulder.explode();
                    }
                }
            }
            else if (isFalling()) {
                float height = getHeight();
                height -= RISING_SPEED;
                setHeight(height);
                if (height <= 0.0) {
                    remove(RemovalReason.DISCARDED);
                }
            }
        }

        this.setBoundingBox(this.makeBoundingBox());

        AABB popUpBounds = getBoundingBox().deflate(0.1f);
        List<Entity> popUpEntities = level().getEntities(this, popUpBounds);
        for (Entity entity : popUpEntities) {
            if (entity.isPickable() && !(entity instanceof EntityBoulderBase) && !(entity instanceof EntityPillar) && !(entity instanceof EntityPillarPiece)) {
                double belowAmount = entity.getY() - (getY() + getHeight());
                if (belowAmount < 0.0) entity.move(MoverType.PISTON, new Vec3(0, -belowAmount, 0));
            }
        }
        super.tick();
        if (hasSyncedCaster && (caster == null || caster.isRemoved())) explode();
    }

    @Override
    protected AABB makeBoundingBox() {
        if (tickTimer() <= 1) return super.makeBoundingBox();
        float f = SIZE_MAP.get(getTier()) / 2.0F - 0.05f;
        return new AABB(getX() - (double)f, getY(), getZ() - (double)f, getX() + (double)f, getY() + getHeight() - 0.05f, getZ() + (double)f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(HEIGHT, 0.0f);
        getEntityData().define(RISING, true);
        getEntityData().define(FALLING, false);
    }

    public float getHeight() {
        return getEntityData().get(HEIGHT);
    }

    public void setHeight(float height) {
        getEntityData().set(HEIGHT, height);
    }

    public void stopRising() {
        getEntityData().set(RISING, false);
        this.setBoundingBox(this.makeBoundingBox());
        currentPiece = new EntityPillarPiece(EntityHandler.PILLAR_PIECE.get(), this.level(), this, new Vec3(this.getX(), this.getY() + getHeight() - 1.0f, this.getZ()));
        level().addFreshEntity(currentPiece);
    }

    public boolean isRising() {
        return getEntityData().get(RISING);
    }

    public void startFalling() {
        getEntityData().set(RISING, false);
        getEntityData().set(FALLING, true);
    }

    public boolean isFalling() {
        return getEntityData().get(FALLING);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("height", getHeight());
        compound.putBoolean("rising", isRising());
        compound.putBoolean("falling", isFalling());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setHeight(compound.getFloat("height"));
        getEntityData().set(RISING, compound.getBoolean("rising"));
        getEntityData().set(FALLING, compound.getBoolean("falling"));
    }

    @Override
    public boolean doRemoveTimer() {
        return !(caster instanceof EntitySculptor);
    }
}
