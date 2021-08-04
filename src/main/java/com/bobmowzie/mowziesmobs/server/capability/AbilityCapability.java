package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.FireballAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.SolarBeamAbility;
import com.bobmowzie.mowziesmobs.server.ability.abilities.SunstrikeAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import java.util.*;

public class AbilityCapability {
    public static final AbilityType<FireballAbility> FIREBALL_ABILITY = new AbilityType<>(FireballAbility::new);
    public static final AbilityType<SunstrikeAbility> SUNSTRIKE_ABILITY = new AbilityType<>(SunstrikeAbility::new);
    public static final AbilityType<SolarBeamAbility> SOLAR_BEAM_ABILITY = new AbilityType<>(SolarBeamAbility::new);
    public static final AbilityType<?>[] PLAYER_ABILITIES = new AbilityType[] {
            SUNSTRIKE_ABILITY,
            SOLAR_BEAM_ABILITY
    };

    public interface IAbilityCapability {

        void activateAbility(LivingEntity entity, AbilityType<?> ability);

        void instanceAbilities(LivingEntity entity);

        void tick(LivingEntity entity);

        AbilityType<?>[] getAbilities(LivingEntity entity);

        Map<AbilityType<?>, Ability> getAbilityInstances();

        Ability getActiveAbility();

        void setActiveAbility(Ability activeAbility);

        INBT writeNBT();

        void readNBT(INBT nbt);
    }

    public static class AbilityCapabilityImp implements IAbilityCapability {
        Map<AbilityType<?>, Ability> abilityInstances = new HashMap<>();
        Ability activeAbility = null;

        @Override
        public void instanceAbilities(LivingEntity entity) {
            for (AbilityType<?> ability : getAbilities(entity)) {
                abilityInstances.put(ability, ability.makeInstance(entity));
            }
        }

        @Override
        public void activateAbility(LivingEntity entity, AbilityType<?> abilityType) {
            Ability ability = abilityInstances.get(abilityType);
            if (ability != null) {
                boolean tryResult = ability.tryAbility();
                if (tryResult) ability.start();
            }
            else System.out.println("Ability " + abilityType.toString() + " does not exist on mob " + entity.getClass().getSimpleName());
        }

        @Override
        public void tick(LivingEntity entity) {
            for (Ability ability : abilityInstances.values()) {
                ability.tick();
            }
        }

        @Override
        public AbilityType<?>[] getAbilities(LivingEntity entity) {
            if (entity instanceof PlayerEntity) {
                return PLAYER_ABILITIES;
            }
            return new AbilityType[0];
        }

        @Override
        public Map<AbilityType<?>, Ability> getAbilityInstances() {
            return abilityInstances;
        }

        @Override
        public Ability getActiveAbility() {
            return activeAbility;
        }

        @Override
        public void setActiveAbility(Ability activeAbility) {
            if (getActiveAbility() != null && getActiveAbility().isUsing()) getActiveAbility().end();
            this.activeAbility = activeAbility;
        }

        @Override
        public INBT writeNBT() {
            CompoundNBT compound = new CompoundNBT();
            return compound;
        }

        @Override
        public void readNBT(INBT nbt) {
            CompoundNBT compound = (CompoundNBT) nbt;
        }
    }

    public static class AbilityStorage implements Capability.IStorage<IAbilityCapability> {
        @Override
        public INBT writeNBT(Capability<AbilityCapability.IAbilityCapability> capability, AbilityCapability.IAbilityCapability instance, Direction side) {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<AbilityCapability.IAbilityCapability> capability, AbilityCapability.IAbilityCapability instance, Direction side, INBT nbt) {
            instance.readNBT(nbt);
        }
    }

    public static class AbilityProvider implements ICapabilityProvider
    {
        @CapabilityInject(IAbilityCapability.class)
        public static final Capability<IAbilityCapability> ABILITY_CAPABILITY = null;

        private final LazyOptional<IAbilityCapability> instance = LazyOptional.of(ABILITY_CAPABILITY::getDefaultInstance);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ABILITY_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }
}
