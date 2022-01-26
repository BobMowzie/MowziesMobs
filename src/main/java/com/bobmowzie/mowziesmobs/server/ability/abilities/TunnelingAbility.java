package com.bobmowzie.mowziesmobs.server.ability.abilities;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimatedGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
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
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.List;

public class TunnelingAbility extends Ability {
    private int doubleTapTimer = 0;
    public boolean prevUnderground;
    public BlockState justDug = Blocks.DIRT.getDefaultState();
    boolean underground = false;

    private float spinAmount = 0;
    private float pitch = 0;

    public TunnelingAbility(AbilityType<? extends Ability> abilityType, LivingEntity user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE)
        });
    }

    @Override
    public void tickNotUsing() {
        super.tickNotUsing();
        if (doubleTapTimer > 0) doubleTapTimer--;
    }

    @Override
    public void start() {
        super.start();
        underground = false;
        prevUnderground = false;
        if (getUser().world.isRemote()) {
            spinAmount = 0;
            pitch = 0;
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        getUser().fallDistance = 0;
        if (getUser() instanceof PlayerEntity) ((PlayerEntity)getUser()).abilities.isFlying = false;
        underground = !getUser().world.getEntitiesWithinAABB(EntityBlockSwapper.class, getUser().getBoundingBox().grow(1)).isEmpty();
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
        else {
            getUser().setMotion(getUser().getMotion().subtract(0, 0.07, 0));
            if (getUser().getMotion().getY() < -1.3) getUser().setMotion(getUser().getMotion().getX(), -1.3, getUser().getMotion().getZ());
        }

        if ((getUser().isSneaking() && getUser().getMotion().y < 0) || underground) {
            if (getUser().ticksExisted % 16 == 0) getUser().playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE.get(rand.nextInt(3)).get(), 0.6f, 0.5f + rand.nextFloat() * 0.2f);
            Vector3d userCenter = getUser().getPositionVec().add(0, getUser().getHeight() / 2f, 0);
            float radius = 2f;
            AxisAlignedBB aabb = new AxisAlignedBB(-radius, -radius, -radius, radius, radius, radius);
            aabb = aabb.offset(userCenter);
            for (int i = 0; i < getUser().getMotion().length() * 4; i++) {
                for (int x = (int) Math.floor(aabb.minX); x <= Math.floor(aabb.maxX); x++) {
                    for (int y = (int) Math.floor(aabb.minY); y <= Math.floor(aabb.maxY); y++) {
                        for (int z = (int) Math.floor(aabb.minZ); z <= Math.floor(aabb.maxZ); z++) {
                            Vector3d posVec = new Vector3d(x, y, z);
                            if (posVec.add(0.5, 0.5, 0.5).subtract(userCenter).lengthSquared() > radius * radius) continue;
                            Vector3d motionScaled = getUser().getMotion().normalize().scale(i);
                            posVec = posVec.add(motionScaled);
                            BlockPos pos = new BlockPos(posVec);
                            BlockState blockState = getUser().world.getBlockState(pos);
                            if (EffectGeomancy.isBlockDiggable(blockState) && blockState.getBlock() != Blocks.BEDROCK) {
                                justDug = blockState;
                                EntityBlockSwapper.swapBlock(getUser().world, pos, Blocks.AIR.getDefaultState(), 20, false, false);
                            }
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
                EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK.get(), getUser().world, 80, justDug);
                fallingBlock.setPosition(getUser().getPosX(), getUser().getPosY() + 1, getUser().getPosZ());
                fallingBlock.setMotion(getUser().getRNG().nextFloat() * 0.8f - 0.4f, 0.4f + getUser().getRNG().nextFloat() * 0.8f, getUser().getRNG().nextFloat() * 0.8f - 0.4f);
                getUser().world.addEntity(fallingBlock);
            }
        }
        prevUnderground = underground;
    }

    @Override
    public boolean canUse() {
        return EffectGeomancy.canUse(getUser()) && super.canUse();
    }

    @Override
    protected boolean canContinueUsing() {
        boolean canContinueUsing = EffectGeomancy.canUse(getUser()) && (!getUser().isOnGround() || underground) && super.canContinueUsing();
        return canContinueUsing;
    }

    @Override
    public void onSneakDown(PlayerEntity player) {
        super.onSneakDown(player);
        if (doubleTapTimer > 0 && !player.isOnGround()) {
            AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.TUNNELING_ABILITY);
        }
        doubleTapTimer = 9;
    }

    @Override
    public <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> e, GeckoPlayer.Perspective perspective) {
        e.getController().transitionLengthTicks = 4;
        if (perspective == GeckoPlayer.Perspective.THIRD_PERSON) {
            float yMotionThreshold = getUser() == Minecraft.getInstance().player ? 1 : 2;
            if (!underground && !getUser().isSneaking() && getUser().getMotion().getY() < yMotionThreshold) {
                e.getController().setAnimation(new AnimationBuilder().addAnimation("tunneling_fall", false));
            }
            else {
                e.getController().setAnimation(new AnimationBuilder().addAnimation("tunneling_drill", true));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void codeAnimations(MowzieAnimatedGeoModel<? extends IAnimatable> model, float partialTick) {
        super.codeAnimations(model, partialTick);
        float faceMotionController = 1f - model.getControllerValue("FaceVelocityController");
        Vector3d moveVec = getUser().getMotion().normalize();
        pitch = (float) MathHelper.lerp(0.3 * partialTick, pitch, moveVec.getY());
        MowzieGeoBone com = model.getMowzieBone("CenterOfMass");
        com.setRotationX((float) (-Math.PI/2f + Math.PI/2f * pitch) * faceMotionController);

        float spinSpeed = 0.35f;
        if (faceMotionController < 1 && spinAmount < Math.PI * 2f - 0.01 && spinAmount > 0.01) {
            float f = (float) ((Math.PI * 2f - spinAmount) / (Math.PI * 2f));
            f = (float) Math.pow(f, 0.5);
            spinAmount += partialTick * spinSpeed * f;
            if (spinAmount > Math.PI * 2f) {
                spinAmount = 0;
            }
        }
        else {
            spinAmount += faceMotionController * partialTick * spinSpeed;
            spinAmount = (float) (spinAmount % (Math.PI * 2));
        }
        MowzieGeoBone waist = model.getMowzieBone("Waist");
        waist.addRotationY(-spinAmount);
    }
}
