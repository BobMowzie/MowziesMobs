package com.bobmowzie.mowziesmobs.common.entity;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnad325 on 7/23/15.
 */
public class EntityTribeElite extends EntityTribesman {
    public List<EntityTribeHunter> pack = new ArrayList<>();
    public int packSize = 0;
    public EntityTribeElite(World world) {
        super(world);
        tasks.addTask(5, new EntityAIWander(this, 0.4));
        setMask(0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (pack != null)
        {
            updatePackSize();
            if (getAttackTarget() == null) {
                for (int i = 0; i < pack.size(); i++) {
                    if (pack.get(i) != null)
                        pack.get(i).getNavigator().tryMoveToXYZ(posX + 3 * Math.cos((2 * Math.PI / packSize) * i), posY, posZ + 3 * Math.sin((2 * Math.PI / packSize) * i), 0.45);
                }
            }
        }
    }

    private void updatePackSize() {
        packSize = 0;
        for (int i = 0; i < pack.size(); i++) {
            if (pack.get(i) != null) packSize++;
        }
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
    {
        int size = rand.nextInt(2) + 3;
        for(int i = 0; i <= size; i++)
        {
            pack.add(i, new EntityTribeHunter(worldObj, this));
            pack.get(i).setMask(0);
            pack.get(i).setIndex(i);
            pack.get(i).setLeaderID(getEntityId());
            pack.get(i).setPosition(posX + 0.1 * i, posY, posZ);
            worldObj.spawnEntityInWorld(pack.get(i));
        }
        return super.onSpawnWithEgg(p_110161_1_);
    }

    @Override
    public void onDeath(DamageSource p_70645_1_)
    {
        super.onDeath(p_70645_1_);
        for(int i = 0; i < pack.size(); i++)
        {
            if (pack.get(i) != null) pack.get(i).leader = null;
        }
    }
}
