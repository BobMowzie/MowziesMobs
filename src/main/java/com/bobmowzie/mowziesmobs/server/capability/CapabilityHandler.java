package com.bobmowzie.mowziesmobs.server.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public final class CapabilityHandler {
    public static void register() {
        CapabilityManager.INSTANCE.register(FrozenCapability.IFrozenCapability.class, new FrozenCapability.FrozenStorage(), FrozenCapability.FrozenCapabilityImp::new);
        CapabilityManager.INSTANCE.register(LastDamageCapability.ILastDamageCapability.class, new LastDamageCapability.LastDamageStorage(), LastDamageCapability.LastDamageCapabilityImp::new);
        CapabilityManager.INSTANCE.register(PlayerCapability.IPlayerCapability.class, new PlayerCapability.PlayerStorage(), PlayerCapability.PlayerCapabilityImp::new);
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        if (!entity.isAlive()) return null;
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }
}
