package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerTryAbility;
import com.bobmowzie.mowziesmobs.server.message.MessageUseAbility;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public enum AbilityHandler {
    INSTANCE;

    public <T extends LivingEntity> void sendAbilityMessage(T entity, Ability<?> ability) {
        if (entity.world.isRemote) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, AbilityCapability.AbilityProvider.ABILITY_CAPABILITY);
        if (abilityCapability != null) {
            AbilityInstance instance = abilityCapability.getAbilityInstances().get(ability);
            if (instance.canUse()) {
                abilityCapability.activateAbility(entity, ability);
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageUseAbility(entity.getEntityId(), abilityCapability.getAbilities().indexOf(ability)));
            }
        }
    }

    public <T extends PlayerEntity> void sendPlayerTryAbilityMessage(T entity, Ability<?> ability) {
        if (!(entity.world.isRemote && entity instanceof ClientPlayerEntity)) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, AbilityCapability.AbilityProvider.ABILITY_CAPABILITY);
        if (abilityCapability != null) {
            MowziesMobs.NETWORK.sendToServer(new MessagePlayerTryAbility(abilityCapability.getAbilities().indexOf(ability)));
        }
    }
}