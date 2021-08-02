package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityInstance;
import com.bobmowzie.mowziesmobs.server.ability.FireballAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityCapability {
    public static final FireballAbility FIREBALL_ABILITY = new FireballAbility();
    public static final Ability<?>[] ABILITIES = new Ability[] {
            FIREBALL_ABILITY
    };

    public interface IAbilityCapability {
        void activateAbility(LivingEntity entity, Ability<?> ability);

        public void instanceAbilities(LivingEntity entity);

        void tick(LivingEntity entity);

        INBT writeNBT();

        void readNBT(INBT nbt);
    }

    public static class AbilityCapabilityImp implements IAbilityCapability {
        Map<Ability<?>, AbilityInstance> abilityInstances = new HashMap<>();

        @Override
        public void instanceAbilities(LivingEntity entity) {
            for (Ability<?> ability : ABILITIES) {
                abilityInstances.put(ability, ability.makeInstance(entity));
            }
        }

        @Override
        public void activateAbility(LivingEntity entity, Ability<?> ability) {
            AbilityInstance instance = abilityInstances.get(ability);
            if (instance != null) instance.onStart();
            else System.out.println("Ability " + ability.getClass().getSimpleName() + " does not exist on mob " + entity.getClass().getSimpleName());
        }

        @Override
        public void tick(LivingEntity entity) {
            for (AbilityInstance abilityInstance : abilityInstances.values()) {
                if (abilityInstance.isUsing()) abilityInstance.tick();
            }
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
