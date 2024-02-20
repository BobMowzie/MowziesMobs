package com.bobmowzie.mowziesmobs.server.entity.umvuthana;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityUmvuthanaFollowerToPlayer extends EntityUmvuthanaFollower<Player> {
    private static final EntityDataAccessor<ItemStack> MASK_STORED = SynchedEntityData.defineId(EntityUmvuthanaFollowerToPlayer.class, EntityDataSerializers.ITEM_STACK);
    @OnlyIn(Dist.CLIENT)
    public Vec3[] feetPos;

    public EntityUmvuthanaFollowerToPlayer(EntityType<? extends EntityUmvuthanaFollowerToPlayer> type, Level world) {
        this(type, world, null);
    }

    public EntityUmvuthanaFollowerToPlayer(EntityType<? extends EntityUmvuthanaFollowerToPlayer> type, Level world, Player leader) {
        super(type, world, Player.class, leader);
        xpReward = 0;
        if (world.isClientSide) {
            feetPos = new Vec3[]{new Vec3(0, 0, 0)};
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(MASK_STORED, new ItemStack(ItemHandler.UMVUTHANA_MASK_FURY.get(), 1));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (random.nextFloat() < 0.5) return null;
        return super.getAmbientSound();
    }

    @Override
    public void tick() {
        if (tickCount > 30 && (getLeader() == null || getLeader().getHealth() <= 0)) {
            deactivate();
        }
        super.tick();
        if (level.isClientSide && feetPos != null && feetPos.length > 0 && active) {
            feetPos[0] = position().add(0, 0.05f, 0);
            if (tickCount % 10 == 0) AdvancedParticleBase.spawnParticle(level, ParticleHandler.RING2.get(), feetPos[0].x(), feetPos[0].y(), feetPos[0].z(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 1.5F, 1, 223 / 255f, 66 / 255f, 1, 1, 15, true, false, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(feetPos),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1f, 10f), false)
            });
        }
    }

    @Override
    protected InteractionResult mobInteract(Player playerIn, InteractionHand hand) {
        if (playerIn == leader) {
            deactivate();
        }
        return super.mobInteract(playerIn, hand);
    }

    private void deactivate() {
        if (getActive() && getActiveAbilityType() != DEACTIVATE_ABILITY) {
            AbilityHandler.INSTANCE.sendAbilityMessage(this, DEACTIVATE_ABILITY);
            playSound(MMSounds.ENTITY_UMVUTHANA_RETRACT.get(), 1, 1);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.MAX_HEALTH, 20);
    }

    @Override
    protected int getGroupCircleTick() {
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
        return CapabilityHandler.getCapability(leader, CapabilityHandler.PLAYER_CAPABILITY);
    }

    @Override
    public boolean isUmvuthiDevoted() {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return null;
    }

    @Nullable
    public UUID getOwnerId() {
        return getLeader() == null ? null : getLeader().getUUID();
    }

    @Nullable
    public Entity getOwner() {
        return leader;
    }

    public boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset)
    {
        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        BlockState iblockstate = this.level.getBlockState(blockpos);
        return iblockstate.isValidSpawn(this.level, blockpos, this.getType()) && this.level.isEmptyBlock(blockpos.above()) && this.level.isEmptyBlock(blockpos.above(2));
    }

    public ItemStack getStoredMask() {
        return getEntityData().get(MASK_STORED);
    }

    public void setStoredMask(ItemStack mask) {
        getEntityData().set(MASK_STORED, mask);
    }

    @Override
    protected ItemStack getDeactivatedMask(ItemUmvuthanaMask mask) {
        return getStoredMask();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        CompoundTag compoundnbt = compound.getCompound("storedMask");
        this.setStoredMask(ItemStack.of(compoundnbt));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (!this.getStoredMask().isEmpty()) {
            compound.put("storedMask", this.getStoredMask().save(new CompoundTag()));
        }
    }
}
