package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.UUID;

public class GeckoPlayer implements IAnimatable {

	private UUID uuid;
	private AnimationFactory factory = new AnimationFactory(this);

	public GeckoPlayer(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 3, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public PlayerEntity getPlayer() {
		World world = Minecraft.getInstance().world;
		if (world != null) {
			return world.getPlayerByUuid(uuid);
		}
		return null;
	}

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> e) {
		PlayerEntity player = getPlayer();
		if (player != null) {
			e.getController().setAnimation(new AnimationBuilder().addAnimation("spawn_boulder"));
		}
		return PlayState.CONTINUE;
	}
}