package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class SolarBeamAbility extends Ability {
    protected EntitySolarBeam solarBeam;

    public SolarBeamAbility(AbilityType<SolarBeamAbility> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 20),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 55),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 20)
        });
    }

    @Override
    public void start() {
        super.start();
        LivingEntity user = getUser();
        if (!getUser().world.isRemote()) {
            EntitySolarBeam solarBeam = new EntitySolarBeam(EntityHandler.SOLAR_BEAM, user.world, user, user.getPosX(), user.getPosY() + 1.2f, user.getPosZ(), (float) ((user.rotationYawHead + 90) * Math.PI / 180), (float) (-user.rotationPitch * Math.PI / 180), 55);
            solarBeam.setHasPlayer(true);
            user.world.addEntity(solarBeam);
            user.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 80, 2, false, false));
            EffectInstance sunsBlessingInstance = user.getActivePotionEffect(EffectHandler.SUNS_BLESSING);
            if (sunsBlessingInstance != null) {
                int duration = sunsBlessingInstance.getDuration();
                user.removePotionEffect(EffectHandler.SUNS_BLESSING);
                int solarBeamCost = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SUNS_BLESSING.solarBeamCost.get() * 60 * 20;
                if (duration - solarBeamCost > 0) {
                    user.addPotionEffect(new EffectInstance(EffectHandler.SUNS_BLESSING, duration - solarBeamCost, 0, false, false));
                }
            }

            this.solarBeam = solarBeam;
        }
        playAnimation("solar_beam_charge");
    }

    @Override
    public void end() {
        super.end();
        if (solarBeam != null) solarBeam.remove();
    }

    @Override
    public boolean canUse() {
        if (getUser() instanceof PlayerEntity && !((PlayerEntity)getUser()).inventory.getCurrentItem().isEmpty()) return false;
        return getUser().isPotionActive(EffectHandler.SUNS_BLESSING) && super.canUse();
    }

    @Override
    public <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> e) {
        e.getController().setAnimation(new AnimationBuilder().addAnimation("solar_beam_charge", false));
        return PlayState.CONTINUE;
    }
}
