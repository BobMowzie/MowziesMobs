package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerFirstPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.entity.IAnimationTickable;
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
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class GeckoPlayer implements IAnimatable, IAnimationTickable {

	protected IGeoRenderer<GeckoPlayer> renderer;
	protected MowzieAnimatedGeoModel<GeckoPlayer> model;

	private int tickTimer = 0;

	private PlayerEntity player;
	private AnimationFactory factory = new AnimationFactory(this);
	public static final String THIRD_PERSON_CONTROLLER_NAME = "thirdPersonAnimation";
	public static final String FIRST_PERSON_CONTROLLER_NAME = "firstPersonAnimation";

	public enum Perspective {
		FIRST_PERSON,
		THIRD_PERSON
	}

	public GeckoPlayer(PlayerEntity player) {
		this.player = player;
		setup(player);
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new MowzieAnimationController<>(this, getControllerName(), 0, this::predicate));
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
		e.getController().transitionLengthTicks = 0;
		PlayerEntity player = getPlayer();
		if (player == null) {
			return PlayState.STOP;
		}
		AbilityCapability.IAbilityCapability abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(player);
		if (abilityCapability == null) {
			return PlayState.STOP;
		}

		if (abilityCapability.getActiveAbility() != null) {
			return abilityCapability.animationPredicate(e, getPerspective());
		}
		else {
			e.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
			return PlayState.CONTINUE;
		}
	}

	@Nullable
	public static GeckoPlayer getGeckoPlayer(PlayerEntity player, Perspective perspective) {
		if (perspective == Perspective.FIRST_PERSON) return GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
		PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
		if (playerCapability != null) {
			return playerCapability.getGeckoPlayer();
		}
		return null;
	}

	public static MowzieAnimationController<GeckoPlayer> getAnimationController(PlayerEntity player, Perspective perspective) {
		PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
		if (playerCapability != null) {
			GeckoPlayer geckoPlayer;
			if (perspective == Perspective.FIRST_PERSON) geckoPlayer = GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
			else geckoPlayer = playerCapability.getGeckoPlayer();
			if (geckoPlayer != null) {
				String name = perspective == Perspective.FIRST_PERSON ? FIRST_PERSON_CONTROLLER_NAME : THIRD_PERSON_CONTROLLER_NAME;
				return (MowzieAnimationController<GeckoPlayer>) GeckoLibUtil.getControllerForID(geckoPlayer.getFactory(), player.getUniqueID().hashCode(), name);
			}
		}
		return null;
	}

	public IGeoRenderer<GeckoPlayer> getPlayerRenderer() {
		return renderer;
	}

	public MowzieAnimatedGeoModel<GeckoPlayer> getModel() {
		return model;
	}

	public abstract String getControllerName();

	public abstract Perspective getPerspective();

	public abstract void setup(PlayerEntity player);

	public static class GeckoPlayerFirstPerson extends GeckoPlayer {
		public GeckoPlayerFirstPerson(PlayerEntity player) {
			super(player);
		}

		@Override
		public String getControllerName() {
			return FIRST_PERSON_CONTROLLER_NAME;
		}

		@Override
		public Perspective getPerspective() {
			return Perspective.FIRST_PERSON;
		}

		@Override
		public void setup(PlayerEntity player) {
			ModelGeckoPlayerFirstPerson modelGeckoPlayer = new ModelGeckoPlayerFirstPerson();
			model = modelGeckoPlayer;
			model.resourceForModelId((AbstractClientPlayerEntity) player);
			GeckoFirstPersonRenderer geckoRenderer = new GeckoFirstPersonRenderer(Minecraft.getInstance(), modelGeckoPlayer);
			renderer = geckoRenderer;
			if (!geckoRenderer.getModelsToLoad().containsKey(this.getClass())) {
				geckoRenderer.getModelsToLoad().put(this.getClass(), geckoRenderer);
			}
		}
	}

	public static class GeckoPlayerThirdPerson extends GeckoPlayer {
		public GeckoPlayerThirdPerson(PlayerEntity player) {
			super(player);
		}

		@Override
		public String getControllerName() {
			return THIRD_PERSON_CONTROLLER_NAME;
		}

		@Override
		public Perspective getPerspective() {
			return Perspective.THIRD_PERSON;
		}

		@Override
		public void setup(PlayerEntity player) {
			ModelGeckoPlayerThirdPerson modelGeckoPlayer = new ModelGeckoPlayerThirdPerson();
			model = modelGeckoPlayer;
			model.resourceForModelId((AbstractClientPlayerEntity) player);
			GeckoRenderPlayer geckoRenderer = new GeckoRenderPlayer(Minecraft.getInstance().getRenderManager(), modelGeckoPlayer);
			renderer = geckoRenderer;
			if (!geckoRenderer.getModelsToLoad().containsKey(this.getClass())) {
				geckoRenderer.getModelsToLoad().put(this.getClass(), geckoRenderer);
			}
		}
	}
}