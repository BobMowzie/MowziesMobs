package com.bobmowzie.mowziesmobs.server.entity.effects.geomancy;

import com.bobmowzie.mowziesmobs.server.entity.effects.EntityMagicEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.manager.AnimationData;

public class EntityPillar extends EntityGeomancyBase {
    private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(EntityPillar.class, EntityDataSerializers.FLOAT);

    private boolean rising = true;

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
    public void tick() {
        super.tick();
        if (!level.isClientSide() && rising) {
            setHeight(getHeight() + 0.1f);
        }
        float f = this.getBbWidth() / 2.0F;
        AABB aabb = new AABB(getX() - (double)f, getY(), getZ() - (double)f, getX() + (double)f, getY() + getHeight(), getZ() + (double)f);
        setBoundingBox(aabb);
    }

    public void stopRising() {
        rising = false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(HEIGHT, 0.0f);
    }

    public float getHeight() {
        return getEntityData().get(HEIGHT);
    }

    public void setHeight(float height) {
        getEntityData().set(HEIGHT, height);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("height", getHeight());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        setHeight(compound.getFloat("height"));
        stopRising();
    }
}
