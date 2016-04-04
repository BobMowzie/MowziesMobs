package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.client.model.tools.IntermittentAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationAI;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MowzieEntity extends EntityCreature implements IEntityAdditionalSpawnData, IAnimatedEntity, IntermittentAnimatableEntity {
    private static final byte START_IA_HEALTH_UPDATE_ID = 4;

    public int frame;
    public float targetDistance;
    public float targetAngle;
    public AnimationAI currentAnim = null;
    public boolean active;
    private int animationTick;
    private Animation animation;

    public EntityLivingBase blockingEntity = null;

    private List<IntermittentAnimation> intermittentAnimations = new ArrayList<>();

    public MowzieEntity(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        frame++;
        if (getAnimation() != NO_ANIMATION) {
            animationTick++;
        }
        if (getAttackTarget() != null) {
            targetDistance = (float) Math.sqrt((getAttackTarget().posZ - posZ) * (getAttackTarget().posZ - posZ) + (getAttackTarget().posX - posX) * (getAttackTarget().posX - posX));
            targetAngle = (float) getAngleBetweenEntities(this, getAttackTarget());
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    public int getAttack() {
        return 0;
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.posZ - first.posZ, second.posX - first.posX) * (180 / Math.PI) + 90;
    }

    public List<EntityPlayer> getPlayersNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityPlayer> listEntityPlayers = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof EntityPlayer && getDistanceToEntity(entityNeighbor) <= radius).map(entityNeighbor -> (EntityPlayer) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return listEntityPlayers;
    }

    public List<EntityLivingBase> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityLivingBase> listEntityLivingBase = nearbyEntities.stream().filter(entityNeighbor -> entityNeighbor instanceof EntityLivingBase && getDistanceToEntity(entityNeighbor) <= radius && entityNeighbor.posY + entityNeighbor.boundingBox.maxY > posY + 2 && entityNeighbor.posY <= posY + distanceY).map(entityNeighbor -> (EntityLivingBase) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
        return listEntityLivingBase;
    }

    @Override
    protected void onDeathUpdate() {
        ++deathTime;

        if (deathTime == getDeathAnimation().getDuration() - 20) {
            int experience;

            if (!worldObj.isRemote && (recentlyHit > 0 || isPlayer()) && func_146066_aG() && worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                experience = getExperiencePoints(attackingPlayer);

                while (experience > 0) {
                    int j = EntityXPOrb.getXPSplit(experience);
                    experience -= j;
                    worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, j));
                }
            }

            this.setDead();

            for (experience = 0; experience < 20; ++experience) {
                double d2 = rand.nextGaussian() * 0.02D;
                double d0 = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("explode", posX + (double) (rand.nextFloat() * width * 2.0F) - (double) width, posY + (double) (rand.nextFloat() * height), posZ + (double) (rand.nextFloat() * width * 2.0F) - (double) width, d2, d0, d1);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        boolean attack = super.attackEntityFrom(source, damage);
        if (attack) {
            if (getHealth() > 0.0F && getAnimation() == NO_ANIMATION) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, getHurtAnimation());
            } else if (getHealth() <= 0.0F) {
                if (currentAnim != null) {
                    currentAnim.resetTask();
                }
                AnimationHandler.INSTANCE.sendAnimationMessage(this, getDeathAnimation());
            }
        }
        return attack;
    }

    protected void addIntermittentAnimation(IntermittentAnimation animation) {
        animation.setID((byte) intermittentAnimations.size());
        intermittentAnimations.add(animation);
    }

    @Override
    public void handleHealthUpdate(byte id) {
        if (id >= START_IA_HEALTH_UPDATE_ID && id - START_IA_HEALTH_UPDATE_ID < intermittentAnimations.size()) {
            intermittentAnimations.get(id - START_IA_HEALTH_UPDATE_ID).start();
            return;
        }
        super.handleHealthUpdate(id);
    }

    @Override
    public byte getOffsetEntityState() {
        return START_IA_HEALTH_UPDATE_ID;
    }

    public void circleEntity(Entity target, float radius, float speed, boolean direction, int circleFrame, float offset, float moveSpeedMultiplier) {
        int directionInt = direction ? 1 : -1;
        this.getNavigator().tryMoveToXYZ(target.posX + radius * Math.cos(directionInt * circleFrame * 0.5 * speed / radius + offset), target.posY, target.posZ + radius * Math.sin(directionInt * circleFrame * 0.5 * speed / radius + offset), speed * moveSpeedMultiplier);
    }

    protected void repelEntities(float x, float y, float z, float radius) {
        List<EntityLivingBase> nearbyEntities = getEntityLivingBaseNearby(x, y, z, radius);
        for (Entity entity : nearbyEntities) {
            double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
            entity.motionX = -0.1 * Math.cos(angle);
            entity.motionZ = -0.1 * Math.sin(angle);
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public abstract Animation getDeathAnimation();

    public abstract Animation getHurtAnimation();
}
