package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
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
        if (!world.isRemote && getAttackTarget() != null && getAttackTarget().isDead) setAttackTarget(null);
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
    public UUID getOwnerId() {
        return getLeader() == null ? null : getLeader().getUniqueID();
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return leader;
    }
}
