package com.bobmowzie.mowziesmobs.server.ability;

import net.minecraft.world.entity.LivingEntity;

public class AbilityType<T extends Ability> implements Comparable<AbilityType<T>> {
    private final IFactory<T> factory;
    private final String name;

    public AbilityType(String name, IFactory<T> factoryIn) {
        factory = factoryIn;
        this.name = name;
    }

    public T makeInstance(LivingEntity user) {
        return factory.create(this, user);
    }

    public interface IFactory<T extends Ability> {
        T create(AbilityType<T> p_create_1_, LivingEntity user);
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(AbilityType<T> o) {
        return this.getName().compareTo(o.getName());
    }
}
