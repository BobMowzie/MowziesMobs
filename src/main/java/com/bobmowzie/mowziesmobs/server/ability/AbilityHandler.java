package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerUseAbility;
import com.bobmowzie.mowziesmobs.server.message.MessageUseAbility;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;

public enum AbilityHandler {
    INSTANCE;

    @Nullable
    public AbilityCapability.IAbilityCapability getAbilityCapability(LivingEntity entity) {
        return CapabilityHandler.getCapability(entity, AbilityCapability.AbilityProvider.ABILITY_CAPABILITY);
    }

    public <T extends LivingEntity> void sendAbilityMessage(T entity, Ability<?> ability) {
        if (entity.world.isRemote) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            AbilityInstance instance = abilityCapability.getAbilityInstances().get(ability);
            if (instance.canUse()) {
                abilityCapability.activateAbility(entity, ability);
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageUseAbility(entity.getEntityId(), ArrayUtils.indexOf(abilityCapability.getAbilities(entity), ability)));
            }
        }
    }

    public <T extends PlayerEntity> void sendPlayerTryAbilityMessage(T entity, Ability<?> ability) {
        if (!(entity.world.isRemote && entity instanceof ClientPlayerEntity)) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            MowziesMobs.NETWORK.sendToServer(new MessagePlayerUseAbility(ArrayUtils.indexOf(abilityCapability.getAbilities(entity), ability)));
        }
    }
}