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
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

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

        boolean attackingPrevented();

        boolean blockBreakingBuildingPrevented();

        boolean interactingPrevented();

        boolean itemUsePrevented(ItemStack itemStack);

         <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> e, GeckoPlayer.Perspective perspective);

        void codeAnimations(MowzieAnimatedGeoModel<? extends IAnimatable> model, float partialTick);

        Tag writeNBT();

        void readNBT(Tag nbt);
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
        public Tag writeNBT() {
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
        public void readNBT(Tag nbt) {
            CompoundTag compound = (CompoundTag) nbt;
            Set<String> keys = compound.getAllKeys();
            for (String abilityName : keys) {
                nbtMap.put(abilityName, compound.get(abilityName));
            }
        }
    }

    public static class AbilityStorage implements Capability.IStorage<IAbilityCapability> {
        @Override
        public Tag writeNBT(Capability<AbilityCapability.IAbilityCapability> capability, AbilityCapability.IAbilityCapability instance, Direction side) {
            return instance.writeNBT();
        }

        @Override
        public void readNBT(Capability<AbilityCapability.IAbilityCapability> capability, AbilityCapability.IAbilityCapability instance, Direction side, Tag nbt) {
            instance.readNBT(nbt);
        }
    }

    public static class AbilityProvider implements ICapabilitySerializable<Tag>
    {
        @CapabilityInject(IAbilityCapability.class)
        public static final Capability<IAbilityCapability> ABILITY_CAPABILITY = null;

        private final LazyOptional<IAbilityCapability> instance = LazyOptional.of(ABILITY_CAPABILITY::getDefaultInstance);

        @Override
        public Tag serializeNBT() {
            return ABILITY_CAPABILITY.getStorage().writeNBT(ABILITY_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null);
        }

        @Override
        public void deserializeNBT(Tag nbt) {
            ABILITY_CAPABILITY.getStorage().readNBT(ABILITY_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")), null, nbt);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ABILITY_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }
}
