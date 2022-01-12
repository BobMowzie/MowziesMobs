package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.resources.Direction;
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

        void tick(LivingEntity entity);

        INBT writeNBT();

        void readNBT(INBT nbt);
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

        @Override
        public void tick(LivingEntity entity) {
//            if (!hasSunblock && entity.isPotionActive(EffectHandler.SUNBLOCK)) hasSunblock = true;
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

    public static class LivingStorage implements Capability.IStorage<ILivingCapability> {
        @Override
        public INBT writeNBT(Capability<LivingCapability.ILivingCapability> capability, LivingCapability.ILivingCapability instance, Direction side) {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<LivingCapability.ILivingCapability> capability, LivingCapability.ILivingCapability instance, Direction side, INBT nbt) {
            instance.readNBT(nbt);
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
