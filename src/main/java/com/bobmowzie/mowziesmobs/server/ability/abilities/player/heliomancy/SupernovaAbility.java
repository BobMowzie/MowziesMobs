package com.bobmowzie.mowziesmobs.server.ability.abilities.player.heliomancy;

import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.server.ability.*;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySuperNova;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SupernovaAbility extends PlayerAbility {
    private boolean leftClickDown;
    private boolean rightClickDown;
    private Vec3[] betweenHandsPos;

    public SupernovaAbility(AbilityType<Player, SupernovaAbility> abilityType, Player user) {
        super(abilityType, user, EntityUmvuthi.SupernovaAbility.SECTION_TRACK);
        betweenHandsPos = new Vec3[1];
    }

    @Override
    public void start() {
        super.start();
        getUser().playSound(MMSounds.ENTITY_SUPERNOVA_START.get(), 3f, 1f);
        playAnimation("supernova", false);

        MobEffectInstance sunsBlessingInstance = getUser().getEffect(EffectHandler.SUNS_BLESSING);
        if (sunsBlessingInstance != null) {
            getUser().removeEffect(EffectHandler.SUNS_BLESSING);
        }
    }

    @Override
    public boolean canUse() {
        if (getUser() != null && !(getUser().getInventory().getSelected().isEmpty())) return false;
        return getUser().hasEffect(EffectHandler.SUNS_BLESSING) && super.canUse();
    }

    @Override
    public void tickUsing() {
        super.tickUsing();

        if (getTicksInUse() < 84) {
            getUser().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 4, false, false));
        }

        if (getTicksInUse() == 30) {
            getUser().playSound(MMSounds.ENTITY_SUPERNOVA_BLACKHOLE.get(), 2f, 1.2f);
        }

        if (getTicksInUse() < 30) {
            List<LivingEntity> entities = getEntityLivingBaseNearby(getUser(), 16, 16, 16, 16);
            for (LivingEntity inRange : entities) {
                if (inRange instanceof Player && ((Player)inRange).getAbilities().invulnerable) continue;
                Vec3 diff = inRange.position().subtract(getUser().position().add(0, 3, 0));
                diff = diff.normalize().scale(0.03);
                inRange.setDeltaMovement(inRange.getDeltaMovement().subtract(diff));

                if (inRange.getY() < getUser().getY() + 3) inRange.setDeltaMovement(inRange.getDeltaMovement().add(0, 0.075, 0));
            }
        }

        if (getLevel().isClientSide) {
            GeckoPlayer geckoPlayer = GeckoPlayer.getGeckoPlayer(getUser(), GeckoPlayer.Perspective.THIRD_PERSON);
            if (geckoPlayer != null) {
                GeckoRenderPlayer renderPlayer = (GeckoRenderPlayer) geckoPlayer.getPlayerRenderer();
                if (renderPlayer.betweenHandsPos != null) {
                    betweenHandsPos[0] = getUser().position().add(renderPlayer.betweenHandsPos);
                }
            }
            EntityUmvuthi.SupernovaAbility.superNovaEffects(this, betweenHandsPos, getLevel());
        }
    }

    @Override
    protected void beginSection(AbilitySection section) {
        super.beginSection(section);
        if (section.sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            if (!getUser().level.isClientSide) {
                EntitySuperNova superNova = new EntitySuperNova(EntityHandler.SUPER_NOVA.get(), getUser().level, getUser(), getUser().getX(), getUser().getY() + getUser().getBbHeight()/2f, getUser().getZ());
                getUser().level.addFreshEntity(superNova);
            }
        }
    }

    @Override
    public void onLeftMouseDown(Player player) {
        super.onLeftMouseDown(player);
        if (player == getUser()) leftClickDown = true;
    }

    @Override
    public void onLeftMouseUp(Player player) {
        super.onLeftMouseUp(player);
        if (player == getUser()) leftClickDown = false;
    }

    @Override
    public void onRightMouseDown(Player player) {
        super.onRightMouseDown(player);
        if (player == getUser()) rightClickDown = true;
    }

    @Override
    public void onRightMouseUp(Player player) {
        super.onRightMouseUp(player);
        if (player == getUser()) rightClickDown = false;
    }

    @Override
    public void tick() {
        super.tick();
        if (getUser().isCrouching() && rightClickDown && leftClickDown) {
            AbilityHandler.INSTANCE.sendAbilityMessage(getUser(), AbilityHandler.SUPERNOVA_ABILITY);
        }
    }

    @Override
    public boolean canCancelActiveAbility() {
        Ability ability = getActiveAbility();
        return ability != null && (ability.getAbilityType() == AbilityHandler.SOLAR_FLARE_ABILITY || ability.getAbilityType() == AbilityHandler.SOLAR_BEAM_ABILITY) && ability.getTicksInUse() < 5;
    }
}
