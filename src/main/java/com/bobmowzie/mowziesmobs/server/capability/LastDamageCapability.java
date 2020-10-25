package com.bobmowzie.mowziesmobs.server.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class LastDamageCapability {
    public interface ILastDamageCapability {
        void setLastDamage(float damage);
        float getLastDamage();
    }

    public static class LastDamageCapabilityImp implements ILastDamageCapability {
        float lastDamage = 0;

        @Override
        public void setLastDamage(float damage) {
            lastDamage = damage;
        }

        @Override
        public float getLastDamage() {
            return lastDamage;
        }
    }

    public static class LastDamageStorage implements Capability.IStorage<ILastDamageCapability> {
        @Override
        public INBT writeNBT(Capability<ILastDamageCapability> capability, ILastDamageCapability instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability<ILastDamageCapability> capability, ILastDamageCapability instance, Direction side, INBT nbt) {

        }
    }

    public static class LastDamageProvider implements ICapabilityProvider
    {
        @CapabilityInject(ILastDamageCapability.class)
        public static final Capability<ILastDamageCapability> LAST_DAMAGE_CAPABILITY = null;

        private LazyOptional<ILastDamageCapability> instance = LazyOptional.of(LAST_DAMAGE_CAPABILITY::getDefaultInstance);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == LAST_DAMAGE_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }
}
