package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class TestEntity extends Entity {
    public TestEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }


    public TestEntity(EntityType<?> p_19870_, Level p_19871_, Vec3 position) {
        super(p_19870_, p_19871_);
        this.setPos(position);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= 20) remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
