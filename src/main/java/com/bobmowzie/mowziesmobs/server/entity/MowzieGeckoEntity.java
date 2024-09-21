package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.dynamics.GeckoDynamicChain;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.abilities.player.SimpleAnimationAbility;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.ilexiconn.llibrary.server.event.AnimationEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.TestOnly;
import org.spongepowered.asm.mixin.Debug;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationProcessor;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Queue;

public abstract class MowzieGeckoEntity extends MowzieEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected MowzieAnimationController<MowzieGeckoEntity> controller = new MowzieAnimationController<>(this, "controller", 5, this::predicate, 0);

    public GeckoDynamicChain[] dynamicChains;

    public MowzieGeckoEntity(EntityType<? extends MowzieEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected int getDeathDuration() {
        Ability deathAbility = getActiveAbility();
        if (deathAbility instanceof SimpleAnimationAbility) return ((SimpleAnimationAbility) deathAbility).getDuration();
        return 20;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    public abstract AbilityType getHurtAbility();

    public abstract AbilityType getDeathAbility();


    @Override
    public boolean hurt(DamageSource source, float damage) {
        boolean attack = super.hurt(source, damage);
        if (attack) {
            if (getHealth() > 0.0F && (getActiveAbility() == null || getActiveAbility().damageInterrupts()) && playsHurtAnimation) {
                sendAbilityMessage(getHurtAbility());
            } else if (getHealth() <= 0.0F) {
                sendAbilityMessage(getDeathAbility());
            }
        }
        return attack;
    }

    protected <E extends GeoEntity> PlayState predicate(AnimationState<E> state) {
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability();
        FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(this, CapabilityHandler.FROZEN_CAPABILITY);
        if (abilityCapability == null) {
            return PlayState.STOP;
        }
        if (frozenCapability != null && frozenCapability.getFrozen()) {
            return PlayState.STOP;
        }

        if (abilityCapability.getActiveAbility() != null) {
            getController().transitionLength(0);
            return abilityCapability.animationPredicate(state, null);
        }
        else {
            loopingAnimations(state);
            return PlayState.CONTINUE;
        }
    }

    private static RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    protected <E extends GeoEntity> void loopingAnimations(AnimationState<E> event) {
        event.getController().setAnimation(IDLE_ANIM);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(controller);
    }

    public MowzieAnimationController<MowzieGeckoEntity> getController() {
        return controller;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public AbilityType<?, ?>[] getAbilities() {
        return new AbilityType[]{};
    }

    public AbilityCapability.IAbilityCapability getAbilityCapability() {
        return AbilityHandler.INSTANCE.getAbilityCapability(this);
    }

    public Ability getActiveAbility() {
        AbilityCapability.IAbilityCapability capability = getAbilityCapability();
        if (capability == null) return null;
        return getAbilityCapability().getActiveAbility();
    }

    public AbilityType getActiveAbilityType() {
        Ability ability = getActiveAbility();
        if (ability == null) return null;
        return ability.getAbilityType();
    }

    public Ability getAbility(AbilityType abilityType) {
        AbilityCapability.IAbilityCapability capability = getAbilityCapability();
        if (capability == null) return null;
        return getAbilityCapability().getAbilityMap().get(abilityType);
    }

    public void sendAbilityMessage(AbilityType abilityType) {
        AbilityHandler.INSTANCE.sendAbilityMessage(this, abilityType);
    }
}
