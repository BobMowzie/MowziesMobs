package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.*;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy.*;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.message.MessageInterruptAbility;
import com.bobmowzie.mowziesmobs.server.message.MessageJumpToAbilitySection;
import com.bobmowzie.mowziesmobs.server.message.MessagePlayerUseAbility;
import com.bobmowzie.mowziesmobs.server.message.MessageUseAbility;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;

public enum AbilityHandler {
    INSTANCE;

    public static final AbilityType<Player, FireballAbility> FIREBALL_ABILITY = new AbilityType<>("fireball", FireballAbility::new);
    public static final AbilityType<Player, SunstrikeAbility> SUNSTRIKE_ABILITY = new AbilityType<>("sunstrike", SunstrikeAbility::new);
    public static final AbilityType<Player, SolarBeamAbility> SOLAR_BEAM_ABILITY = new AbilityType<>("solar_beam", SolarBeamAbility::new);
    public static final AbilityType<Player, WroughtAxeSwingAbility> WROUGHT_AXE_SWING_ABILITY = new AbilityType<>("wrought_axe_swing", WroughtAxeSwingAbility::new);
    public static final AbilityType<Player, WroughtAxeSlamAbility> WROUGHT_AXE_SLAM_ABILITY = new AbilityType<>("wrought_axe_slam", WroughtAxeSlamAbility::new);
    public static final AbilityType<Player, IceBreathAbility> ICE_BREATH_ABILITY = new AbilityType<>("ice_breath", IceBreathAbility::new);
    public static final AbilityType<Player, SpawnBoulderAbility> SPAWN_BOULDER_ABILITY = new AbilityType<>("spawn_boulder", SpawnBoulderAbility::new);
    public static final AbilityType<Player, TunnelingAbility> TUNNELING_ABILITY = new AbilityType<>("tunneling", TunnelingAbility::new);
    public static final AbilityType<Player, SimplePlayerAnimationAbility> HIT_BOULDER_ABILITY = new AbilityType<>("hit_boulder", (type, player) ->
            new SimplePlayerAnimationAbility(type, (Player) player, "hit_boulder", 10, false, false)
    );
    public static final AbilityType<Player, SpawnPillarAbility> SPAWN_PILLAR_ABILITY = new AbilityType<>("spawn_pillar", SpawnPillarAbility::new);
    public static final AbilityType<Player, GroundSlamAbility> GROUND_SLAM_ABILITY = new AbilityType<>("ground_slam", GroundSlamAbility::new);
    public static final AbilityType<Player, BoulderRollAbility> BOULDER_ROLL_ABILITY = new AbilityType<>("boulder_roll", BoulderRollAbility::new);

    public static final AbilityType<Player, SimplePlayerAnimationAbility> BACKSTAB_ABILITY = new AbilityType<>("backstab", (type, player) ->
            new SimplePlayerAnimationAbility(type, (Player) player, "backstab", 12, true, true)
    );

    public static final AbilityType<Player, RockSlingAbility> ROCK_SLING = new AbilityType<>("rock_sling", RockSlingAbility::new);

    public static final AbilityType<Player, ? extends PlayerAbility>[] PLAYER_ABILITIES = new AbilityType[] {
            SUNSTRIKE_ABILITY,
            SOLAR_BEAM_ABILITY,
            WROUGHT_AXE_SWING_ABILITY,
            WROUGHT_AXE_SLAM_ABILITY,
            ICE_BREATH_ABILITY,
            SPAWN_BOULDER_ABILITY,
            SPAWN_PILLAR_ABILITY,
            TUNNELING_ABILITY,
            HIT_BOULDER_ABILITY,
            BACKSTAB_ABILITY,
            BOULDER_ROLL_ABILITY,
            ROCK_SLING,
            GROUND_SLAM_ABILITY
    };

    @Nullable
    public AbilityCapability.IAbilityCapability getAbilityCapability(LivingEntity entity) {
        return CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);
    }

    @Nullable
    public Ability getAbility(LivingEntity entity, AbilityType<?, ?> abilityType) {
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            return abilityCapability.getAbilityMap().get(abilityType);
        }
        return null;
    }

    public <T extends LivingEntity> void sendAbilityMessage(T entity, AbilityType<?, ?> abilityType) {
        if (entity.level.isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance != null && instance.canUse()) {
                abilityCapability.activateAbility(entity, abilityType);
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageUseAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)));
            }
        }
    }

    public <T extends LivingEntity> void sendInterruptAbilityMessage(T entity, AbilityType<?, ?> abilityType) {
        if (entity.level.isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.interrupt();
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageInterruptAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)));
            }
        }
    }

    public <T extends Player> void sendPlayerTryAbilityMessage(T entity, AbilityType<?, ?> ability) {
        if (!(entity.level.isClientSide && entity instanceof LocalPlayer)) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            MowziesMobs.NETWORK.sendToServer(new MessagePlayerUseAbility(ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), ability)));
        }
    }


    public <T extends LivingEntity> void sendJumpToSectionMessage(T entity, AbilityType<?, ?> abilityType, int sectionIndex) {
        if (entity.level.isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.jumpToSection(sectionIndex);
                MowziesMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageJumpToAbilitySection(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType), sectionIndex));
            }
        }
    }
}