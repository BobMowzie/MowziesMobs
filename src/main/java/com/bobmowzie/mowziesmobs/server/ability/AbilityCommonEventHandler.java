package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AbilityCommonEventHandler {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        PlayerEntity player = event.getPlayer();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onRightClickEmpty(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onRightClickBlock(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onRightClickWithItem(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        PlayerEntity player = event.getPlayer();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onRightClickEntity(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        PlayerEntity player = event.getPlayer();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onLeftClickEmpty(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        PlayerEntity player = event.getPlayer();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onLeftClickBlock(event);
            }
        }
    }

    @SubscribeEvent
    public void onLeftClickEntity(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onLeftClickEntity(event);
            }
        }
    }

    @SubscribeEvent
    public void onTakeDamage(LivingHurtEvent event) {
        LivingEntity player = event.getEntityLiving();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onTakeDamage(event);
            }
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity player = event.getEntityLiving();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onJump(event);
            }
        }
    }
}
