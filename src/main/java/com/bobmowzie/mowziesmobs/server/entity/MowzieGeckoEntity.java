package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class MowzieGeckoEntity extends MowzieEntity implements IAnimatable, IAnimationTickable {

    protected AnimationFactory factory = new AnimationFactory(this);
    protected MowzieAnimationController<MowzieGeckoEntity> controller = new MowzieAnimationController<>(this, "controller", 10, this::predicate);

    public MowzieGeckoEntity(EntityType<? extends MowzieEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected int getDeathDuration() {
        return 20;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    public abstract AbilityType getHurtAbility();

    public abstract AbilityType getDeathAbility();

    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        getController().transitionLengthTicks = 0;
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability();
        if (abilityCapability == null) {
            return PlayState.STOP;
        }

        if (abilityCapability.getActiveAbility() != null) {
            return abilityCapability.animationPredicate(event, null);
        }
        else {
            loopingAnimations(event);
            return PlayState.CONTINUE;
        }
    }

    protected <E extends IAnimatable> void loopingAnimations(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(controller);
    }

    public MowzieAnimationController<MowzieGeckoEntity> getController() {
        return controller;
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
