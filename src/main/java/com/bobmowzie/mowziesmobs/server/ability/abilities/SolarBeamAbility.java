package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityInstance;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySolarBeam;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class SolarBeamAbility extends Ability<SolarBeamAbility.SolarBeamAbilityInstance> {
    public SolarBeamAbility() {
        super(new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 20),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.ACTIVE, 55),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 20)
        });
    }

    @Override
    protected void start(SolarBeamAbilityInstance abilityInstance) {
        super.start(abilityInstance);
        LivingEntity user = abilityInstance.getUser();
        if (!abilityInstance.getUser().world.isRemote()) {
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

            abilityInstance.solarBeam = solarBeam;
        }
    }

    @Override
    protected void end(SolarBeamAbilityInstance abilityInstance) {
        super.end(abilityInstance);
        if (abilityInstance.solarBeam != null) abilityInstance.solarBeam.remove();
    }

    @Override
    public boolean canUse(LivingEntity user) {
        if (user instanceof PlayerEntity && !((PlayerEntity)user).inventory.getCurrentItem().isEmpty()) return false;
        return user.isPotionActive(EffectHandler.SUNS_BLESSING);
    }

    @Override
    public SolarBeamAbilityInstance makeInstance(LivingEntity user) {
        return new SolarBeamAbilityInstance(this, user);
    }

    protected static class SolarBeamAbilityInstance extends AbilityInstance {
        protected EntitySolarBeam solarBeam;
        public SolarBeamAbilityInstance(SolarBeamAbility abilityType, LivingEntity user) {
            super(abilityType, user);
        }
    }
}
