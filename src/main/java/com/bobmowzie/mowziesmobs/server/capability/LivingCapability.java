package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class LivingCapability {
    public interface ILivingCapability {
        void setLastDamage(float damage);
        float getLastDamage();
        void setHasSunblock(boolean hasSunblock);
        boolean getHasSunblock();
    }

    public static class LastDamageCapabilityImp implements ILivingCapability {
        float lastDamage = 0;
        boolean hasSunblock;

        @Override
        public void setLastDamage(float damage) {
            lastDamage = damage;
        }

        @Override
        public float getLastDamage() {
            return lastDamage;
        }

        @Override
        public void setHasSunblock(boolean hasSunblock) {
            this.hasSunblock = hasSunblock;
        }

        @Override
        public boolean getHasSunblock() {
            return hasSunblock;
        }
    }

    public static class LivingStorage implements Capability.IStorage<ILivingCapability> {
        @Override
        public INBT writeNBT(Capability<ILivingCapability> capability, ILivingCapability instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability<ILivingCapability> capability, ILivingCapability instance, Direction side, INBT nbt) {

        }
    }

    public static class LivingProvider implements ICapabilityProvider
    {
        @CapabilityInject(ILivingCapability.class)
        public static final Capability<ILivingCapability> LIVING_CAPABILITY = null;

        private final LazyOptional<ILivingCapability> instance = LazyOptional.of(LIVING_CAPABILITY::getDefaultInstance);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == LIVING_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }
}
