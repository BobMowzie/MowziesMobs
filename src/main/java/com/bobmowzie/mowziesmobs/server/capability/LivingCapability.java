package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class LivingCapability {
    public static ResourceLocation ID = new ResourceLocation(MowziesMobs.MODID, "living_cap");

    public interface ILivingCapability extends INBTSerializable<CompoundTag> {
        void setLastDamage(float damage);
        float getLastDamage();
        void setHasSunblock(boolean hasSunblock);
        boolean getHasSunblock();

        void tick(LivingEntity entity);
    }

    public static class LivingCapabilityImp implements ILivingCapability {
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
        public CompoundTag serializeNBT() {
            return new CompoundTag();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
        }
    }
    
    public static class LivingProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
    {
        private final LazyOptional<LivingCapability.ILivingCapability> instance = LazyOptional.of(LivingCapabilityImp::new);

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(NullPointerException::new).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
            return CapabilityHandler.LIVING_CAPABILITY.orEmpty(cap, instance.cast());
        }
    }
}
