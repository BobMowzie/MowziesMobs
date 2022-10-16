package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class MowzieGeckoEntity extends MowzieEntity implements IAnimatable, IAnimationTickable {
    protected AnimationFactory factory = new AnimationFactory(this);
    protected String currentAnimation;

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
    public void tick() {
        super.tick();

    }

    @Override
    protected int getDeathDuration() {
        return 0;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(currentAnimation, true));
        return PlayState.CONTINUE;
    }
}
