package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityMagicEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
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
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.HashMap;
import java.util.List;

public class EntityPillar extends EntityGeomancyBase {
    private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(EntityPillar.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> RISING = SynchedEntityData.defineId(EntityPillar.class, EntityDataSerializers.BOOLEAN);

    public float prevHeight = 0;

    private static final float RISING_SPEED = 0.2f;

    private static final HashMap<GeomancyTier, Integer> SIZE_MAP = new HashMap<>();
    static {
        SIZE_MAP.put(GeomancyTier.NONE, 1);
        SIZE_MAP.put(GeomancyTier.SMALL, 2);
        SIZE_MAP.put(GeomancyTier.MEDIUM, 3);
        SIZE_MAP.put(GeomancyTier.LARGE, 4);
        SIZE_MAP.put(GeomancyTier.HUGE, 5);
    }


    public EntityPillar(EntityType<? extends EntityMagicEffect> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityPillar(EntityType<? extends EntityPillar> type, Level world, LivingEntity caster, BlockState blockState, BlockPos pos) {
        super(type, world, caster, blockState, pos);
    }

    public boolean checkCanSpawn() {
        if (!level.getEntitiesOfClass(EntityPillar.class, getBoundingBox().deflate(0.01)).isEmpty()) return false;
        return level.noCollision(this, getBoundingBox().deflate(0.01));
    }

    @Override
    public boolean canCollideWith(Entity p_20303_) {
        if (p_20303_ instanceof EntityBoulder) return false;
        return super.canCollideWith(p_20303_);
    }

    @Override
    public void tick() {
        prevHeight = getHeight();
        if (isRising()) {
            if (!level.isClientSide()) {
                setHeight(getHeight() + RISING_SPEED);
                List<EntityBoulder> boulders = level.getEntitiesOfClass(EntityBoulder.class, getBoundingBox().deflate(0.1f));
                for (EntityBoulder boulder : boulders) {
                    if (!boulder.isTravelling() && boulder.getTier().ordinal() > this.getTier().ordinal()) {
                        this.setTier(boulder.getTier());
                        boulder.explode();
                    }
                }
            }
        }

        this.setBoundingBox(this.makeBoundingBox());

        List<Entity> popUpEntities = level.getEntities(this, getBoundingBox().deflate(0.1f));
        for (Entity entity : popUpEntities) {
            if (entity.isPickable() && !(entity instanceof EntityBoulder) && !(entity instanceof EntityPillar)) {
                entity.setDeltaMovement(0, RISING_SPEED, 0);
            }
        }
        super.tick();
    }

    @Override
    protected AABB makeBoundingBox() {
        if (tickTimer() <= 1) return super.makeBoundingBox();
        float f = SIZE_MAP.get(getTier()) / 2.0F;
        return new AABB(getX() - (double)f, getY(), getZ() - (double)f, getX() + (double)f, getY() + getHeight(), getZ() + (double)f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(HEIGHT, 0.0f);
        getEntityData().define(RISING, true);
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
    }

    public boolean isRising() {
        return getEntityData().get(RISING);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("height", getHeight());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setHeight(compound.getFloat("height"));
        stopRising();
    }
}
