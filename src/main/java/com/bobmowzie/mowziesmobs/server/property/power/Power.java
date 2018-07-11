package com.bobmowzie.mowziesmobs.server.property.power;

import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class Power {

    private MowziePlayerProperties properties;

    public Power(MowziePlayerProperties properties) {
        this.properties = properties;
    }

    public void onUpdate(TickEvent.PlayerTickEvent event) {

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

    public void onRightMouseDown(EntityPlayer player) {

    }

    public void onLeftMouseDown(EntityPlayer player) {

    }

    public void onRightMouseUp(EntityPlayer player) {

    }

    public void onLeftMouseUp(EntityPlayer player) {

    }

    public void onSneakDown(EntityPlayer player) {

    }

    public void onSneakUp(EntityPlayer player) {

    }

    public boolean canUse(EntityPlayer player) {
        return true;
    }

    public MowziePlayerProperties getProperties() {
        return properties;
    }

}
