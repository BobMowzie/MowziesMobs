package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataManager;
import net.minecraft.resources.ActionResultType;
import net.minecraft.resources.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityBarakoanToPlayer extends EntityBarakoan<Player> {
    private static final EntityDataAccessor<ItemStack> MASK_STORED = EntityDataManager.createKey(EntityBarakoanToPlayer.class, EntityDataSerializers.ITEMSTACK);
    @OnlyIn(Dist.CLIENT)
    public Vec3[] feetPos;

    public EntityBarakoanToPlayer(EntityType<? extends EntityBarakoanToPlayer> type, World world) {
        this(type, world, null);
    }

    public EntityBarakoanToPlayer(EntityType<? extends EntityBarakoanToPlayer> type, World world, Player leader) {
        super(type, world, Player.class, leader);
        experienceValue = 0;
        if (world.isClientSide) {
            feetPos = new Vec3[]{new Vec3(0, 0, 0)};
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(MASK_STORED, new ItemStack(ItemHandler.BARAKOA_MASK_FURY, 1));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (rand.nextFloat() < 0.5) return null;
        return super.getAmbientSound();
    }

    @Override
    public void tick() {
        if (ticksExisted > 30 && (getLeader() == null || getLeader().getHealth() <= 0)) {
            deactivate();
        }
        super.tick();
        if (world.isClientSide && feetPos != null && feetPos.length > 0) {
            feetPos[0] = getPositionVec().add(0, 0.05f, 0);
            if (ticksExisted % 10 == 0) AdvancedParticleBase.spawnParticle(world, ParticleHandler.RING2.get(), feetPos[0].getX(), feetPos[0].getY(), feetPos[0].getZ(), 0, 0, 0, false, 0, Math.PI/2f, 0, 0, 1.5F, 1, 223 / 255f, 66 / 255f, 1, 1, 15, true, false, new ParticleComponent[]{
                    new ParticleComponent.PinLocation(feetPos),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, ParticleComponent.KeyTrack.startAndEnd(1f, 0f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(1f, 10f), false)
            });
        }
    }

    @Override
    protected ActionResultType getEntityInteractionResult(Player playerIn, Hand hand) {
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
        return getDataManager().get(MASK_STORED);
    }

    public void setStoredMask(ItemStack mask) {
        getDataManager().set(MASK_STORED, mask);
    }

    @Override
    protected ItemStack getDeactivatedMask(ItemBarakoaMask mask) {
        return getStoredMask();
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        CompoundNBT compoundnbt = compound.getCompound("storedMask");
        this.setStoredMask(ItemStack.read(compoundnbt));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (!this.getStoredMask().isEmpty()) {
            compound.put("storedMask", this.getStoredMask().write(new CompoundNBT()));
        }
    }
}
