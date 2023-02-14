package com.bobmowzie.mowziesmobs.server.ability.abilities.geomancy;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.*;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class BoulderRollAbility extends PlayerAbility {
    private static int START_UP = 15;
    float spinAmount = 0;

    public BoulderRollAbility(AbilityType<Player, ? extends Ability> abilityType, Player user) {
        super(abilityType, user,  new AbilitySection[] {
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE)
        });
    }



    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
    }

    @Override
    public <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> e, GeckoPlayer.Perspective perspective) {
        e.getController().transitionLengthTicks = 0;
        if (perspective == GeckoPlayer.Perspective.THIRD_PERSON) {
            e.getController().setAnimation(new AnimationBuilder().addAnimation("boulder_roll_loop_still", true));
        }
        return PlayState.CONTINUE;
    }


    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            getUser().setDeltaMovement(getUser().getViewVector(1f).normalize().multiply(0.3d,0d,0.3d));
        }
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            //playAnimation("boulder_roll_loop", true);
            getUser().setDeltaMovement(getUser().getViewVector(1f).normalize().multiply(1d,0d,1d));
        }
    }

     @Override
    public boolean tryAbility() {
        return super.tryAbility();
    }

    @Override
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        super.onRightClickEmpty(event);
        AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getPlayer(), AbilityHandler.BOULDER_ROLL_ABILITY);
    }

    @Override
    public void onRightMouseUp(Player player) {
        super.onRightMouseUp(player);
        nextSection();
    }

    @Override
    public boolean canUse() {
        if (getUser() instanceof Player && !((Player)getUser()).getInventory().getSelected().isEmpty()) return false;
        return getUser().hasEffect(EffectHandler.GEOMANCY) && getUser().isSprinting() && super.canUse();
    }

    @Override
    public void codeAnimations(MowzieAnimatedGeoModel<? extends IAnimatable> model, float partialTick) {
        super.codeAnimations(model, partialTick);
        float spinSpeed = 0.35f;
        spinAmount += partialTick * spinSpeed;
        MowzieGeoBone centerOfMass = model.getMowzieBone("CenterOfMass");
        centerOfMass.addRotationX(-spinAmount);
    }
}
