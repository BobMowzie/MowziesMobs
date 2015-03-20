package com.bobmowzie.mowziesmobs.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBabyFoliaath extends MMEntityBase {
    public EntityBabyFoliaath(World world) {
        super(world);
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        setSize(0.4F, 0.4F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1);
    }

    public void onUpdate()
    {
        super.onUpdate();
        motionX = 0;
        motionZ = 0;
        renderYawOffset = 0;
    }

    @Override
    public void onDeath(DamageSource p_70645_1_) {
        super.onDeath(p_70645_1_);
        for (int i = 0; i < 10; i++)
        {
            worldObj.spawnParticle("blockcrack_18_3", posX, posY + 0.2, posZ, 0, 0, 0);
        }
        setDead();
    }

    @Override
    public void applyEntityCollision(Entity p_70108_1_)
    {
        posX = prevPosX;
        posZ = prevPosZ;
    }

    @Override
    protected String getDeathSound() {
        playSound("dig.grass", 1, 0.8F);
        return null;
    }

    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox)) {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.boundingBox.minY);
            int k = MathHelper.floor_double(this.posZ);

            Block block = this.worldObj.getBlock(i, j - 1, k);

            if (block == Blocks.grass || block.isLeaves(worldObj, i, j - 1, k)|| block == Blocks.dirt) {
                return true;
            }
        }
        return false;
    }
}