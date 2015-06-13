package com.bobmowzie.mowziesmobs.entity;

import com.bobmowzie.mowziesmobs.ai.animation.AnimDie;
import com.bobmowzie.mowziesmobs.ai.animation.AnimFWNAttack;
import com.bobmowzie.mowziesmobs.ai.animation.AnimFWNVerticalAttack;
import com.bobmowzie.mowziesmobs.ai.animation.AnimTakeDamage;
import com.bobmowzie.mowziesmobs.client.model.animation.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.enums.MMAnimation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;

public class EntityWroughtnaut extends MMEntityBase {
    public double walkFrame;
    public ControlledAnimation walkAnim = new ControlledAnimation(10);
    public boolean swingDirection = false;
    public boolean vulnerable = false;
    private int attacksWithoutVertical = 0;

    public EntityWroughtnaut(World world) {
        super(world);
        deathLength = 130;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new AnimFWNAttack(this, 50, "mowziesmobs:wroughtnautWhoosh", 4F, 5.5F, 100F));
        tasks.addTask(1, new AnimFWNVerticalAttack(this, 105, "mowziesmobs:wroughtnautWhoosh", 1F, 5.5F, 40F));
        tasks.addTask(1, new AnimTakeDamage(this, 15));
        tasks.addTask(1, new AnimDie(this, deathLength));
        tasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
        experienceValue = 30;
        this.setSize(3F, 4F);
    }

    @Override
    public int getAttack() {
        return 30;
    }

    public String getHurtSound()
    {
        return "mowziesmobs:wroughtnautHurt1";
    }

    public String getDeathSound()
    {
        playSound("mowziesmobs:wroughtnautScream", 1, 1);
        return null;
    }

    @Override
    protected String getLivingSound() {
        if (getAnimID() == 0 && getAttackTarget() != null)
        {
            int i = MathHelper.getRandomIntegerInRange(this.rand, 0, 3);
            if (i == 0) return "mowziesmobs:wroughtnautGrunt1";
            if (i == 1) return "mowziesmobs:wroughtnautGrunt2";
            if (i == 2) return "mowziesmobs:wroughtnautGrunt3";
        }
        return null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0);
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float p_70097_2_) {
        if (vulnerable && source.getEntity() != null)
        {
            int arc = 220;
            Entity entitySource = source.getEntity();
            float entityHitAngle = (float) ((Math.atan2(entitySource.posZ - posZ, entitySource.posX - posX) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = renderYawOffset % 360;
            if (entityHitAngle < 0) entityHitAngle += 360;
            if (entityAttackingAngle < 0) entityAttackingAngle += 360;
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            if ((entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -arc + 90 / 2))
            {
                playSound("minecraft:random.anvil_land", 0.4F, 2F);
                return false;
            }
            else
            {
                if (currentAnim != null) currentAnim.resetTask();
                return super.attackEntityFrom(source, p_70097_2_);
            }
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
                int i = (int) (3 * Math.random() + 0.5);
                if (attacksWithoutVertical == 4) i = 0;
                if (i == 0)
                {
                    AnimationAPI.sendAnimPacket(this, 5);
                    attacksWithoutVertical = 0;
                }
                else AnimationAPI.sendAnimPacket(this, MMAnimation.ATTACK.animID());
            }
        }
        else getNavigator().tryMoveToXYZ((double)getRestPosX(), (double)getRestPosY(), (double)getRestPosZ(), 0.2D);

        if (getAnimID() == MMAnimation.ATTACK.animID() && getAnimTick() == 1)
        {
            attacksWithoutVertical++;
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

        if (frame % 20 == 3 && speed > 0.03 && getAnimID() == 0) playSound("mob.zombie.metal", 0.5F, 0.5F);
    }

    public void onSpawn()
    {
        System.out.print("On Spawn");
        setRestPosX((int) posX);
        setRestPosY((int) posY);
        setRestPosZ((int) posZ);
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
        onSpawn();
        return super.onSpawnWithEgg(p_110161_1_);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(29, new Integer(0));
        dataWatcher.addObject(30, new Integer(0));
        dataWatcher.addObject(31, new Integer(0));
    }

    public void setRestPosX(Integer restPosX) {
        dataWatcher.updateObject(29, new Integer(restPosX));
    }

    public int getRestPosX() {
        return dataWatcher.getWatchableObjectInt(29);
    }

    public void setRestPosY(Integer restPosY) {
        dataWatcher.updateObject(30, new Integer(restPosY));
    }

    public int getRestPosY() {
        return dataWatcher.getWatchableObjectInt(30);
    }

    public void setRestPosZ(Integer restPosZ) {
        dataWatcher.updateObject(31, new Integer(restPosZ));
    }

    public int getRestPosZ() {
        return dataWatcher.getWatchableObjectInt(31);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("restPosX", getRestPosX());
        compound.setInteger("restPosY", getRestPosY());
        compound.setInteger("restPosZ", getRestPosZ());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        setRestPosX(compound.getInteger("restPosX"));
        setRestPosY(compound.getInteger("restPosY"));
        setRestPosZ(compound.getInteger("restPosZ"));
    }
}