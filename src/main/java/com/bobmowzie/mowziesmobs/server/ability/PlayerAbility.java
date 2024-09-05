package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class PlayerAbility extends Ability<Player> {
    protected RawAnimation activeFirstPersonAnimation;

    protected ItemStack heldItemMainHandVisualOverride;
    protected ItemStack heldItemOffHandVisualOverride;

    public enum HandDisplay {
        DEFAULT,
        DONT_RENDER,
        FORCE_RENDER
    }

    protected HandDisplay firstPersonMainHandDisplay;
    protected HandDisplay firstPersonOffHandDisplay;

    private static RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    public PlayerAbility(AbilityType<Player, ? extends Ability> abilityType, Player user, AbilitySection[] sectionTrack, int cooldownMax) {
        super(abilityType, user, sectionTrack, cooldownMax);
        if (user.level().isClientSide) {
            this.activeAnimation = IDLE_ANIM;
            heldItemMainHandVisualOverride = null;
            heldItemOffHandVisualOverride = null;
            firstPersonMainHandDisplay = HandDisplay.DEFAULT;
            firstPersonOffHandDisplay = HandDisplay.DEFAULT;
        }
    }

    public PlayerAbility(AbilityType<Player, ? extends Ability> abilityType, Player user, AbilitySection[] sectionTrack) {
        this(abilityType, user, sectionTrack, 0);
    }

    public void playAnimation(RawAnimation animation, GeckoPlayer.Perspective perspective) {
        if (getUser() != null && getUser().level().isClientSide()) {
            if (perspective == GeckoPlayer.Perspective.FIRST_PERSON) {
                activeFirstPersonAnimation = animation;
            }
            else {
                activeAnimation = animation;
            }
            MowzieAnimationController<GeckoPlayer> controller = GeckoPlayer.getAnimationController(getUser(), perspective);
            GeckoPlayer geckoPlayer = GeckoPlayer.getGeckoPlayer(getUser(), perspective);
            if (controller != null && geckoPlayer != null) {
                controller.playAnimation(geckoPlayer, animation);
            }
        }
    }

    public void playAnimation(String animationName, GeckoPlayer.Perspective perspective, Animation.LoopType loopType) {
        playAnimation(RawAnimation.begin().then(animationName, loopType), perspective);
    }

        public void playAnimation(RawAnimation animation) {
        playAnimation(animation, GeckoPlayer.Perspective.FIRST_PERSON);
        playAnimation(animation, GeckoPlayer.Perspective.THIRD_PERSON);
    }

    @Override
    public void end() {
        super.end();
        if (getUser().level().isClientSide) {
            heldItemMainHandVisualOverride = null;
            heldItemOffHandVisualOverride = null;
            firstPersonMainHandDisplay = HandDisplay.DEFAULT;
            firstPersonOffHandDisplay = HandDisplay.DEFAULT;
        }
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !getUser().isSpectator();
    }

    @Override
    protected boolean canContinueUsing() {
        return super.canContinueUsing() && !getUser().isSpectator();
    }

    public <E extends GeoEntity> PlayState animationPredicate(AnimationState<E> e, GeckoPlayer.Perspective perspective) {
        RawAnimation whichAnimation;
        if (perspective == GeckoPlayer.Perspective.FIRST_PERSON) {
            whichAnimation = activeFirstPersonAnimation;
        }
        else {
            whichAnimation = activeAnimation;
        }
        if (whichAnimation == null || whichAnimation.getAnimationStages().isEmpty())
            return PlayState.STOP;
        e.getController().setAnimation(whichAnimation);
        return PlayState.CONTINUE;
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack heldItemMainHandOverride() {
        return heldItemMainHandVisualOverride;
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack heldItemOffHandOverride() {
        return heldItemOffHandVisualOverride;
    }

    @OnlyIn(Dist.CLIENT)
    public HandDisplay getFirstPersonMainHandDisplay() {
        return firstPersonMainHandDisplay;
    }

    @OnlyIn(Dist.CLIENT)
    public HandDisplay getFirstPersonOffHandDisplay() {
        return firstPersonOffHandDisplay;
    }

    // Events
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {

    }

    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

    }

    public void onRightClickWithItem(PlayerInteractEvent.RightClickItem event) {

    }

    public void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {

    }

    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {

    }

    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {

    }

    public void onLeftClickEntity(AttackEntityEvent event) {

    }

    public void onTakeDamage(LivingHurtEvent event) {

    }

    public void onJump(LivingEvent.LivingJumpEvent event) {

    }

    public void onFall(LivingFallEvent event) {

    }

    public void onRightMouseDown(Player player) {

    }

    public void onLeftMouseDown(Player player) {

    }

    public void onRightMouseUp(Player player) {

    }

    public void onLeftMouseUp(Player player) {

    }

    public void onSneakDown(Player player) {

    }

    public void onSneakUp(Player player) {

    }
}
