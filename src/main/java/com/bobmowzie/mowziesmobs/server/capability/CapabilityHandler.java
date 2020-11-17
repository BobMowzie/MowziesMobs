package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public final class CapabilityHandler {
    public static void register() {
        CapabilityManager.INSTANCE.register(FrozenCapability.IFrozenCapability.class, new FrozenCapability.FrozenStorage(), FrozenCapability.FrozenCapabilityImp::new);
        CapabilityManager.INSTANCE.register(LastDamageCapability.ILastDamageCapability.class, new LastDamageCapability.LastDamageStorage(), LastDamageCapability.LastDamageCapabilityImp::new);
        CapabilityManager.INSTANCE.register(PlayerCapability.IPlayerCapability.class, new PlayerCapability.PlayerStorage(), PlayerCapability.PlayerCapabilityImp::new);
    }

    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        return entity.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty"));
    }
}
