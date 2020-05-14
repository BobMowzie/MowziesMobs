package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityBarakoanToPlayer extends EntityBarakoan<EntityPlayer> implements IEntityOwnable {
    public EntityBarakoanToPlayer(World world) {
        this(world, null);
    }

    public EntityBarakoanToPlayer(World world, EntityPlayer leader) {
        super(world, EntityPlayer.class, leader);
        experienceValue = 0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (player == leader && getActive()) {
            AnimationHandler.INSTANCE.sendAnimationMessage(this, DEACTIVATE_ANIMATION);
            playSound(MMSounds.ENTITY_BARAKOA_RETRACT, 1, 1);
        }
        return super.processInteract(player, hand);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20 * MowziesMobs.CONFIG.healthScaleBarakoa);
    }

    @Override
    public int getAttack() {
        return 7;
    }

    @Override
    protected int getTribeCircleTick() {
        return getPlayerProps().tribeCircleTick;
    }

    @Override
    protected int getPackSize() {
        return getPlayerProps().getPackSize();
    }

    @Override
    protected void addAsPackMember() {
        getPlayerProps().addPackMember(this);
    }

    @Override
    protected void removeAsPackMember() {
//        System.out.println(getPlayerProps().getPackSize());
        getPlayerProps().removePackMember(this);
//        System.out.println(getPlayerProps().getPackSize());
    }

    private MowziePlayerProperties getPlayerProps() {
        return EntityPropertiesHandler.INSTANCE.getProperties(leader, MowziePlayerProperties.class);
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
    @Override
    public UUID getOwnerId() {
        return getLeader() == null ? null : getLeader().getUniqueID();
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return leader;
    }

    public boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset)
    {
        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(this.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
    }
}
