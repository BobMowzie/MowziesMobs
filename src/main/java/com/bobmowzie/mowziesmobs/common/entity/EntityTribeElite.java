package com.bobmowzie.mowziesmobs.common.entity;

import com.bobmowzie.mowziesmobs.common.animation.AnimBlock;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnad325 on 7/23/15.
 */
public class EntityTribeElite extends EntityTribesman
{
    private List<EntityTribeHunter> pack = new ArrayList<EntityTribeHunter>();

    private int packRadius = 3;

    boolean vulnerable = false;

    public EntityTribeElite(World world)
    {
        super(world);
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCow.class, 0, true));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPig.class, 0, true));
        targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySheep.class, 0, true));
        tasks.addTask(2, new AnimBlock(this, 2, 10));
        setMask(0);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        for (int i = 0; i < pack.size(); i++)
        {
            pack.get(i).index = i;
        }

        if (!worldObj.isRemote && pack != null)
        {
            if (getAttackTarget() == null)
            {
                float theta = (2 * (float) Math.PI / pack.size());
                for (int i = 0; i < pack.size(); i++)
                {
                    pack.get(i).getNavigator().tryMoveToXYZ(posX + packRadius * MathHelper.cos(theta * i), posY, posZ + packRadius * MathHelper.sin(theta * i), 0.45);
                }
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (source.getEntity() != null && !vulnerable) {
            playSound("mob.zombie.wood", 0.3f, 1.5f);
            faceEntity(source.getEntity(), 100, getVerticalFaceSpeed());
            getLookHelper().setLookPositionWithEntity(source.getEntity(), 200F, 30F);
            AnimationAPI.sendAnimPacket(this, 2);
            return false;
        }
        return super.attackEntityFrom(source, damage);
    }

    public int getpackSize() {
        return pack.size();
    }

    public void removePackMember(EntityTribeHunter tribeHunter)
    {
        pack.remove(tribeHunter);
        sortPackMembers();
    }

    public void addPackMember(EntityTribeHunter tribeHunter)
    {
        pack.add(tribeHunter);
        sortPackMembers();
    }

    private void sortPackMembers()
    {
        double theta = 2 * Math.PI / pack.size();
        for (int i = 0; i < pack.size(); i++)
        {
            int nearestIndex = -1;
            double smallestDiffSq = Double.MAX_VALUE;
            double targetTheta = theta * i;
            double x = posX + packRadius * Math.cos(targetTheta);
            double z = posZ + packRadius * Math.sin(targetTheta);
            for (int n = 0; n < pack.size(); n++)
            {
                EntityTribeHunter tribeHunter = pack.get(n);
                double diffSq = (x - tribeHunter.posX) * (x - tribeHunter.posX) + (z - tribeHunter.posZ) * (z - tribeHunter.posZ);
                if (diffSq < smallestDiffSq)
                {
                    smallestDiffSq = diffSq;
                    nearestIndex = n;
                }
            }
            if (nearestIndex == -1)
            {
                throw new ArithmeticException("All pack members have NaN x and z?");
            }
            pack.add(i, pack.remove(nearestIndex));
        }
    }

    public int getPackSize()
    {
        return pack.size();
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
        int size = rand.nextInt(2) + 3;
        for (int i = 0; i <= size; i++)
        {
            EntityTribeHunter tribeHunter = new EntityTribeHunter(worldObj, this);
            pack.add(tribeHunter);
            tribeHunter.setMask(0);
            tribeHunter.setLeaderUUID(getUniqueID().toString());
            tribeHunter.setPosition(posX + 0.1 * i, posY, posZ);
            worldObj.spawnEntityInWorld(tribeHunter);
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
