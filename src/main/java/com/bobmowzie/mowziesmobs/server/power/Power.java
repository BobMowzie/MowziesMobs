package com.bobmowzie.mowziesmobs.server.power;

import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

public abstract class Power {

    private final PlayerCapability.PlayerCapabilityImp capability;

    public Power(PlayerCapability.PlayerCapabilityImp capability) {
        this.capability = capability;
    }

    public void tick(TickEvent.PlayerTickEvent event) {

    }

    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {

    }

    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

    }

    public void onRightClickWithItem(PlayerInteractEvent.RightClickItem event) {

    }

    public void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {

    }

    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {

    }

    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {

    }

    public void onLeftClickEntity(AttackEntityEvent event) {

    }

    public void onTakeDamage(LivingHurtEvent event) {

    }

    public void onJump(LivingEvent.LivingJumpEvent event) {

    }

    public void onRightMouseDown(Player player) {

    }

    public void onLeftMouseDown(Player player) {

    }

    public void onRightMouseUp(Player player) {

    }

    public void onLeftMouseUp(Player player) {

    }

    public void onSneakDown(Player player) {

    }

    public void onSneakUp(Player player) {

    }

    public boolean canUse(Player player) {
        return true;
    }

    public PlayerCapability.PlayerCapabilityImp getProperties() {
        return capability;
    }

    public List<LivingEntity> getEntityLivingBaseNearby(LivingEntity player, double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(player, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double r) {
        return player.level.getEntitiesOfClass(entityClass, player.getBoundingBox().inflate(r, r, r), e -> e != player && player.distanceTo(e) <= r);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return player.level.getEntitiesOfClass(entityClass, player.getBoundingBox().inflate(dX, dY, dZ), e -> e != player && player.distanceTo(e) <= r);
    }
}
