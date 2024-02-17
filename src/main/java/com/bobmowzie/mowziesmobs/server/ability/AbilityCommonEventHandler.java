package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AbilityCommonEventHandler {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                if (ability instanceof PlayerAbility) {
                    ((PlayerAbility)ability).onRightClickEmpty(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                if (ability instanceof PlayerAbility) {
                    ((PlayerAbility)ability).onRightClickBlock(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                if (ability instanceof PlayerAbility) {
                    ((PlayerAbility)ability).onRightClickWithItem(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                if (ability instanceof PlayerAbility) {
                    ((PlayerAbility)ability).onRightClickEntity(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                if (ability instanceof PlayerAbility) {
                    ((PlayerAbility)ability).onLeftClickEmpty(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                if (ability instanceof PlayerAbility) {
                    ((PlayerAbility)ability).onLeftClickBlock(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLeftClickEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                if (ability instanceof PlayerAbility) {
                    ((PlayerAbility)ability).onLeftClickEntity(event);
                }
            }
        }
    }

    @SubscribeEvent
    public void onTakeDamage(LivingHurtEvent event) {
        LivingEntity player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                ability.onTakeDamage(event);
            }
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity player = event.getEntity();
        AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        if (abilityCapability != null) {
            for (Ability ability : abilityCapability.getAbilities()) {
                if (ability instanceof PlayerAbility) {
                    ((PlayerAbility)ability).onJump(event);
                }
            }
        }
    }
}
