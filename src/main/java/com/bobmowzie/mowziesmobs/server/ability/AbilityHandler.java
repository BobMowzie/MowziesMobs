package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.ability.abilities.*;
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

    public static final AbilityType<FireballAbility> FIREBALL_ABILITY = new AbilityType<>("fireball", FireballAbility::new);
    public static final AbilityType<SunstrikeAbility> SUNSTRIKE_ABILITY = new AbilityType<>("sunstrike", SunstrikeAbility::new);
    public static final AbilityType<SolarBeamAbility> SOLAR_BEAM_ABILITY = new AbilityType<>("solar_beam", SolarBeamAbility::new);
    public static final AbilityType<WroughtAxeSwingAbility> WROUGHT_AXE_SWING_ABILITY = new AbilityType<>("wrought_axe_swing", WroughtAxeSwingAbility::new);
    public static final AbilityType<WroughtAxeSlamAbility> WROUGHT_AXE_SLAM_ABILITY = new AbilityType<>("wrought_axe_slam", WroughtAxeSlamAbility::new);
    public static final AbilityType<IceBreathAbility> ICE_BREATH_ABILITY = new AbilityType<>("ice_breath", IceBreathAbility::new);
    public static final AbilityType<SpawnBoulderAbility> SPAWN_BOULDER_ABILITY = new AbilityType<>("spawn_boulder", SpawnBoulderAbility::new);
    public static final AbilityType<TunnelingAbility> TUNNELING_ABILITY = new AbilityType<>("tunneling", TunnelingAbility::new);
    public static final AbilityType<?>[] PLAYER_ABILITIES = new AbilityType[] {
            SUNSTRIKE_ABILITY,
            SOLAR_BEAM_ABILITY,
            WROUGHT_AXE_SWING_ABILITY,
            WROUGHT_AXE_SLAM_ABILITY,
            ICE_BREATH_ABILITY,
            SPAWN_BOULDER_ABILITY,
            TUNNELING_ABILITY
    };

    @Nullable
    public AbilityCapability.IAbilityCapability getAbilityCapability(LivingEntity entity) {
        return CapabilityHandler.getCapability(entity, AbilityCapability.AbilityProvider.ABILITY_CAPABILITY);
    }

    @Nullable
    public Ability getAbility(LivingEntity entity, AbilityType<?> abilityType) {
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            return abilityCapability.getAbilityMap().get(abilityType);
        }
        return null;
    }

    public <T extends LivingEntity> void sendAbilityMessage(T entity, AbilityType<?> ability) {
        if (entity.world.isRemote) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(ability);
            if (instance.canUse()) {
                abilityCapability.activateAbility(entity, ability);
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageUseAbility(entity.getEntityId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), ability)));
            }
        }
    }

    public <T extends PlayerEntity> void sendPlayerTryAbilityMessage(T entity, AbilityType<?> ability) {
        if (!(entity.world.isRemote && entity instanceof ClientPlayerEntity)) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            MowziesMobs.NETWORK.sendToServer(new MessagePlayerUseAbility(ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), ability)));
        }
    }
}