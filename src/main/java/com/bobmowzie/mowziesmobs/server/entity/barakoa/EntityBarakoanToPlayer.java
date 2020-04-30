package com.bobmowzie.mowziesmobs.server.entity.barakoa;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
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

    @Override
    protected void dropLoot() {
        return;
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
