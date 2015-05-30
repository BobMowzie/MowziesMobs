package com.bobmowzie.mowziesmobs.entity;

import com.bobmowzie.mowziesmobs.ai.animation.AnimDie;
import com.bobmowzie.mowziesmobs.ai.animation.AnimFWNAttack;
import com.bobmowzie.mowziesmobs.ai.animation.AnimFWNVerticalAttack;
import com.bobmowzie.mowziesmobs.ai.animation.AnimTakeDamage;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

public class EntityWroughtnaut extends MMEntityBase {
    public double walkFrame;
    public ControlledAnimation walkAnim = new ControlledAnimation(10);
    public boolean swingDirection = false;
    public boolean vulnerable = false;

    public EntityWroughtnaut(World world) {
        super(world);
        deathLength = 83;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new AnimFWNAttack(this, 50, "mowziesmobs:wroughtnautWhoosh", 4F, 5.5F, 100F));
        tasks.addTask(1, new AnimFWNVerticalAttack(this, 105, "mowziesmobs:wroughtnautWhoosh", 1F, 5.5F, 40F));
        tasks.addTask(1, new AnimTakeDamage(this, 15));
        tasks.addTask(1, new AnimDie(this, deathLength));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
        experienceValue = 20;
        this.setSize(3F, 4F);
    }

    @Override
    public int getAttack() {
        return 40;
    }

    public String getHurtSound()
    {
        return "mowziesmobs:wroughtnautHurt1";
    }

    public String getDeathSound()
    {
        return "mowziesmobs:wroughtnautScream";
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (vulnerable)
        {
            if (currentAnim != null) currentAnim.resetTask();
            return super.attackEntityFrom(p_70097_1_, p_70097_2_);
        }
        else playSound("minecraft:random.anvil_land", 0.4F, 2F);
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (getAttackTarget() != null)
        {
            if (getAnimID() == 0) getNavigator().tryMoveToEntityLiving(this.getAttackTarget(), 0.2D);
            else getNavigator().clearPathEntity();

            if (targetDistance <= 5 && getAttackTarget().posY - posY >= -1 && getAttackTarget().posY - posY <= 3 && getAnimID() == 0)
            {
                int i = (int) (2 * Math.random() + 0.5);
                if (i == 0) AnimationAPI.sendAnimPacket(this, 5);
                else AnimationAPI.sendAnimPacket(this, MMAnimation.ATTACK.animID());
            }
        }
        if (getAnimID() == MMAnimation.ATTACK.animID() && getAnimTick() == 1)
        {
            int i = (int) (Math.random() + 0.5);
            if (i == 0) swingDirection = false;
            if (i == 1) swingDirection = true;
        }

        if (getAnimID() == 5 && getAnimTick() == 29)
        {
            int i = MathHelper.floor_double(posX + 4 * Math.cos((renderYawOffset + 90) * Math.PI/180));
            int j = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
            int k = MathHelper.floor_double(posZ + 4 * Math.sin((renderYawOffset + 90) * Math.PI/180));
            Block block = this.worldObj.getBlock(i, j, k);

            if (block.getMaterial() != Material.air)
            {
                for (int n = 0; n <= 20; n++) this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + this.worldObj.getBlockMetadata(i, j, k), posX + 4.5 * Math.cos((renderYawOffset + 90) * Math.PI/180) + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.boundingBox.minY + 0.1D, posZ + 4.5 * Math.sin((renderYawOffset + 90) * Math.PI/180) + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 3D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D);
            }
        }

        else if (worldObj.isRemote) getNavigator().clearPathEntity();

        double walkFrameIncrement = 1.5 * Math.pow(Math.sin((Math.PI * 0.05)*(frame - 9)), 2) + 0.25;
        walkFrame += walkFrameIncrement;

        float moveX = (float) (posX - prevPosX);
        float moveZ = (float) (posZ - prevPosZ);
        float speed = (float) Math.sqrt(moveX*moveX + moveZ*moveZ);
        if (speed > 0.02)
        {
            if (getAnimID() == 0) walkAnim.increaseTimer();
        }
        else walkAnim.decreaseTimer();
        if (getAnimID() != 0) walkAnim.decreaseTimer(2);

        if (frame % 20 == 3 && speed > 0.03) playSound("mob.zombie.metal", 0.5F, 0.5F);

        if (getAnimID() == 0) AnimationAPI.sendAnimPacket(this, MMAnimation.DIE.id);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}