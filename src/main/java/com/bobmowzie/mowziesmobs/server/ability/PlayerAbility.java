package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.event.predicate.AnimationState;

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

    public PlayerAbility(AbilityType<Player, ? extends Ability> abilityType, Player user, AbilitySection[] sectionTrack, int cooldownMax) {
        super(abilityType, user, sectionTrack, cooldownMax);
        if (user.level.isClientSide) {
            this.activeAnimation = new RawAnimation().addAnimation("idle");
            heldItemMainHandVisualOverride = null;
            heldItemOffHandVisualOverride = null;
            firstPersonMainHandDisplay = HandDisplay.DEFAULT;
            firstPersonOffHandDisplay = HandDisplay.DEFAULT;
        }
    }

    public PlayerAbility(AbilityType<Player, ? extends Ability> abilityType, Player user, AbilitySection[] sectionTrack) {
        this(abilityType, user, sectionTrack, 0);
    }

    public void playAnimation(String animationName, GeckoPlayer.Perspective perspective, boolean shouldLoop) {
        if (getUser() != null && getUser().level.isClientSide()) {
            RawAnimation newActiveAnimation = new RawAnimation().addAnimation(animationName, shouldLoop);
            if (perspective == GeckoPlayer.Perspective.FIRST_PERSON) {
                activeFirstPersonAnimation = newActiveAnimation;
            }
            else {
                activeAnimation = newActiveAnimation;
            }
            MowzieAnimationController<GeckoPlayer> controller = GeckoPlayer.getAnimationController((Player) getUser(), perspective);
            GeckoPlayer geckoPlayer = GeckoPlayer.getGeckoPlayer((Player) getUser(), perspective);
            if (controller != null && geckoPlayer != null) {
                controller.playAnimation(geckoPlayer, newActiveAnimation);
            }
        }
    }

    public void playAnimation(String animationName, boolean shouldLoop) {
        playAnimation(animationName, GeckoPlayer.Perspective.FIRST_PERSON, shouldLoop);
        playAnimation(animationName, GeckoPlayer.Perspective.THIRD_PERSON, shouldLoop);
    }

    @Override
    public void end() {
        super.end();
        if (getUser().level.isClientSide) {
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
        if (whichAnimation == null || whichAnimation.getRawAnimationList().isEmpty())
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
