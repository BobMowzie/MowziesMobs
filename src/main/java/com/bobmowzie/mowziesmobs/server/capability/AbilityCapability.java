package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class AbilityCapability {

    public interface IAbilityCapability {

        void activateAbility(LivingEntity entity, AbilityType<?> ability);

        void instanceAbilities(LivingEntity entity);

        void tick(LivingEntity entity);

        AbilityType<?>[] getAbilityTypesOnEntity(LivingEntity entity);

        Map<AbilityType<?>, Ability> getAbilityMap();

        Collection<Ability> getAbilities();

        Ability getActiveAbility();

        void setActiveAbility(Ability activeAbility);

        INBT writeNBT();

        void readNBT(INBT nbt);
    }

    public static class AbilityCapabilityImp implements IAbilityCapability {
        Map<AbilityType<?>, Ability> abilityInstances = new HashMap<>();
        Ability activeAbility = null;

        @Override
        public void instanceAbilities(LivingEntity entity) {
            for (AbilityType<?> ability : getAbilityTypesOnEntity(entity)) {
                abilityInstances.put(ability, ability.makeInstance(entity));
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
            if (entity instanceof PlayerEntity) {
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
            if (getActiveAbility() != null && getActiveAbility().isUsing()) getActiveAbility().end();
            this.activeAbility = activeAbility;
        }

        @Override
        public Collection<Ability> getAbilities() {
            return abilityInstances.values();
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

    public static class AbilityStorage implements Capability.IStorage<IAbilityCapability> {
        @Override
        public INBT writeNBT(Capability<AbilityCapability.IAbilityCapability> capability, AbilityCapability.IAbilityCapability instance, Direction side) {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<AbilityCapability.IAbilityCapability> capability, AbilityCapability.IAbilityCapability instance, Direction side, INBT nbt) {
            instance.readNBT(nbt);
        }
    }

    public static class AbilityProvider implements ICapabilityProvider
    {
        @CapabilityInject(IAbilityCapability.class)
        public static final Capability<IAbilityCapability> ABILITY_CAPABILITY = null;

        private final LazyOptional<IAbilityCapability> instance = LazyOptional.of(ABILITY_CAPABILITY::getDefaultInstance);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ABILITY_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }

    public static final class AbilityEventHandler {
        @SubscribeEvent
        public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
            PlayerEntity player = event.getPlayer();
            IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null) {
                for (Ability ability : abilityCapability.getAbilities()) {
                    ability.onRightClickEmpty(event);
                }
            }
        }

        @SubscribeEvent
        public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            PlayerEntity player = event.getPlayer();
            IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null) {
                for (Ability ability : abilityCapability.getAbilities()) {
                    ability.onRightClickBlock(event);
                }
            }
        }

        @SubscribeEvent
        public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
            PlayerEntity player = event.getPlayer();
            IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null) {
                for (Ability ability : abilityCapability.getAbilities()) {
                    ability.onRightClickWithItem(event);
                }
            }
        }

        @SubscribeEvent
        public void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
            PlayerEntity player = event.getPlayer();
            IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null) {
                for (Ability ability : abilityCapability.getAbilities()) {
                    ability.onRightClickEntity(event);
                }
            }
        }

        @SubscribeEvent
        public void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
            PlayerEntity player = event.getPlayer();
            IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null) {
                for (Ability ability : abilityCapability.getAbilities()) {
                    ability.onLeftClickEmpty(event);
                }
            }
        }

        @SubscribeEvent
        public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
            PlayerEntity player = event.getPlayer();
            IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null) {
                for (Ability ability : abilityCapability.getAbilities()) {
                    ability.onLeftClickBlock(event);
                }
            }
        }

        @SubscribeEvent
        public void onLeftClickEntity(AttackEntityEvent event) {
            PlayerEntity player = event.getPlayer();
            IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null) {
                for (Ability ability : abilityCapability.getAbilities()) {
                    ability.onLeftClickEntity(event);
                }
            }
        }

        @SubscribeEvent
        public void onTakeDamage(LivingHurtEvent event) {
            LivingEntity player = event.getEntityLiving();
            IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null) {
                for (Ability ability : abilityCapability.getAbilities()) {
                    ability.onTakeDamage(event);
                }
            }
        }

        @SubscribeEvent
        public void onJump(LivingEvent.LivingJumpEvent event) {
            LivingEntity player = event.getEntityLiving();
            IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (abilityCapability != null) {
                for (Ability ability : abilityCapability.getAbilities()) {
                    ability.onJump(event);
                }
            }
        }
    }
}
