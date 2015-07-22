package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.common.animation.MMAnimBase;
import com.bobmowzie.mowziesmobs.common.animation.MMAnimation;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;

import java.util.ArrayList;
import java.util.List;

public class MMEntityBase extends EntityCreature implements IEntityAdditionalSpawnData, IAnimatedEntity, IntermittentAnimatableEntity
{
    private static final byte START_IA_HEALTH_UPDATE_ID = 4;

    public int frame;
    public float targetDistance;
    public float targetAngle;
    public MMAnimBase currentAnim = null;
    public boolean active;
    protected int deathLength = 30;
    private int animTick;
    private int animID;

    private List<IntermittentAnimation> intermittentAnimations = new ArrayList<IntermittentAnimation>();

    public MMEntityBase(World world)
    {
        super(world);
    }

    public void onUpdate()
    {
        super.onUpdate();
        frame++;
        if (animID != 0) animTick++;

        if (getAttackTarget() != null)
        {
            targetDistance = (float) Math.sqrt((getAttackTarget().posZ - posZ) * (getAttackTarget().posZ - posZ) + (getAttackTarget().posX - posX) * (getAttackTarget().posX - posX));
            targetAngle = (float) getAngleBetweenEntities(this, getAttackTarget());
        }
    }

    public void writeSpawnData(ByteBuf buffer)
    {

    }

    public void readSpawnData(ByteBuf additionalData)
    {

    }

    protected boolean isAIEnabled()
    {
        return true;
    }

    public int getAnimID()
    {
        return animID;
    }

    public void setAnimID(int i)
    {
        animID = i;
    }

    public int getAnimTick()
    {
        return animTick;
    }

    public void setAnimTick(int i)
    {
        animTick = i;
    }

    public int getAttack()
    {
        return 0;
    }

    public double getAngleBetweenEntities(Entity first, Entity second)
    {
        return Math.atan2(second.posZ - first.posZ, second.posX - first.posX) * (180 / Math.PI) + 90;
    }

    public List<EntityPlayer> getPlayersNearby(double distanceX, double distanceY, double distanceZ, double radius)
    {
        List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityPlayer> listEntityPlayers = new ArrayList<EntityPlayer>();
        for (Entity entityNeighbor : list)
        {
            if (entityNeighbor instanceof EntityPlayer && getDistanceToEntity(entityNeighbor) <= radius)
                listEntityPlayers.add((EntityPlayer) entityNeighbor);
        }
        return listEntityPlayers;
    }

    public List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius)
    {
        List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityLivingBase> listEntityLivingBase = new ArrayList<EntityLivingBase>();
        for (Entity entityNeighbor : list)
        {
            if (entityNeighbor instanceof EntityLivingBase && getDistanceToEntity(entityNeighbor) <= radius && entityNeighbor.posY >= posY && entityNeighbor.posY <= posY + distanceY)
                listEntityLivingBase.add((EntityLivingBase) entityNeighbor);
        }
        return listEntityLivingBase;
    }

    public void sendPacket(AbstractMessage packet)
    {
        if (!worldObj.isRemote)
        {
            MowziesMobs.networkWrapper.sendToAll(packet);
        }
    }

    protected void onDeathUpdate()
    {
        ++deathTime;

        if (deathTime == deathLength - 20)
        {
            int i;

            if (!worldObj.isRemote && (recentlyHit > 0 || isPlayer()) && func_146066_aG() && worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
            {
                i = getExperiencePoints(attackingPlayer);

                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, j));
                }
            }

            setDead();

            for (i = 0; i < 20; ++i)
            {
                double d2 = rand.nextGaussian() * 0.02D;
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("explode", posX + (double) (rand.nextFloat() * width * 2.0F) - (double) width, posY + (double) (rand.nextFloat() * height), posZ + (double) (rand.nextFloat() * width * 2.0F) - (double) width, d2, d0, d1);
            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, float damage)
    {
        boolean b = super.attackEntityFrom(source, damage);
        if (b)
        {
            if (getHealth() > 0.0F && getAnimID() == 0)
            {
                AnimationAPI.sendAnimPacket(this, MMAnimation.TAKEDAMAGE.animID());
            }
            else if (getHealth() <= 0.0F)
            {
                if (currentAnim != null) currentAnim.resetTask();
                AnimationAPI.sendAnimPacket(this, MMAnimation.DIE.animID());
            }
        }
        return b;
    }

    protected void addIntermittentAnimation(IntermittentAnimation intermittentAnimation)
    {
        intermittentAnimation.setID((byte) intermittentAnimations.size());
        intermittentAnimations.add(intermittentAnimation);
    }

    @Override
    public void handleHealthUpdate(byte id)
    {
        if (id >= START_IA_HEALTH_UPDATE_ID && id - START_IA_HEALTH_UPDATE_ID < intermittentAnimations.size())
        {
            intermittentAnimations.get(id - START_IA_HEALTH_UPDATE_ID).start();
            return;
        }
        super.handleHealthUpdate(id);
    }

    @Override
    public byte getOffsetEntityState()
    {
        return START_IA_HEALTH_UPDATE_ID;
    }

    public void circleEntity(Entity target, float radius, float speed, boolean direction)
    {
        getNavigator().tryMoveToXYZ(target.posX + radius * Math.cos(frame * 0.5 * speed/radius), target.posY, target.posZ + radius * Math.sin(frame * 0.5 * speed/radius), speed);
    }
}
