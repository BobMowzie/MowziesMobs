package com.bobmowzie.mowziesmobs.client.render.entity.player;

import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerFirstPerson;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelGeckoPlayerThirdPerson;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.entity.IAnimationTickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class GeckoPlayer implements GeoEntity {

	protected GeoRenderer<GeckoPlayer> renderer;
	protected MowzieGeoModel<GeckoPlayer> model;
	protected MowzieAnimationController<GeckoPlayer> controller;

	private int tickTimer = 0;

	private Player player;
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	public static final String THIRD_PERSON_CONTROLLER_NAME = "thirdPersonAnimation";
	public static final String FIRST_PERSON_CONTROLLER_NAME = "firstPersonAnimation";

	public enum Perspective {
		FIRST_PERSON,
		THIRD_PERSON
	}

	public GeckoPlayer(Player player) {
		this.player = player;
		setup(player);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controller = new MowzieAnimationController<>(this, getControllerName(), 0, this::predicate, 0);
		controllers.add(controller);
	}

	public MowzieAnimationController<GeckoPlayer> getController() {
		return controller;
	}

	public Player getPlayer() {
		return player;
	}

	public void tick() {
		tickTimer++;
	}

	@Override
	public double getTick(Object entity) {
		return ((GeckoPlayer)entity).tickTimer;
	}

	private static RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
	public <E extends GeoEntity> PlayState predicate(AnimationState<E> e) {
		e.getController().transitionLength(0);
		Player player = getPlayer();
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
			e.getController().setAnimation(IDLE_ANIMATION);
			return PlayState.CONTINUE;
		}
	}

	@Nullable
	public static GeckoPlayer getGeckoPlayer(Player player, Perspective perspective) {
		if (perspective == Perspective.FIRST_PERSON) return GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
		PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
		if (playerCapability != null) {
			return playerCapability.getGeckoPlayer();
		}
		return null;
	}

	public static MowzieAnimationController<GeckoPlayer> getAnimationController(Player player, Perspective perspective) {
		PlayerCapability.IPlayerCapability playerCapability = CapabilityHandler.getCapability(player, CapabilityHandler.PLAYER_CAPABILITY);
		if (playerCapability != null) {
			GeckoPlayer geckoPlayer;
			if (perspective == Perspective.FIRST_PERSON) geckoPlayer = GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
			else geckoPlayer = playerCapability.getGeckoPlayer();
			if (geckoPlayer != null) {
				return geckoPlayer.controller;
			}
		}
		return null;
	}

	public GeoRenderer<GeckoPlayer> getPlayerRenderer() {
		return renderer;
	}

	public MowzieGeoModel<GeckoPlayer> getModel() {
		return model;
	}

	public abstract String getControllerName();

	public abstract Perspective getPerspective();

	public abstract void setup(Player player);

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}

	public static class GeckoPlayerFirstPerson extends GeckoPlayer {
		public GeckoPlayerFirstPerson(Player player) {
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
		public void setup(Player player) {
			ModelGeckoPlayerFirstPerson modelGeckoPlayer = new ModelGeckoPlayerFirstPerson();
			model = modelGeckoPlayer;
			GeckoFirstPersonRenderer geckoRenderer = new GeckoFirstPersonRenderer(Minecraft.getInstance(), modelGeckoPlayer);
			renderer = geckoRenderer;
			if (!geckoRenderer.getModelsToLoad().containsKey(this.getClass())) {
				geckoRenderer.getModelsToLoad().put(this.getClass(), geckoRenderer);
			}

			getAnimatableInstanceCache().getManagerForId(renderer.getInstanceId(this));
			getController().setLastModel(model);
			GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON = this;
		}
	}

	public static class GeckoPlayerThirdPerson extends GeckoPlayer {
		public static GeckoRenderPlayer GECKO_RENDERER_THIRD_PERSON_NORMAL;
		public static ModelGeckoPlayerThirdPerson GECKO_MODEL_THIRD_PERSON_NORMAL;
		public static GeckoRenderPlayer GECKO_RENDERER_THIRD_PERSON_SLIM;
		public static ModelGeckoPlayerThirdPerson GECKO_MODEL_THIRD_PERSON_SLIM;

		protected GeoRenderer<GeckoPlayer> rendererSlim;
		protected MowzieGeoModel<GeckoPlayer> modelSlim;

		public GeckoPlayerThirdPerson(Player player) {
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
		public void setup(Player player) {
			model = GECKO_MODEL_THIRD_PERSON_NORMAL;
			renderer = GECKO_RENDERER_THIRD_PERSON_NORMAL;
			modelSlim = GECKO_MODEL_THIRD_PERSON_SLIM;
			rendererSlim = GECKO_RENDERER_THIRD_PERSON_SLIM;

			getAnimatableInstanceCache().getManagerForId(renderer.getInstanceId(this));
			getController().setLastModel(model);
		}

		public boolean isPlayerSlim() {
			return ((AbstractClientPlayer) getPlayer()).getModelName().equals("slim");
		}

		@Override
		public MowzieGeoModel<GeckoPlayer> getModel() {
			return isPlayerSlim() ? modelSlim : model;
		}

		@Override
		public GeoRenderer<GeckoPlayer> getPlayerRenderer() {
			return isPlayerSlim() ? rendererSlim : renderer;
		}

		public static void initRenderer() {
			GECKO_MODEL_THIRD_PERSON_NORMAL = new ModelGeckoPlayerThirdPerson();
			GECKO_MODEL_THIRD_PERSON_SLIM = new ModelGeckoPlayerThirdPerson();
			GECKO_MODEL_THIRD_PERSON_SLIM.setUseSmallArms(true);

			Minecraft minecraft = Minecraft.getInstance();
			EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
			ItemRenderer itemRenderer = minecraft.getItemRenderer();
			ResourceManager resourceManager = minecraft.getResourceManager();
			EntityModelSet entityModelSet = minecraft.getEntityModels();
			Font font = minecraft.font;
			EntityRendererProvider.Context context = new EntityRendererProvider.Context(dispatcher, itemRenderer, minecraft.getBlockRenderer(), dispatcher.getItemInHandRenderer(), resourceManager, entityModelSet, font);
			GeckoRenderPlayer geckoRenderer = new GeckoRenderPlayer(context, false, GECKO_MODEL_THIRD_PERSON_NORMAL);
			if (!geckoRenderer.getModelsToLoad().containsKey(GeckoPlayer.GeckoPlayerThirdPerson.class)) {
				geckoRenderer.getModelsToLoad().put(GeckoPlayer.GeckoPlayerThirdPerson.class, geckoRenderer);
			}
			GECKO_RENDERER_THIRD_PERSON_NORMAL = geckoRenderer;

			GeckoRenderPlayer geckoRendererSlim = new GeckoRenderPlayer(context, true, GECKO_MODEL_THIRD_PERSON_SLIM);
			if (!geckoRendererSlim.getModelsToLoad().containsKey(GeckoPlayer.GeckoPlayerThirdPerson.class)) {
				geckoRendererSlim.getModelsToLoad().put(GeckoPlayer.GeckoPlayerThirdPerson.class, geckoRendererSlim);
			}
			GECKO_RENDERER_THIRD_PERSON_SLIM = geckoRendererSlim;
		}
	}
}