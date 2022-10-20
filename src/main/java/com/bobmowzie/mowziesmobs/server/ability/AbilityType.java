package com.bobmowzie.mowziesmobs.server.ability;

import net.minecraft.world.entity.LivingEntity;

public class AbilityType<M extends LivingEntity, T extends Ability<M>> implements Comparable<AbilityType<M, T>> {
    private final IFactory<M, T> factory;
    private final String name;

    public AbilityType(String name, IFactory<M, T> factoryIn) {
        factory = factoryIn;
        this.name = name;
    }

    public T makeInstance(LivingEntity user) {
        return factory.create(this, (M) user);
    }

    public interface IFactory<M extends LivingEntity, T extends Ability<M>> {
        T create(AbilityType<M, T> p_create_1_, M user);
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(AbilityType<M, T> o) {
        return this.getName().compareTo(o.getName());
    }
}
