package com.bobmowzie.mowziesmobs.server.ability.abilities.player;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoModel;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilitySection;
import com.bobmowzie.mowziesmobs.server.ability.AbilityType;
import com.bobmowzie.mowziesmobs.server.ability.PlayerAbility;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityBlockSwapper;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityFallingBlock;
import com.bobmowzie.mowziesmobs.server.item.ItemEarthboreGauntlet;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.List;

public class TunnelingAbility extends PlayerAbility {
    private int doubleTapTimer = 0;
    public boolean prevUnderground;
    public BlockState justDug = Blocks.DIRT.defaultBlockState();
    boolean underground = false;

    private float spinAmount = 0;
    private float pitch = 0;

    private int timeUnderground = 0;
    private int timeAboveGround = 0;

    private InteractionHand whichHand;
    private ItemStack gauntletStack;

    public TunnelingAbility(AbilityType<Player, ? extends Ability> abilityType, Player user) {
        super(abilityType, user, new AbilitySection[] {
                new AbilitySection.AbilitySectionInfinite(AbilitySection.AbilitySectionType.ACTIVE)
        });
    }

    @Override
    public void tickNotUsing() {
        super.tickNotUsing();
        if (doubleTapTimer > 0) doubleTapTimer--;
    }

    public void playGauntletAnimation() {
        if (getUser() != null) {
            if (gauntletStack != null && gauntletStack.getItem() == ItemHandler.EARTHBORE_GAUNTLET) {
                Player player = (Player) getUser();
                ItemHandler.EARTHBORE_GAUNTLET.playAnimation(player, gauntletStack, ItemEarthboreGauntlet.ANIM_OPEN);
            }
        }
    }

    public void stopGauntletAnimation() {
        if (getUser() != null) {
            if (gauntletStack != null && gauntletStack.getItem() == ItemHandler.EARTHBORE_GAUNTLET) {
                Player player = (Player) getUser();
                ItemHandler.EARTHBORE_GAUNTLET.playAnimation(player, gauntletStack, ItemEarthboreGauntlet.ANIM_REST);
            }
        }
    }

    @Override
    public void start() {
        super.start();
        underground = false;
        prevUnderground = false;
        if (getUser().isOnGround()) getUser().push(0, 0.8f, 0);
        whichHand = getUser().getUsedItemHand();
        gauntletStack = getUser().getUseItem();
        if (getUser().level.isClientSide()) {
            spinAmount = 0;
            pitch = 0;
        }
    }

    public boolean damageGauntlet() {
        ItemStack stack = getUser().getUseItem();
        if (stack.getItem() == ItemHandler.EARTHBORE_GAUNTLET) {
            InteractionHand handIn = getUser().getUsedItemHand();
            if (stack.getDamageValue() + 5 < stack.getMaxDamage()) {
                stack.hurtAndBreak(5, getUser(), p -> p.broadcastBreakEvent(handIn));
                return true;
            }
            else {
                if (ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.breakable.get()) {
                    stack.hurtAndBreak(5, getUser(), p -> p.broadcastBreakEvent(handIn));
                }
                return false;
            }
        }
        return false;
    }

    public void restoreGauntlet(ItemStack stack) {
        if (stack.getItem() == ItemHandler.EARTHBORE_GAUNTLET) {
            if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHBORE_GAUNTLET.breakable.get()) {
                stack.setDamageValue(Math.max(stack.getDamageValue() - 1, 0));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!isUsing() && getUser() instanceof Player) {
            Player player = (Player) getUser();
            for (ItemStack stack : player.getInventory().items) {
                restoreGauntlet(stack);
            }
            for (ItemStack stack : player.getInventory().offhand) {
                restoreGauntlet(stack);
            }
        }
    }

