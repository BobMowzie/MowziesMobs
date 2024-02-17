package com.bobmowzie.mowziesmobs.server.entity.frostmaw;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;

/**
 * Created by BobMowzie on 7/20/2017.
 */
public class EntityFrozenController extends Entity {
    public EntityFrozenController(EntityType<? extends EntityFrozenController> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide && tickCount >= 70 && !isVehicle()) discard() ;
//        List<Entity> passengers = getPassengers();
//        for (Entity passenger : passengers) {
//            if (passenger instanceof LivingEntity) {
//                LivingEntity livingEntity = (LivingEntity)passenger;
//                if (!livingEntity.isPotionActive(PotionHandler.FROZEN)) discard() ;
//            }
//        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

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
    public double getPassengersRidingOffset() {
        return 0;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    @Override
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
    	return true;
    }

    @Override
    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger))
        {
            if (passenger instanceof Player) passenger.setPos(this.getX(), this.getY(), this.getZ());
            else passenger.absMoveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        }
    }
}
