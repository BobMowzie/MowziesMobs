package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nonnull;
import java.util.*;

public class AbilityCapability {

    public interface IAbilityCapability extends INBTSerializable<CompoundTag> {

        void activateAbility(LivingEntity entity, AbilityType<?> ability);

        void instanceAbilities(LivingEntity entity);

        void tick(LivingEntity entity);

        AbilityType<?>[] getAbilityTypesOnEntity(LivingEntity entity);

        Map<AbilityType<?>, Ability> getAbilityMap();

        Collection<Ability> getAbilities();

        Ability getActiveAbility();

        void setActiveAbility(Ability activeAbility);

        boolean attackingPrevented();

        boolean blockBreakingBuildingPrevented();

        boolean interactingPrevented();

        boolean itemUsePrevented(ItemStack itemStack);

         <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> e, GeckoPlayer.Perspective perspective);

        void codeAnimations(MowzieAnimatedGeoModel<? extends IAnimatable> model, float partialTick);
    }

    public static class AbilityCapabilityImp implements IAbilityCapability {
        SortedMap<AbilityType<?>, Ability> abilityInstances = new TreeMap<>();
        Ability activeAbility = null;
        Map<String, Tag> nbtMap = new HashMap<>();

        @Override
        public void instanceAbilities(LivingEntity entity) {
            setActiveAbility(null);
            for (AbilityType<?> abilityType : getAbilityTypesOnEntity(entity)) {
                Ability ability = abilityType.makeInstance(entity);
                abilityInstances.put(abilityType, ability);
                if (nbtMap.containsKey(abilityType.getName())) ability.readNBT(nbtMap.get(abilityType.getName()));
            }
        }

        @Override
        public void activateAbility(LivingEntity entity, AbilityType<?> abilityType) {
            Ability ability = abilityInstances.get(abilityType);
            if (ability != null) {
                boolean tryResult = ability.tryAbility();
                if (tryResult) ability.start();
            }
            else System.out.println("Ability " + abilityType.toString() + " does not exist on mob " + entity.getClass().getSimpleName());
        }

        @Override
        public void tick(LivingEntity entity) {
            for (Ability ability : abilityInstances.values()) {
                ability.tick();
            }
        }

        @Override
        public AbilityType<?>[] getAbilityTypesOnEntity(LivingEntity entity) {
            if (entity instanceof Player) {
                return AbilityHandler.PLAYER_ABILITIES;
            }
            return new AbilityType[0];
        }

        @Override
        public Map<AbilityType<?>, Ability> getAbilityMap() {
            return abilityInstances;
        }

        @Override
        public Ability getActiveAbility() {
            return activeAbility;
        }

        @Override
        public void setActiveAbility(Ability activeAbility) {
            if (getActiveAbility() != null && getActiveAbility().isUsing()) getActiveAbility().interrupt();
            this.activeAbility = activeAbility;
        }

        @Override
        public Collection<Ability> getAbilities() {
            return abilityInstances.values();
        }

        @Override
        public boolean attackingPrevented() {
            return getActiveAbility() != null && getActiveAbility().preventsAttacking();
        }

        @Override
        public boolean blockBreakingBuildingPrevented() {
            return getActiveAbility() != null && getActiveAbility().preventsBlockBreakingBuilding();
        }

        @Override
        public boolean interactingPrevented() {
            return getActiveAbility() != null && getActiveAbility().preventsInteracting();
        }

        @Override
        public boolean itemUsePrevented(ItemStack itemStack) {
            return getActiveAbility() != null && getActiveAbility().preventsItemUse(itemStack);
        }

        @Override
        public <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> e, GeckoPlayer.Perspective perspective) {
            return getActiveAbility().animationPredicate(e, perspective);
        }

        public void codeAnimations(MowzieAnimatedGeoModel<? extends IAnimatable> model, float partialTick) {
            getActiveAbility().codeAnimations(model, partialTick);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compound = new CompoundTag();
            for (Map.Entry<AbilityType<?>, Ability> abilityEntry : getAbilityMap().entrySet()) {
                CompoundTag nbt = abilityEntry.getValue().writeNBT();
                if (!nbt.isEmpty()) {
                    compound.put(abilityEntry.getKey().getName(), nbt);
                }
            }
            return compound;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            CompoundTag compound = (CompoundTag) nbt;
            Set<String> keys = compound.getAllKeys();
            for (String abilityName : keys) {
                nbtMap.put(abilityName, compound.get(abilityName));
            }
        }
    }

    public static class AbilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
    {
        private final LazyOptional<IAbilityCapability> instance = LazyOptional.of(AbilityCapabilityImp::new);

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
            return CapabilityHandler.ABILITY_CAPABILITY.orEmpty(cap, instance.cast());
        }
    }
}
