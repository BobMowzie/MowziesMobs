package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBlockSwapper;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class TunnelingAbility extends Ability {
    private int doubleTapTimer = 0;
    public boolean prevUnderground;
    public BlockState justDug = Blocks.DIRT.getDefaultState();
    boolean underground = false;

    public TunnelingAbility(AbilityType<? extends Ability> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE)
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (doubleTapTimer > 0) doubleTapTimer--;
        if (isUsing()) {
            getUser().fallDistance = 0;
            if (getUser() instanceof PlayerEntity) ((PlayerEntity)getUser()).abilities.isFlying = false;
            boolean underground = !getUser().world.getEntitiesWithinAABB(EntityBlockSwapper.class, getUser().getBoundingBox().grow(0.2)).isEmpty();
            if (getUser().isOnGround() && !underground) end();
            Vector3d lookVec = getUser().getLookVec();
            float tunnelSpeed = 0.3f;
            if (underground) {
                if (getUser().isSneaking()) {
                    getUser().setMotion(lookVec.normalize().scale(tunnelSpeed));
                }
                else {
                    getUser().setMotion(lookVec.mul(0.3, 0, 0.3).add(0, 1, 0).normalize().scale(tunnelSpeed));
                }

                List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(getUser(),2, 2, 2, 2);
                for (LivingEntity entityHit : entitiesHit) {
                    DamageSource damageSource = DamageSource.causeMobDamage(getUser());
                    if (getUser() instanceof PlayerEntity) damageSource = DamageSource.causePlayerDamage((PlayerEntity) getUser());
                    entityHit.attackEntityFrom(damageSource, 6 * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.geomancyAttackMultiplier.get().floatValue());
                }
            }
            else getUser().setMotion(getUser().getMotion().subtract(0, 0.07, 0));


            if ((getUser().isSneaking() && lookVec.y < 0) || underground) {
                if (getUser().ticksExisted % 16 == 0) getUser().playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE.get(rand.nextInt(3)).get(), 0.6f, 0.5f + rand.nextFloat() * 0.2f);
                for (double x = -1; x <= 1; x++) {
                    for (double y = -1; y <= 2; y++) {
                        for (double z = -1; z <= 1; z++) {
                            if (Math.sqrt(x * x + y * y + z * z) > 1.75) continue;
                            BlockPos pos = new BlockPos(getUser().getPosX() + x + getUser().getMotion().getX(), getUser().getPosY() + y + getUser().getMotion().getY() + getUser().getHeight()/2f, getUser().getPosZ() + z + getUser().getMotion().getZ());
                            BlockState blockState = getUser().world.getBlockState(pos);
                            if (EffectGeomancy.isBlockDiggable(blockState) && blockState.getBlock() != Blocks.BEDROCK) {
                                justDug = blockState;
                                EntityBlockSwapper.swapBlock(getUser().world, pos, Blocks.AIR.getDefaultState(), 20, false, false);
                            }
                        }
                    }
                }
            }
            if (!prevUnderground && underground) {
                getUser().playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM.get(rand.nextInt(3)).get(), 1f, 0.9f + rand.nextFloat() * 0.1f);
                if (getUser().world.isRemote)
                    AdvancedParticleBase.spawnParticle(getUser().world, ParticleHandler.RING2.get(), (float) getUser().getPosX(), (float) getUser().getPosY() + 0.02f, (float) getUser().getPosZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f, 30f), false)
                    });
            }
            if (prevUnderground && !underground) {
                getUser().playSound(MMSounds.EFFECT_GEOMANCY_BREAK.get(), 1f, 0.9f + rand.nextFloat() * 0.1f);
                if (getUser().world.isRemote)
                    AdvancedParticleBase.spawnParticle(getUser().world, ParticleHandler.RING2.get(), (float) getUser().getPosX(), (float) getUser().getPosY() + 0.02f, (float) getUser().getPosZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                            new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f, 30f), false)
                    });
                getUser().setMotion(getUser().getMotion().scale(10f));

                for (int i = 0; i < 6; i++) {
                    if (justDug == null) justDug = Blocks.DIRT.getDefaultState();
//                        ParticleFallingBlock.spawnFallingBlock(getUser().world, getUser().getPosX(), getUser().getPosY() + 1, getUser().getPosZ(), 30f, 80, 1, getUser().getRNG().nextFloat() * 0.8f - 0.4f, 0.4f + getUser().getRNG().nextFloat() * 0.8f, getUser().getRNG().nextFloat() * 0.8f - 0.4f, ParticleFallingBlock.EnumScaleBehavior.CONSTANT, justDug);
                    EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK, getUser().world, 80, justDug);
                    fallingBlock.setPosition(getUser().getPosX(), getUser().getPosY() + 1, getUser().getPosZ());
                    fallingBlock.setMotion(getUser().getRNG().nextFloat() * 0.8f - 0.4f, 0.4f + getUser().getRNG().nextFloat() * 0.8f, getUser().getRNG().nextFloat() * 0.8f - 0.4f);
                    getUser().world.addEntity(fallingBlock);
                }
            }
            prevUnderground = underground;
        }
    }

    @Override
    public boolean canUse() {
        return EffectGeomancy.canUse(getUser()) && super.canUse();
    }

    @Override
    protected boolean canContinueUsing() {
        return EffectGeomancy.canUse(getUser()) && super.canContinueUsing();
    }

    @Override
    public void onSneakDown(PlayerEntity player) {
        super.onSneakDown(player);
        if (doubleTapTimer > 0 && !player.isOnGround()) {
            AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.TUNNELING_ABILITY);
        }
        doubleTapTimer = 9;
    }
}
