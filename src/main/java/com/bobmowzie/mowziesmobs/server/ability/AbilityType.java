package com.bobmowzie.mowziesmobs.server.ability;

import net.minecraft.entity.LivingEntity;

// Ability type class defining behaviors and attributes of ability
public class AbilityType<T extends Ability> {
    private final IFactory<T> factory;

    public AbilityType(IFactory<T> factoryIn) {
        factory = factoryIn;
    }

    public T makeInstance(LivingEntity user) {
        return factory.create(this, user);
    }

    public interface IFactory<T extends Ability> {
        T create(AbilityType<T> p_create_1_, LivingEntity user);
    }
}
