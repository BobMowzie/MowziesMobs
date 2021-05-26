package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.ai.NearestAttackableTargetPredicateGoal;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityBarakoanToPlayer extends EntityBarakoan<PlayerEntity> {
    private static final DataParameter<ItemStack> MASK = EntityDataManager.createKey(EntityBarakoanToPlayer.class, DataSerializers.ITEMSTACK);

    public EntityBarakoanToPlayer(EntityType<? extends EntityBarakoanToPlayer> type, World world) {
        this(type, world, null);
    }

    public EntityBarakoanToPlayer(EntityType<? extends EntityBarakoanToPlayer> type, World world, PlayerEntity leader) {
        super(type, world, PlayerEntity.class, leader);
        experienceValue = 0;
    }

    @Override
    protected void registerTargetGoals() {
        super.registerTargetGoals();
        if (getMask() == MaskType.FAITH) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetPredicateGoal<PlayerEntity>(this, PlayerEntity.class, 0, true, true, (new EntityPredicate()).setDistance(getAttributeValue(Attributes.FOLLOW_RANGE)).setCustomPredicate(target -> {
                if (!active) return false;
                if (target != getLeader()) return false;
                return target.getLastAttackedEntity() != null || target.getCombatTracker().getBestAttacker() != null || target.getHealth() < target.getMaxHealth();
            }).allowFriendlyFire().allowInvulnerable().setSkipAttackChecks()) {
                @Override
                public boolean shouldContinueExecuting() {
                    LivingEntity livingentity = this.goalOwner.getAttackTarget();
                    if (livingentity == null) {
                        livingentity = this.target;
                    }
                    boolean targetHasTarget = target.getCombatTracker().getBestAttacker() != null;
                    boolean canHeal = true;
                    if (this.goalOwner instanceof EntityBarakoa) canHeal = ((EntityBarakoa)this.goalOwner).canHeal(livingentity);
                    return super.shouldContinueExecuting() && (livingentity.getHealth() < livingentity.getMaxHealth() || targetHasTarget) && canHeal;
                }

                @Override
                protected double getTargetDistance() {
                    return super.getTargetDistance() * 2;
                }

                @Override
                public void startExecuting() {
                    targetEntitySelector.setIgnoresLineOfSight().allowInvulnerable().allowFriendlyFire().setSkipAttackChecks();
                    super.startExecuting();
                }
            });
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(MASK, new ItemStack(ItemHandler.BARAKOA_MASK_FURY, 1));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (rand.nextFloat() < 0.5) return null;
        return super.getAmbientSound();
    }

    @Override
    public void tick() {
        if (getLeader() == null || getLeader().getHealth() <= 0) deactivate();
        super.tick();
    }

    @Override
    protected ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        if (playerIn == leader) {
            deactivate();
        }
        return super.getEntityInteractionResult(playerIn, hand);
    }

    private void deactivate() {
        if (getActive() && getAnimation() != DEACTIVATE_ANIMATION) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
            playSound(MMSounds.ENTITY_BARAKOA_RETRACT.get(), 1, 1);
        }
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MowzieEntity.createAttributes().createMutableAttribute(Attributes.ATTACK_DAMAGE, 7 * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.attackMultiplier.get())
                .createMutableAttribute(Attributes.MAX_HEALTH, 20 * ConfigHandler.COMMON.MOBS.BARAKOA.combatConfig.healthMultiplier.get());
    }

    @Override
    protected int getTribeCircleTick() {
        PlayerCapability.IPlayerCapability capability = getPlayerCapability();
        if (capability == null) return 0;
        return capability.getTribeCircleTick();
    }

    @Override
    protected int getPackSize() {
        PlayerCapability.IPlayerCapability capability = getPlayerCapability();
        if (capability == null) return 0;
        return capability.getPackSize();
    }

    @Override
    protected void addAsPackMember() {
        PlayerCapability.IPlayerCapability capability = getPlayerCapability();
        if (capability == null) return;
        capability.addPackMember(this);
    }

    @Override
    protected void removeAsPackMember() {
        PlayerCapability.IPlayerCapability capability = getPlayerCapability();
        if (capability == null) return;
        capability.removePackMember(this);
    }

    private PlayerCapability.IPlayerCapability getPlayerCapability() {
        return CapabilityHandler.getCapability(leader, PlayerCapability.PlayerProvider.PLAYER_CAPABILITY);
    }

    @Override
    public boolean isBarakoDevoted() {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return null;
    }

    @Nullable
    public UUID getOwnerId() {
        return getLeader() == null ? null : getLeader().getUniqueID();
    }

    @Nullable
    public Entity getOwner() {
        return leader;
    }

    public boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset)
    {
        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        BlockState iblockstate = this.world.getBlockState(blockpos);
        return iblockstate.canEntitySpawn(this.world, blockpos, this.getType()) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
    }

    public ItemStack getStoredMask() {
        return getDataManager().get(MASK);
    }

    public void setStoredMask(ItemStack mask) {
        getDataManager().set(MASK, mask);
    }

    @Override
    protected ItemStack getDeactivatedMask(ItemBarakoaMask mask) {
        return getStoredMask();
    }

    public boolean canHeal(LivingEntity entity) {
        return entity == leader;
    }

    @Override
    protected void updateAttackAI() {
        if (getMask() != MaskType.FAITH) super.updateAttackAI();
    }
}
