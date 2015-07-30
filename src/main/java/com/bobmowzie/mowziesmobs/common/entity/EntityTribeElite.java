package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnad325 on 7/23/15.
 */
public class EntityTribeElite extends EntityTribesman
{
    public List<EntityTribeHunter> pack = new ArrayList<EntityTribeHunter>();

    public EntityTribeElite(World world)
    {
        super(world);
        tasks.addTask(5, new EntityAIWander(this, 0.4));
        setMask(0);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (!worldObj.isRemote && pack != null)
        {
            if (getAttackTarget() == null)
            {
                float theta = (2 * (float) Math.PI / pack.size());
                for (int i = 0; i < pack.size(); i++)
                {
                    if (pack.get(i) != null)
                    {
                        pack.get(i).getNavigator().tryMoveToXYZ(posX + 3 * MathHelper.cos(theta * i), posY, posZ + 3 * MathHelper.sin(theta * i), 0.45);
                    }
                }
            }
        }
    }

    public void removePackMember(EntityTribeHunter tribeHunter)
    {
        pack.remove(tribeHunter);
    }

    public void addPackMember(EntityTribeHunter tribeHunter)
    {
        pack.add(tribeHunter);
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
        int size = rand.nextInt(2) + 3;
        for (int i = 0; i <= size; i++)
        {
            pack.add(i, new EntityTribeHunter(worldObj, this));
            pack.get(i).setMask(0);
            pack.get(i).setLeaderUUID(getUniqueID().toString());
            pack.get(i).setPosition(posX + 0.1 * i, posY, posZ);
            worldObj.spawnEntityInWorld(pack.get(i));
        }
        return super.onSpawnWithEgg(data);
    }

    @Override
    public void onDeath(DamageSource damageSource)
    {
        super.onDeath(damageSource);
        for (int i = 0; i < pack.size(); i++)
        {
            pack.get(i).removeLeader();
        }
    }
}
