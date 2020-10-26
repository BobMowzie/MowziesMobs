package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.PlayerCapability;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityBarakoanToPlayer extends EntityBarakoan<PlayerEntity> {
    public EntityBarakoanToPlayer(EntityType<? extends EntityBarakoanToPlayer> type, World world) {
        this(type, world, null);
    }

    public EntityBarakoanToPlayer(EntityType<? extends EntityBarakoanToPlayer> type, World world, PlayerEntity leader) {
        super(type, world, PlayerEntity.class, leader);
        experienceValue = 0;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        if (player == leader && getActive() && getAnimation() != DEACTIVATE_ANIMATION) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
            playSound(MMSounds.ENTITY_BARAKOA_RETRACT.get(), 1, 1);
        }
        return super.processInteract(player, hand);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20 * ConfigHandler.MOBS.BARAKOA.combatData.healthMultiplier);
    }

    @Override
    public int getAttack() {
        return 7;
    }

    @Override
    protected int getTribeCircleTick() {
        return getPlayerCapability().getTribeCircleTick();
    }

    @Override
    protected int getPackSize() {
        return getPlayerCapability().getPackSize();
    }

    @Override
    protected void addAsPackMember() {
        getPlayerCapability().addPackMember(this);
    }

    @Override
    protected void removeAsPackMember() {
        getPlayerCapability().removePackMember(this);
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
}
