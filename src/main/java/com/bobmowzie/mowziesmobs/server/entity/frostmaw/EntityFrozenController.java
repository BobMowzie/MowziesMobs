package com.bobmowzie.mowziesmobs.server.entity.frostmaw;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * Created by BobMowzie on 7/20/2017.
 */
public class EntityFrozenController extends Entity {
    public EntityFrozenController(EntityType<? extends EntityFrozenController> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isClientSide && ticksExisted >= 70 && !isBeingRidden()) remove();
//        List<Entity> passengers = getPassengers();
//        for (Entity passenger : passengers) {
//            if (passenger instanceof LivingEntity) {
//                LivingEntity livingEntity = (LivingEntity)passenger;
//                if (!livingEntity.isPotionActive(PotionHandler.FROZEN)) remove();
//            }
//        }
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean canRiderInteract() {
        return false;
    }

    @Override
    public double getMountedYOffset() {
        return 0;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger))
        {
            if (passenger instanceof Player) passenger.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
            else passenger.setPositionAndRotation(this.getPosX(), this.getPosY(), this.getPosZ(), this.getYRot(), this.getXRot());
        }
    }
}
