package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayer;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.render.entity.RenderPlayerAnimated;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class GeckoPlayer implements IAnimatable, IAnimationTickable {

	private RenderPlayerAnimated renderPlayerAnimated;
	ModelGeckoPlayer geckoPlayerModel;

	private int tickTimer = 0;

	private PlayerEntity player;
	private AnimationFactory factory = new AnimationFactory(this);
	public static final String CONTROLLER_NAME = "activeAnimation";

	public GeckoPlayer(PlayerEntity player) {
		this.player = player;
		geckoPlayerModel = new ModelGeckoPlayer();
		geckoPlayerModel.resourceForModelId((AbstractClientPlayerEntity) player);
		renderPlayerAnimated = new RenderPlayerAnimated(Minecraft.getInstance().getRenderManager(), geckoPlayerModel, ((AbstractClientPlayerEntity) player).getSkinType().equals("slim"));
		if (!renderPlayerAnimated.getModelsToLoad().containsKey(this.getClass())) {
			renderPlayerAnimated.getModelsToLoad().put(this.getClass(), renderPlayerAnimated);
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new MowzieAnimationController<>(this, CONTROLLER_NAME, 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	@Override
	public void tick() {
		tickTimer++;
	}

	@Override
	public int tickTimer() {
		return tickTimer;
	}

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> e) {
		PlayerEntity player = getPlayer();
		if (player == null) {
			return PlayState.STOP;
		}
		AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
		if (abilityCapability == null) {
			return PlayState.STOP;
		}

		if (abilityCapability.getActiveAbility() != null) {
			return abilityCapability.animationPredicate(e);
		}
		else {
			e.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
			return PlayState.CONTINUE;
		}
	}

	@Nullable
	public static GeckoPlayer getGeckoPlayer(PlayerEntity player) {
		PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
		if (playerCapability != null) {
			return playerCapability.getGeckoPlayer();
		}
		return null;
	}

	public static MowzieAnimationController<GeckoPlayer> getAnimationController(PlayerEntity player) {
		PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
		if (playerCapability != null) {
			GeckoPlayer geckoPlayer = playerCapability.getGeckoPlayer();
			if (geckoPlayer != null) {
				return (MowzieAnimationController<GeckoPlayer>) GeckoLibUtil.getControllerForID(geckoPlayer.getFactory(), player.getUniqueID().hashCode(), GeckoPlayer.CONTROLLER_NAME);
			}
		}
		return null;
	}

	public RenderPlayerAnimated getPlayerRenderer() {
		return renderPlayerAnimated;
	}

	public ModelGeckoPlayer getGeckoPlayerModel() {
		return geckoPlayerModel;
	}
}