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

	private Player player;
	private AnimationFactory factory = new AnimationFactory(this);
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
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new MowzieAnimationController<>(this, getControllerName(), 0, this::predicate, 0));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public Player getPlayer() {
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
			e.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
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
				String name = perspective == Perspective.FIRST_PERSON ? FIRST_PERSON_CONTROLLER_NAME : THIRD_PERSON_CONTROLLER_NAME;
				return (MowzieAnimationController<GeckoPlayer>) GeckoLibUtil.getControllerForID(geckoPlayer.getFactory(), player.getUUID().hashCode(), name);
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

	public abstract void setup(Player player);

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
		}
	}

	public static class GeckoPlayerThirdPerson extends GeckoPlayer {
		public static GeckoRenderPlayer GECKO_RENDERER_THIRD_PERSON_NORMAL;
		public static ModelGeckoPlayerThirdPerson GECKO_MODEL_THIRD_PERSON_NORMAL;
		public static GeckoRenderPlayer GECKO_RENDERER_THIRD_PERSON_SLIM;
		public static ModelGeckoPlayerThirdPerson GECKO_MODEL_THIRD_PERSON_SLIM;

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
			if (((AbstractClientPlayer) player).getModelName().equals("slim")) {
				model = GECKO_MODEL_THIRD_PERSON_SLIM;
				renderer = GECKO_RENDERER_THIRD_PERSON_SLIM;
			}
			else {
				model = GECKO_MODEL_THIRD_PERSON_NORMAL;
				renderer = GECKO_RENDERER_THIRD_PERSON_NORMAL;
			}
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
			EntityRendererProvider.Context context = new EntityRendererProvider.Context(dispatcher, itemRenderer, resourceManager, entityModelSet, font);
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