    @Override
    public void tickUsing() {
        super.tickUsing();
        getUser().fallDistance = 0;
        if (getUser() instanceof Player) ((Player)getUser()).getAbilities().flying = false;
        underground = !getUser().level.getEntitiesOfClass(EntityBlockSwapper.class, getUser().getBoundingBox().inflate(0.3)).isEmpty();
        Vec3 lookVec = getUser().getLookAngle();
        float tunnelSpeed = 0.3f;
        ItemStack stack = getUser().getUseItem();
        boolean usingGauntlet = stack.getItem() == ItemHandler.EARTHBORE_GAUNTLET;
        if (underground) {
            timeUnderground++;
            if (usingGauntlet && damageGauntlet()) {
                getUser().setDeltaMovement(lookVec.normalize().scale(tunnelSpeed));
            }
            else {
                getUser().setDeltaMovement(lookVec.multiply(0.3, 0, 0.3).add(0, 1, 0).normalize().scale(tunnelSpeed));
            }

            List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(getUser(),2, 2, 2, 2);
            for (LivingEntity entityHit : entitiesHit) {
                DamageSource damageSource = DamageSource.mobAttack(getUser());
                if (getUser() instanceof Player) damageSource = DamageSource.playerAttack((Player) getUser());
                entityHit.hurt(damageSource, 6 * ConfigHandler.COMMON.TOOLS_AND_ABILITIES.geomancyAttackMultiplier.get().floatValue());
            }
        }
        else {
            timeAboveGround++;
            getUser().setDeltaMovement(getUser().getDeltaMovement().subtract(0, 0.07, 0));
            if (getUser().getDeltaMovement().y() < -1.3) getUser().setDeltaMovement(getUser().getDeltaMovement().x(), -1.3, getUser().getDeltaMovement().z());
        }

        if ((underground && (prevUnderground || lookVec.y < 0) && timeAboveGround > 5) || (getTicksInUse() > 1 && usingGauntlet && lookVec.y < 0 && stack.getDamageValue() + 5 < stack.getMaxDamage())) {
            if (getUser().tickCount % 16 == 0) getUser().playSound(MMSounds.EFFECT_GEOMANCY_RUMBLE.get(rand.nextInt(3)).get(), 0.6f, 0.5f + rand.nextFloat() * 0.2f);
            Vec3 userCenter = getUser().position().add(0, getUser().getBbHeight() / 2f, 0);
            float radius = 2f;
            AABB aabb = new AABB(-radius, -radius, -radius, radius, radius, radius);
            aabb = aabb.move(userCenter);
            for (int i = 0; i < getUser().getDeltaMovement().length() * 4; i++) {
                for (int x = (int) Math.floor(aabb.minX); x <= Math.floor(aabb.maxX); x++) {
                    for (int y = (int) Math.floor(aabb.minY); y <= Math.floor(aabb.maxY); y++) {
                        for (int z = (int) Math.floor(aabb.minZ); z <= Math.floor(aabb.maxZ); z++) {
                            Vec3 posVec = new Vec3(x, y, z);
                            if (posVec.add(0.5, 0.5, 0.5).subtract(userCenter).lengthSqr() > radius * radius) continue;
                            Vec3 motionScaled = getUser().getDeltaMovement().normalize().scale(i);
                            posVec = posVec.add(motionScaled);
                            BlockPos pos = new BlockPos(posVec);
                            BlockState blockState = getUser().level.getBlockState(pos);
                            if (EffectGeomancy.isBlockDiggable(blockState) && blockState.getBlock() != Blocks.BEDROCK) {
                                justDug = blockState;
                                EntityBlockSwapper.swapBlock(getUser().level, pos, Blocks.AIR.defaultBlockState(), 15, false, false);
                            }
                        }
                    }
                }
            }
        }
        if (!prevUnderground && underground) {
            timeUnderground = 0;
            getUser().playSound(MMSounds.EFFECT_GEOMANCY_BREAK_MEDIUM.get(rand.nextInt(3)).get(), 1f, 0.9f + rand.nextFloat() * 0.1f);
            if (getUser().level.isClientSide)
                AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.RING2.get(), (float) getUser().getX(), (float) getUser().getY() + 0.02f, (float) getUser().getZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f, 30f), false)
                });
            playGauntletAnimation();
        }
        if (prevUnderground && !underground) {
            timeAboveGround = 0;
            getUser().playSound(MMSounds.EFFECT_GEOMANCY_BREAK.get(), 1f, 0.9f + rand.nextFloat() * 0.1f);
            if (getUser().level.isClientSide)
                AdvancedParticleBase.spawnParticle(getUser().level, ParticleHandler.RING2.get(), (float) getUser().getX(), (float) getUser().getY() + 0.02f, (float) getUser().getZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 3.5F, 0.83f, 1, 0.39f, 1, 1, 10, true, true, new ParticleComponent[]{
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                        new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(10f, 30f), false)
                });
            if (timeUnderground > 10)
                getUser().setDeltaMovement(getUser().getDeltaMovement().scale(10f));
            else
                getUser().setDeltaMovement(getUser().getDeltaMovement().multiply(3, 7, 3));

            for (int i = 0; i < 6; i++) {
                if (justDug == null) justDug = Blocks.DIRT.defaultBlockState();
                EntityFallingBlock fallingBlock = new EntityFallingBlock(EntityHandler.FALLING_BLOCK.get(), getUser().level, 80, justDug);
                fallingBlock.setPos(getUser().getX(), getUser().getY() + 1, getUser().getZ());
                fallingBlock.setDeltaMovement(getUser().getRandom().nextFloat() * 0.8f - 0.4f, 0.4f + getUser().getRandom().nextFloat() * 0.8f, getUser().getRandom().nextFloat() * 0.8f - 0.4f);
                getUser().level.addFreshEntity(fallingBlock);
            }
            stopGauntletAnimation();
        }
        prevUnderground = underground;
    }

    @Override
    public void end() {
        super.end();
        stopGauntletAnimation();
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    protected boolean canContinueUsing() {
        ItemStack stack = getUser().getUseItem();
        boolean usingGauntlet = stack.getItem() == ItemHandler.EARTHBORE_GAUNTLET;
        if (whichHand == null) return false;
        boolean canContinueUsing = (getTicksInUse() <= 1 || !(getUser().isOnGround() || (getUser().isInWater() && !usingGauntlet)) || underground) && getUser().getItemInHand(whichHand).getItem() == ItemHandler.EARTHBORE_GAUNTLET && super.canContinueUsing();
        return canContinueUsing;
    }

    @Override
    public boolean preventsItemUse(ItemStack stack) {
        if (stack.getItem() == ItemHandler.EARTHBORE_GAUNTLET) return false;
        return super.preventsItemUse(stack);
    }

    @Override
    public <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> e, GeckoPlayer.Perspective perspective) {
        e.getController().transitionLengthTicks = 4;
        if (perspective == GeckoPlayer.Perspective.THIRD_PERSON) {
            float yMotionThreshold = getUser() == Minecraft.getInstance().player ? 1 : 2;
            if (!underground && getUser().getUseItem().getItem() != ItemHandler.EARTHBORE_GAUNTLET && getUser().getDeltaMovement().y() < yMotionThreshold) {
                e.getController().setAnimation(new AnimationBuilder().addAnimation("tunneling_fall", false));
            }
            else {
                e.getController().setAnimation(new AnimationBuilder().addAnimation("tunneling_drill", true));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void codeAnimations(MowzieGeoModel<? extends IAnimatable> model, float partialTick) {
        super.codeAnimations(model, partialTick);
        float faceMotionController = 1f - model.getControllerValueInverted("FaceVelocityController");
        Vec3 moveVec = getUser().getDeltaMovement().normalize();
        pitch = (float) Mth.lerp(0.3 * partialTick, pitch, moveVec.y());
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

    @Override
    public CompoundTag writeNBT() {
        CompoundTag compound = super.writeNBT();
        if (isUsing() && whichHand != null) {
            compound.putInt("whichHand", whichHand.ordinal());
        }
        return compound;
    }

    @Override
    public void readNBT(Tag nbt) {
        super.readNBT(nbt);
        if (isUsing()) {
            CompoundTag compound = (CompoundTag) nbt;
            whichHand = InteractionHand.values()[compound.getInt("whichHand")];
        }
    }
}
