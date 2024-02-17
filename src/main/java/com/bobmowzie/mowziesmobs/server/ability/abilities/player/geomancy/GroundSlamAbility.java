package com.bobmowzie.mowziesmobs.server.ability.abilities.player.geomancy;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class GroundSlamAbility extends PlayerAbility {
    public GroundSlamAbility(AbilityType<Player, ? extends Ability> abilityType, Player user) {
        super(abilityType, user,  new AbilitySection[] {
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.STARTUP, 2),
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE),
                new AbilitySection.AbilitySectionDuration(AbilitySection.AbilitySectionType.RECOVERY, 21)
        });
    }

    @Override
    public void start() {
        super.start();
        playAnimation("ground_pound_loop", true);

    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.STARTUP) {
            //getUser().setDeltaMovement(0d,0d,0d);
        }
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            getUser().setDeltaMovement(0d,-1.5d,0d);
            if(getUser().isOnGround()){
                nextSection();
                for(LivingEntity livingentity : getUser().level.getEntitiesOfClass(LivingEntity.class, getUser().getBoundingBox().inflate(5.2D, 2.0D, 5.2D))) {
                    livingentity.hurt(DamageSource.mobAttack(getUser()),10f);
                }

                EntityCameraShake.cameraShake(getUser().level, getUser().position(), 45, 0.09f, 20, 20);

                BlockState blockBeneath = getUser().level.getBlockState(getUser().blockPosition());
                if (getUser().level.isClientSide) {
                    getUser().playSound(SoundEvents.GENERIC_EXPLODE, 1.5f, 1.0f);
                    for(int i = 0; i < 50;i++ ){
                        getUser().level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockBeneath), getUser().getRandomX(5.8D), getUser().getBlockY() + 0.1f, getUser().getRandomZ(5.8D), 0, 0.38d,0);
                        getUser().level.addParticle(ParticleTypes.POOF, getUser().getRandomX(5f), getUser().getY(), getUser().getRandomZ(5f),0d,0.08d,0d);
                    }
                    AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.RING2.get(), (float) getUser().getX(), (float) getUser().getY() + 0.01f, (float) getUser().getZ(), 0, 0, 0, false, 0, Math.PI / 2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(0.8f, 0.0f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0, (0.8f + 2.7f * 20f / 60f) * 80f), false)
                    });
                }
            }
        }
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
            //playAnimation("ground_pound_land", true);
            getUser().setDeltaMovement(0d,0d,0d);
        }
    }

    @Override
    public boolean canUse() {
        if (getUser() instanceof Player && !((Player)getUser()).getInventory().getSelected().isEmpty()) return false;
        return EffectGeomancy.canUse(getUser()) && getUser().fallDistance > 2 &&super.canUse();
    }

    @Override
    public void nextSection() {
        super.nextSection();
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.ACTIVE) {
            //playAnimation("ground_pound_loop", true);
        }
        if (getCurrentSection().sectionType == AbilitySection.AbilitySectionType.RECOVERY) {
            playAnimation("ground_pound_land", false);
        }
    }

    @Override
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        super.onRightClickEmpty(event);
        if (!getUser().isOnGround() && getUser().isCrouching()){
            AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(event.getEntity(), AbilityHandler.GROUND_SLAM_ABILITY);
        }
    }
}
