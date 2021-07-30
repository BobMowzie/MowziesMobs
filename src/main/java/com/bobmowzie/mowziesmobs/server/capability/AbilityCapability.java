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
import java.util.List;

public class AbilityCapability {
    public static final Ability<?>[] abilityList = new Ability[] {
        new FireballAbility()
    };

    public interface IAbilityCapability {
        void activateAbility(LivingEntity entity, Ability<?> ability);

        void tick(LivingEntity entity);

        INBT writeNBT();

        void readNBT(INBT nbt);
    }

    public static class AbilityCapabilityImp implements IAbilityCapability {
        List<AbilityInstance> activeAbilities = new ArrayList<>();

        @Override
        public void activateAbility(LivingEntity entity, Ability<?> ability) {
            AbilityInstance instance = ability.makeInstance(entity);
            instance.onStart();
            activeAbilities.add(instance);
        }

        @Override
        public void tick(LivingEntity entity) {
            for (AbilityInstance abilityInstance : activeAbilities) {
                if (!abilityInstance.isUsing()) activeAbilities.remove(abilityInstance);
                else abilityInstance.tick();
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
