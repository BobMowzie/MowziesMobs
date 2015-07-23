package com.bobmowzie.mowziesmobs.common.property;

import com.bobmowzie.mowziesmobs.common.item.MMItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import java.util.ArrayList;
import java.util.List;

public class WroughtAxeSwingHandler
{
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            return;
        }
        EntityPlayer player = event.player;
        WroughtAxeSwingProperty property = WroughtAxeSwingProperty.getProperty(player);

        if (player.getHeldItem() != null && player.getHeldItem().getItem() == MMItems.itemWroughtAxe)
        {
            if (property.getTime() > 0)
            {
                property.decrementTime();
            }
            if (property.getTime() == 15 && !player.worldObj.isRemote)
            {
                float damage = 7;
                boolean hit = false;
                float range = 4;
                float knockback = 1.2F;
                float arc = 100;
                List<EntityLivingBase> entitiesHit = getEntityLivingBaseNearby(player, range, 2, range, range);
                for (EntityLivingBase entityHit : entitiesHit)
                {
                    float entityHitAngle = (float) ((Math.atan2(entityHit.posZ - player.posZ, entityHit.posX - player.posX) * (180 / Math.PI) - 90) % 360);
                    float entityAttackingAngle = player.rotationYaw % 360;
                    if (entityHitAngle < 0)
                    {
                        entityHitAngle += 360;
                    }
                    if (entityAttackingAngle < 0)
                    {
                        entityAttackingAngle += 360;
                    }
                    float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                    float entityHitDistance = (float) Math.sqrt((entityHit.posZ - player.posZ) * (entityHit.posZ - player.posZ) + (entityHit.posX - player.posX) * (entityHit.posX - player.posX));
                    if (entityHitDistance <= range && entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2 || entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)
                    {
                        entityHit.attackEntityFrom(DamageSource.causeMobDamage(player), damage);
                        entityHit.motionX *= knockback;
                        entityHit.motionZ *= knockback;
                        hit = true;
                    }
                }
                if (hit)
                {
                    player.playSound("minecraft:random.anvil_land", 0.3F, 0.5F);
                }
            }
        }
    }

    private List<EntityLivingBase> getEntityLivingBaseNearby(EntityLivingBase user, double distanceX, double distanceY, double distanceZ, double radius)
    {
        List<Entity> list = user.worldObj.getEntitiesWithinAABBExcludingEntity(user, user.boundingBox.expand(distanceX, distanceY, distanceZ));
        ArrayList<EntityLivingBase> nearEntities = new ArrayList<EntityLivingBase>();
        for (Entity entityNeighbor : list)
        {
            if (entityNeighbor instanceof EntityLivingBase && user.getDistanceToEntity(entityNeighbor) <= radius)
            {
                nearEntities.add((EntityLivingBase) entityNeighbor);
            }
        }
        return nearEntities;
    }
}
